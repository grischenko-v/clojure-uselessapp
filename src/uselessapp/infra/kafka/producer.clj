(ns uselessapp.infra.kafka.producer
  (:require [cheshire.core :as json])
  (:import (org.apache.kafka.clients.producer KafkaProducer ProducerRecord Callback)
           (org.apache.kafka.common.serialization StringSerializer)
           (java.util Properties)))

(defn make-producer ^KafkaProducer [{:keys [bootstrap]}]
  (let [p (doto (Properties.)
            (.put "bootstrap.servers" bootstrap)
            (.put "key.serializer" "org.apache.kafka.common.serialization.StringSerializer")
            (.put "value.serializer" "org.apache.kafka.common.serialization.StringSerializer")
            (.put "acks" "all")
            (.put "enable.idempotence" "true"))]
    (KafkaProducer. p)))

(defn send!
  "Отправить сообщение. Возвращает RecordMetadata (блокирующе)."
  [^KafkaProducer producer topic key value]
  (let [record (ProducerRecord. topic key value)]
    (.get
     (.send producer record
            (reify Callback
              (onCompletion [_ metadata exception]
                (when exception
                  (println "Kafka send failed:" (.getMessage exception)))
                (when metadata
                  (println "Sent to"
                           (.topic metadata) "partition" (.partition metadata) "offset" (.offset metadata)))))))))

(defn send-json!
  "payload — Clojure map/vector, отправляем как JSON string"
  [^KafkaProducer producer topic key payload]
  (send! producer topic key (json/generate-string payload)))

(defn close! [^KafkaProducer producer]
  (.flush producer)
  (.close producer))

(defonce p (delay (make-producer {:bootstrap "localhost:9092"})))

