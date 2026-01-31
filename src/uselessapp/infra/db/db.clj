(ns uselessapp.infra.db.db
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))


(defn initdb! [ds]
  (jdbc/execute! ds ["
    create table if not exists events (
    id int generated always as identity primary key,
    event varchar(200),
    name varchar(200)
  )"])
  )


(defn get-events [ds]
  (jdbc/execute! ds
                 ["select event, name from events"]
                 {:builder-fn rs/as-unqualified-lower-maps}))

(defn truncate-events! [ds] (jdbc/execute! ds ["truncate table events"]))
