(ns uselessapp.usecase.greeting)

(defn greet [name]
  {
   :name name 
   :event (str "Greeted " (apply str (reverse name)))
   })

