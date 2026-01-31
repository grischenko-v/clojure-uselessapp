(ns uselessapp.usecase.transformweatherdata)

(defn transform [location data]
  (let [current (:current data)
        temperature (:temperature current)
        weather-descriptions (:weather_descriptions current)]
    (str "In " location ", the temperature is " temperature "°C with " (clojure.string/join ", " weather-descriptions) ".")))