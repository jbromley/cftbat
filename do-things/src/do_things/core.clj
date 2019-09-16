(ns do-things.core
  (:gen-class))

(defn add-100
  "Add 100 to the argument."
  [n]
  (+ n 100))

(defn dec-maker
  "Create a custom decrementor function."
  [dec-by]
  #(- % dec-by))

(defn map-set
  "Returns a set consisting of the result of applying f the set of first items
  of each coll, followed by applying f to the set of second items in each coll,
  until any one of the colls is exhausted.  Any remaining items in other colls
  are ignored. Function f should accept number-of-colls arguments."
  [f coll]
  (hash-set (map f (hash-set) coll)))

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(def asym-alien-body-parts [{:name "head" :size 3}
                            {:name "1-eye" :size 1}
                            {:name "1-ear" :size 1}
                            {:name "mouth" :size 1}
                            {:name "nose" :size 1}
                            {:name "neck" :size 2}
                            {:name "1-side" :size 2}
                            {:name "1-tentacle" :size 3}
                            {:name "1-foot" :size 1}])

(defn matching-5-parts
  "Generate parts for a being with five-fold radial symmetry. Returns a list of parts."
  [part]
  (map (fn [part-no] {:name (clojure.string/replace (:name part) #"1-" (str part-no "-"))
                      :size (:size part)})
       (range 2 6)))

(defn make-part-matcher
  "Create a function that will generate body parts with the given radial symmetry."
  [num-parts]
  (fn
    [part]
    (map (fn [part-no] {:name (clojure.string/replace (:name part) #"1-" (str part-no "-"))
                        :size (:size part)})
         (range 2 (inc num-parts)))))

(defn symmetrize-body-parts
  "Takes a seq of maps that have a :name and :size and creates the symmetric counter parts to the parts in the seq."
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)])))
          []
          asym-body-parts))

(defn -main
  "Run the exercises from Chapter 2: Do Things"
  [& args]
  (println (str "This is " "two strings concatenated with str."))
  (println (str "(vector 1 2 3) = " (vector 1 2 3)))
  (println (str "(list 1 2 3) = " (list 1 2 3)))
  (println (str "(hash-map :a 1 :b 2 :c 3) = " (hash-map :a 1 :b 2 :c 3)))
  (println (str "(hash-set 1 1 2 3 3) = " (hash-set 1 1 2 3 3)))
  (println "(map add-100 (range 10)) = " (map add-100 (range 10)))
  (def subtract-13 (dec-maker 13))
  (println "subtract-13 =" (str subtract-13))
  (println "(subtract-13 27) =" (subtract-13 27)))
