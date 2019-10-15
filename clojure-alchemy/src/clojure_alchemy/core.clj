(ns clojure-alchemy.core
  (:gen-class))

;;; Exercise 1
;; Use the list function, quoting, and read-string to create a list
;; that, when evaluated, prints your first name and your favorite sci-fi movie.

(defn favorite-movie
  [first-name movie]
  (read-string  (str "(println (str \"" first-name "\" \" \" \"" movie "\"))")))

;;; Exercise 2:
;; Create an infix function that takes a list like (1 + 3 * 4 - 5) and 
;; transforms it into the lists that Clojure needs in order to correctly
;; evaluate the expression using operator precedence rules.

(declare infix->prefix)

(defn infix
  [arith-list]
  (->> arith-list
       (map #(if (list? %) (infix %) %))
       (infix->prefix '(* /))
       (infix->prefix '(+ -))
       (first)))

(defn infix->prefix
  "Given a list of operators and a list of arithmetic operations, change from infix to prefix notation."
  [ops arith-list]
  (if (< (count arith-list) 3)
    arith-list
    (if (contains? (set ops) (second arith-list))
      (infix->prefix ops (cons (list (second arith-list) (first arith-list) (nth arith-list 2)) (drop 3 arith-list)))
      (concat (take 2 arith-list) (infix->prefix ops (drop 2 arith-list))))))

(defn -main
  [& args]
  (eval (favorite-movie "Jay" "Matrix"))
  (println (infix '(1 + 1)))
  (println (infix '(1 + 3 * 4 - 5)))
  (println (infix '((1 + 3) * 4 - 5))))
