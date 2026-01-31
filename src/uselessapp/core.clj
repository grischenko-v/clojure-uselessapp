(ns uselessapp.core
  (:require
   [next.jdbc :as jdbc]
   [uselessapp.config :as config]
   [uselessapp.infra.db.db :refer [get-events truncate-events!]]
   [uselessapp.system :as system])
  (:gen-class))

(defn -main [& _]
  (let [cfg (config/read-config!)]
    (system/start! cfg)))


(comment 
  (system/stop! {:http nil})

  (let [cfg (config/read-config!)]
  (let [ds (jdbc/get-datasource (:db cfg))]
  (get-events ds) 
    ))
   (let [cfg (config/read-config!)]
    (let [ds (jdbc/get-datasource (:db cfg))]
      (truncate-events! ds)
      ))
  )

