(ns uselessapp.usecase.handleconsumeevent 
  (:require
   [cheshire.core :as json]
   [uselessapp.infra.db.events :as dbevents]))


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
      (dbevents/insert-event! ds {:event event :name name}))))