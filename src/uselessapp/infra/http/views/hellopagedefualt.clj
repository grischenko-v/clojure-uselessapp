(ns uselessapp.infra.http.views.hellopagedefualt
  (:require [hiccup.page :as page]))

(defn page [{:keys [title events]}]
  (page/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Hello"]]
   [:body
    [:a {:href "/"} "Home"]
    [:br]
    [:a {:href "/hello"} "Said hello list"]
    [:hr]
    [:h1 "How say hello? " title]

    [:h2 "Events"]
    (if (seq events)
      [:ul
       (for [{:keys [name event]} events]
         [:li (str (or name "unknown") " : " event)])]
      [:p [:em "No events yet"]])

    [:hr]]))