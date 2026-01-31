(ns uselessapp.migrate
  (:require
   [uselessapp.config :as config]
   [migratus.core :as migratus]))

(defn migratus-config [cfg]
  {:store         :database
   :migration-dir "migrations"
   :db            (:db cfg)})

(defn -main [& _]
  (let [cfg (config/read-config!)
        mcfg (migratus-config cfg)]
    (println "Running DB migrations...")
    (migratus/migrate mcfg)
    (println "Migrations finished")))