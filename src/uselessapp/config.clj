(ns uselessapp.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [malli.core :as m]
            [malli.error :as me]))


(def ConfigSchema
  [:map
   [:http [:map [:port [:int {:min 1 :max 65535}]]]]
   [:weather_access_key [:map [:string ]]]
   [:db   [:map 
           [:dbtype string?]
           [:host string?]
           [:dbname string?]
           [:user string?]
           [:password string?]]]
   [:kafka [:map [:bootstrap string?]
            [:topic string?]]]
   ])

(defn validate! [cfg]
  (when-not (m/validate ConfigSchema cfg)
    (throw (ex-info "Invalid config"
                    {:errors (me/humanize (m/explain ConfigSchema cfg))})))
  cfg)

(defn load-config [] 
  (aero/read-config (io/resource "config.edn")))

(defn read-config! []
  (-> (load-config)
      (validate!)))

(System/getenv "DB_URL")

(read-config!)
