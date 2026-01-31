(ns uselessapp.infra.http.handlers.handler
  (:require
   [ring.util.response :as resp]
   [uselessapp.infra.clients.weatherclient :refer [get-weather]]
   [uselessapp.infra.db.db :as db]
   [uselessapp.infra.http.views.hello :as hellopage]
   [uselessapp.infra.http.views.hellopagedefualt :as hellopagedefualt]
   [uselessapp.infra.http.views.main :as mainpage]
   [uselessapp.infra.kafka.producer :as producer]
   [uselessapp.usecase.factorial :as factorial]
   [uselessapp.usecase.greeting :as greetingusecase]))

(defn home [_]
  (let [html (mainpage/page)]
    (-> (resp/response html)
        (resp/content-type "text/html; charset=UTF-8"))))

(defn health [_]
  (-> (resp/response "ok\n")
      (resp/content-type "text/plain; charset=UTF-8")))


(defn hello-default [ds]
  (fn [{:keys [query-params]}]
    (let [title  (get query-params "title" "")
          events (db/get-events ds)
          html   (hellopagedefualt/page {:title title :events events})]
      (-> (resp/response html)
          (resp/content-type "text/html; charset=UTF-8")))))

(defn hello [{:keys [path-params query-params]}]
  (let [name (:name path-params)
        title (get query-params "title" "")
        html (hellopage/page {:name name})]
    (future
      (producer/send-json! @producer/p "events" name (greetingusecase/greet name)))
    (->
     (resp/response html)
     (resp/content-type "text/html; charset=UTF-8"))))

(defn factorial [{:keys [path-params]}]
  (let [number (:number path-params)
        factorial-value (factorial/factorial (Integer/parseInt number))]
    (-> (resp/response (str factorial-value))
        (resp/content-type "text/plain; charset=UTF-8"))))

(defn weather [{:keys [path-params]}]
  (let [location (:location path-params)]
    (let [weather-data (get-weather location)] 
      (-> (resp/response (str "Weather for " location " is " weather-data)) 
          (resp/content-type "text/plain; charset=UTF-8"))
      )))

