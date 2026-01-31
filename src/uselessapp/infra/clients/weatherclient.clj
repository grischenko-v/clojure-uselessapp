(ns uselessapp.infra.clients.weatherclient
  (:require
   [clj-http.client :as http]
   [uselessapp.config :refer [read-config!]]))

(defn- fetch-weather-from-api [location]
  (let [cfg (read-config!)
        api-key (get-in cfg [:weatherapi :access_key])]
    (let [resp (http/get "http://api.weatherstack.com/current"
                         {:query-params {:access_key api-key
                                         :query location}
                          :as :json})]
      (:body resp))))

(defn get-weather [location]
  (let [data (fetch-weather-from-api location)]
    {:location location
     :data data}))

(comment 
  
   (let [cfg (read-config!)])
  (fetch-weather-from-api "New York")
  )
