(ns fwpd.core
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

(defn find-suspects
  "Find the names of suspects that exceed the minimum glitter."
  [filename minimum-glitter]
  (map #(:name %) (glitter-filter minimum-glitter (mapify (parse (slurp filename))))))

(defn append-suspect
  "Appends a suspect to the existing list of suspects."
  [suspect suspect-list]
  (conj suspect-list suspect))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (find-suspects filename 3))
