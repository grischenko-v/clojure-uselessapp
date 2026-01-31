(ns uselessapp.system
  (:require
   [next.jdbc :as jdbc]
   [uselessapp.infra.db.migrations :as migrations]
   [uselessapp.infra.http.http :as http]
   [uselessapp.infra.http.router :as router]
   [uselessapp.infra.kafka.consumer :as consumer]
   [uselessapp.usecase.handleconsumeevent :refer [insert-event-handler]]))

(defn start! [cfg]
  (when (get-in cfg [:db :migrate-on-start?])
    (migrations/migrate! cfg))
  (let [ds (jdbc/get-datasource (:db cfg))
        app (router/make-app {:ds ds})
        http-server (http/start! (:http cfg) app)
        kafka-handler (insert-event-handler ds)
        consumer-h (when-let [k (:kafka cfg)]
                     (consumer/start-consumer! k kafka-handler))]
    {:cfg cfg :ds ds :http http-server :consumer consumer-h}))

(defn stop! [{:keys [http consumer]}]
  (when consumer (consumer/stop-consumer! consumer))
  (when http (http/stop! http))
  :stopped)