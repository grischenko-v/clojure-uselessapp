(ns uselessapp.usecase.factorial)
(defn factorial [n]
  (reduce *' 1 (range 1 (inc n))))