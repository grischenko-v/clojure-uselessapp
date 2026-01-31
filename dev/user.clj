(ns user
  (:require
   [clojure.tools.namespace.repl :as tn]
   [next.jdbc :as jdbc]
   [uselessapp.config :as config]
   [uselessapp.infra.db.db :refer [get-events]]
   [uselessapp.system :as system]))

(defonce system* (atom nil))

(defn start []
  (reset! system* (system/start! (config/read-config!)))
  :started)

(defn stop []
  (when-let [s @system*]
    (system/stop! s)
    (reset! system* nil))
  :stopped)

(defn reset []
  (stop)
  (tn/refresh :after 'user/start))

(comment
(let [ds (jdbc/get-datasource (:db (config/read-config!)))]
  (get-events ds))
)


