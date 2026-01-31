(ns uselessapp.infra.http.router
  (:require
   [reitit.ring :as ring]
   [uselessapp.infra.http.handlers.handler :as handler]))

(defn make-router [ds]
  (ring/router
   [["/" handler/home]
    ["/health" handler/health]
    ["/hello" (handler/hello-default ds)]
    ["/hello/:name" handler/hello]
    ["/factorial/:number" handler/factorial]
    ["/weather/:location" handler/weather]
    ]))

(defn make-app [{:keys [ds]}]
  (ring/ring-handler
   (make-router ds)
   (ring/create-default-handler)))