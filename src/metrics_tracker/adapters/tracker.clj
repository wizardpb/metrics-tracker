(ns metrics-tracker.adapters.tracker
  (:require [org.httpkit.client :as http]
            [cheshire.core :refer :all])
  (:import (java.util Date)))

(defn call-api
  "Call the Tracker API at endpoint using method as the verb and token as authentication. The endpoint is prefixed
  with service v5 URL '/services/v5'. Body is an optional data structure that will be JSON encoded and used as the
  request body. method and body must be consistent"
  ([method endpoint token opts]
   (let [service-prefix "https://www.pivotaltracker.com/services/v5"
         {:keys [body params]} opts
         request-opts {:method       method
                       :url          (if (= (nth endpoint 0) \/) (str service-prefix endpoint) endpoint)
                       :body         (when body (generate-string body))
                       :headers      {
                                      "X-TrackerToken" token
                                      }
                       :query-params params
                       }]
     ;(println request-opts)
     (let [response (update @(http/request request-opts) :body #(parse-string % true))]
       (println response)
       (if (>= (:status response) 400)
         (throw (UnsupportedOperationException. ^String (get-in response [:body :error])))
         (:body response)))))
  ([method endpoint token] (call-api method endpoint token {}))
  )

(defn- ch-key [new-key]
  (fn [val] [new-key val]))

(defn- transform-keys
  "Return a transformed map, mapping values at keys in (keys mapping) to values at keys (vals mapping)"
  [mapping input]
  (into {}
    (map (fn [[in-key map-key]] (when (contains? input in-key) [map-key (in-key input)])) mapping)))

(defn to-date [millis]
  (Date. ^Long millis))

(defn update-if-present [map key fn]
  (if (contains? map key)
    (update map key fn)
    map))

(defn create-story
  "Create a story from tracker story data"
  [data]
  (-> (transform-keys
     {
      :id            :story/id
      :current_state :story/state
      :accepted_at   :story/finished-time
      :estimate      :story/estimate}
     data)
    (update-if-present :story/state #(cond
                            (#{"accepted" "delivered" "finished"} %) :done
                            (#{"started"} %) :in-progress
                            (#{"rejected"} %) :rejected
                            :else :ready))
    (update-if-present :story/finished-time to-date)
    (update-if-present :story/estimate #(float %)))
  )

(defn create-iteration [data]
  (-> (transform-keys
        {
         :number  :iter/id
         :stories :iter/stories
         :start   :iter/start-date
         :finish  :iter/end-date
         } data)
    (update-if-present :iter/stories #(map create-story %))
    (update-if-present :iter/start-date to-date)
    (update-if-present :iter/end-date to-date)))

(defn previous-iterations
  "Return the last n completed iterations along with their stories"
  ([project n limit]
   (->>
     (call-api :get (str "/projects/" (.toString project) "/iterations") "eccca23c47f1ed3e7de008bcd023d7b7"
       {:params {:scope "done" :offset (str "-" (.toString n)) :limit (.toString limit) :date_format "millis"}})
     (map create-iteration)))
  ([project n] (previous-iterations project n 5)))

