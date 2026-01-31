(ns uselessapp.infra.http.views.main
  (:require [hiccup.page :as page])
  )

(defn page []
  (page/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Main Page"]]
   [:body
    [:h1 "Welcome to the Useless App!"]
    [:p "This is the main page of the Useless App."]
    [:p "Go to /hello/{your-name} to be greeted!"]
    [:p "Go to /factorial/{your-number} to get the factorial!"]
    [:p "Go to /weather/{your-location} to get the weather!"]
    [:p "Greeting will transfered from kafka, resversed and stored in the database."]
    [:a {:href "/hello"} "Said hello list"]
    [:hr]]))