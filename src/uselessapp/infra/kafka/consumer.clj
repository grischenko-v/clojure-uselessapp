(ns uselessapp.infra.kafka.consumer
    (:require
     [cheshire.core :as json]
     [uselessapp.infra.db.events :as db])
  
  (:import
   (java.time Duration)
   (java.util Collections Properties)
   (org.apache.kafka.clients.consumer KafkaConsumer)))

(defn insert-event-handler
  [ds]
  (fn [{:keys [value] :as msg}]
    (println "Consumed msg:" (select-keys msg [:topic :partition :offset :key]) "value=" value)
    (let [payload (try
                    (json/parse-string (str value) true) ; keywords
                    (catch Exception _
                      {:event (str value)}))
          ;; поддержим разные формы payload
          event   (or (:event payload) (:message payload) (:value payload))
          name    (:name payload)]
      (db/insert-event! ds {:event event :name name}))))


(defn make-consumer
  ^KafkaConsumer
  [{:keys [bootstrap group-id]}]
  (let [props (doto (Properties.)
                (.put "bootstrap.servers" bootstrap)
                (.put "group.id" group-id)
                (.put "key.deserializer"
                      "org.apache.kafka.common.serialization.StringDeserializer")
                (.put "value.deserializer"
                      "org.apache.kafka.common.serialization.StringDeserializer")
                ;; контролируем commit сами
                (.put "enable.auto.commit" "false")
                ;; если нет offset — начать сначала
                (.put "auto.offset.reset" "earliest"))]
    (KafkaConsumer. props)))

(defn run-consumer!
  "handler — функция (fn [{:keys [topic partition offset key value]}] ...)"
  [^KafkaConsumer consumer topic handler stop?]
  (.subscribe consumer (Collections/singletonList topic))
  (try
    (while (not @stop?)
      (let [records (.poll consumer (Duration/ofMillis 500))]
        (when (pos? (.count records))
          (doseq [r records]
            (handler {:topic (.topic r)
                      :partition (.partition r)
                      :offset (.offset r)
                      :key (.key r)
                      :value (.value r)}))
          ;; commit после успешной обработки
          (.commitSync consumer))))
    (finally
      (.close consumer))))

(defn start-consumer!
  [{:keys [bootstrap topic group-id]} handler]

  (let [stop? (atom false)
        c (make-consumer {:bootstrap bootstrap
                          :group-id (or group-id "myapp")})
        t (Thread. #(run-consumer! c topic handler stop?))]

    (.start t)

    {:thread t
     :stop? stop?}))

(defn stop-consumer! [{:keys [stop? thread]}]
  (when stop?
    (reset! stop? true))
  (when thread
    (.join thread 2000))
  :stopped)

