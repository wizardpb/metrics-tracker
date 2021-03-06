 (ns user
   (:require [org.httpkit.client :as http]
             [cheshire.core :refer :all]
             [clojure.java.io :as io]
             [clojure.string :as str]
             [clojure.pprint :refer (pprint)]
             [clojure.repl :refer :all]
             [clojure.test :as test]
             [clojure.tools.namespace.repl :refer (refresh refresh-all)]
             [datomic.api :as d]
             [metrics-tracker.conf :as conf]
             [metrics-tracker.system :as system]
             [metrics-tracker.adapters.tracker :as tr]
             [metrics-tracker.db.init :refer :all]))

(def system nil)

(defn init
  "Constructs the current development system."
  []
  (alter-var-root #'system
    (constantly (system/system conf/configuration))))

(defn start
  "Starts the current development system."
  []
  (alter-var-root #'system system/start))

(defn stop
  "Shuts down and destroys the current development system."
  []
  (alter-var-root #'system
    (fn [s] (when s (system/stop s)))))

(defn go
  "Initializes the current development system and starts it running."
  []
  (init)
  (start))

(defn reset []
  (stop)
  (refresh :after 'user/go))

(defn create-db []
  (let [eids (init-db [{:db/id        "p1"
                        :project/name "GIC Index"
                        :project/id   "1660137"
                        :project/type :tracker
                        :project.tracker/api-token "eccca23c47f1ed3e7de008bcd023d7b7"}
                       {:db/id        "p2"
                        :project/name "Metaweb"
                        :project/id   "156907"
                        :project/type :tracker
                        :project.tracker/api-token "eccca23c47f1ed3e7de008bcd023d7b7"}
                       {:db/id        "p3"
                        :project/name "PMS - Portfolio Management System"
                        :project/id   "695031"
                        :project/type :tracker
                        :project.tracker/api-token "eccca23c47f1ed3e7de008bcd023d7b7"}
                       ])
        db (d/db (d/connect (:db-uri conf/configuration)))]
    (map #(d/pull db '[*] %) eids))
  )

