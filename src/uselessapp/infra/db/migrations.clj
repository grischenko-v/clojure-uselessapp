(ns uselessapp.infra.db.migrations
  (:require [migratus.core :as migratus]))

(defn migrate! [cfg]
  (migratus/migrate
   {:store :database
    :migration-dir "migrations"
    :db (:db cfg)}))