(ns hello-core-async.simple)

;;; Collections
;; Customer records:  name, age, rating
;; Filter by age  (under age)
;; Map (rating)
;; Reduce + (sum of all under age ratings)

(defrecord Customer [name age rating])

(def customers
  [(Customer. "Jonas" 17 5)
   (Customer. "Laurynas" 16 3)
   (Customer. "Petras" 35 4)])

(def underage-rating-sum
  (->> customers
       (filter #(< (:age %) 18))
       (map :rating)
       (reduce +)))
