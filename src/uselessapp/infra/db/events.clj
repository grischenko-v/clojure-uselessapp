(ns uselessapp.infra.db.events
  (:require [next.jdbc :as jdbc]))

(defn insert-event! [ds {:keys [event name]}]
  (jdbc/execute! ds
                 ["insert into events(event, name) values (?, ?)"
                  (str event)
                  (when name (str name))]))