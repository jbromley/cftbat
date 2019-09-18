(ns fwpd.core
  (:require clojure.pprint)
  (:gen-class))

(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV file into rows of columns."
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}."
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(defn list-suspects
  "Return a list of records of suspects."
  [filename minimum-glitter]
  (glitter-filter minimum-glitter (mapify (parse (slurp filename)))))

(defn find-suspects
  "Find the names of suspects that exceed the minimum glitter."
  [filename minimum-glitter]
  (map #(:name %) (glitter-filter minimum-glitter (mapify (parse (slurp filename))))))

(defn append-suspect
  "Appends a suspect to the existing list of suspects."
  [suspect suspect-list]
  (conj suspect-list suspect))

(def validators {:name string?
                 :glitter-index (fn [index] (and (number? index) (>= index 0) (<= index 10)))})

(defn not-nil?
  "Determine if a value is not nil."
  [value]
  ((complement nil?) value))

(defn validate
  "Ensure a record has the necessary :name and :glitter-index keys. The :name
  value must be a string. The :glitter-index value must be a number between 0
  and 10 inclusive."
  [validators record]
  (reduce (fn [is-valid vamp-key]
            (and is-valid (not-nil? (get record vamp-key)) ((get validators vamp-key) (get record vamp-key))))
          true
          vamp-keys))

(defn to-csv
  "Convert a list of suspects to a comma-delimited list."
  [suspects]
  (map (fn [r]
         (str (clojure.string/join "," (list (:name r) (:glitter-index r))) "\n"))
       suspects))

(defn -main
  "Test the chapter 4 exercises."
  [& args]
  (def suspects (list-suspects filename 3))
  (println (str "The suspects are:"))
  (clojure.pprint/pprint suspects)
  (println "Append a suspect to the list.")
  (def suspects-2 (append-suspect {:name "James Smith" :glitter-index 8} suspects))
  (clojure.pprint/pprint suspects-2)
  (println "Validate some records:")
  (println (str "(validate validators {}) = " (validate validators {})))
  (println (str "(validate validators {:glitter-index 5}) = "
                (validate validators {:glitter-index 5})))
  (println (str "(validate validators {:name \"James Smith\"}) = "
                (validate validators {:name "James Smith"})))
  (println (str "(validate validators {:name 13 :glitter-index 5}) = "
                (validate validators {:name 13 :glitter-index 5})))
  (println (str "(validate validators {:name \"James Smith\" :glitter-index \"x\"}) = "
                (validate validators {:name "James Smith" :glitter-index "x"})))
  (println (str "(validate validators {:name \"James Smith\" :glitter-index 11}) = "
                (validate validators {:name "James Smith" :glitter-index 11})))
  (println (str "(validate validators {:name \"James Smith\" :glitter-index 9}) = "
                (validate validators {:name "James Smith" :glitter-index 9})))
  (println (str "(to-csv suspects-2 = " (list (to-csv suspects-2)))))
