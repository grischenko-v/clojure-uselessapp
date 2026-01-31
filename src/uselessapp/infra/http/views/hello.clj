(ns uselessapp.infra.http.views.hello
  (:require [hiccup.page :as page])
  )

(defn page [{:keys [name]}]
  (page/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Hello"]]
   [:body
    [:h1 "Hello, " (or name "stranger") "!"]
    [:a {:href "/"} "Home"]
    [:hr]]))

