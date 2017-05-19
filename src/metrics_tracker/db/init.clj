(ns metrics-tracker.db.init
  (:require [datomic.api :as d]
            [metrics-tracker.conf :as conf])
  (:use [metrics-tracker.db.schema]))

(defn init-db [init-data]
  (d/delete-database (:db-uri conf/configuration))
  (d/create-database (:db-uri conf/configuration))
  (let [conn (d/connect (:db-uri conf/configuration))]
    @(d/transact conn (schema-def))
    (vals (:tempids @(d/transact conn init-data)))))


