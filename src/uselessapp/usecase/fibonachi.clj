(ns uselessapp.usecase.fibonachi)

(defn fibonachi [n]
  (loop [a 0 b 1 count n]
    (if (zero? count)
      a
      (recur b (+ a b) (dec count)))))
