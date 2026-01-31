(ns uselessapp.infra.http.http
  (:require [ring.adapter.jetty :refer [run-jetty]]))

(defn start! [{:keys [port]} app]
  (when-not port
    (throw (ex-info "HTTP port is nil" {})))
  (run-jetty app {:port port :join? false}))

(defn stop! [server]
  (when server
    (.stop server)))