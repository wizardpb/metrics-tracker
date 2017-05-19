(ns metrics-tracker.adapters.tracker
  (:require [org.httpkit.client :as http]
            [cheshire.core :refer :all]))

(defn call-api
  "Call the Tracker API at endpoint using method as the verb and token as authentication. The endpoint is prefixed
  with service v5 URL '/services/v5'. Body is an optional data structure that will be JSON encoded and used as the
  request body. method and body must be consistent"
  ([method endpoint token opts]
   (let [service-prefix "https://www.pivotaltracker.com/services/v5"
         {:keys [body params]} opts
         request-opts {:method       method
                       :url          (str service-prefix endpoint)
                       :body         (when body (generate-string body))
                       :headers      {
                                      "X-TrackerToken" token
                                      }
                       :query-params params
                       }]
     ;(println request-opts)
     (let [response (update @(http/request request-opts) :body #(parse-string % true))]
       ;(println response)
       (if (>= (:status response) 400)
         (throw (UnsupportedOperationException. ^String (get-in response [:body :error])))
         (:body response)))))
  ([method endpoint token] (call-api method endpoint token {}))
  )

