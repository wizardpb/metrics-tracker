(ns metrics-tracker.system
  (:require [datomic.api :as d]))

(defn system [conf]
  {
   :db {:uri (:db-uri conf)}
   })

(defn start [s]
  (-> s
    (assoc-in [:db :conn] (d/connect (get-in s [:db :uri])))
    ))

(defn stop [s]
  (-> s
    (dissoc :db :conn)))