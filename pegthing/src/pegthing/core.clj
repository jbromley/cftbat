(ns pegthing.core
  ; (require [clojure.set :as set])
  (:gen-class))

(declare successful-move prompt-move game-over query-rows)


;;; Functions for defining mathematical model of the board.

(defn tri*
  "Generates a lazy sequence of triangular numbers."
  ([] (tri* 0 1))
  ([sum n]
   (let [new-sum (+ sum n)]
     (cons new-sum (lazy-seq (tri* new-sum (inc n)))))))

(def tri (tri*))

(defn triangular?
  "Is the number triangular?"
  [n]
  (= n (last (take-while #(>= n %) tri))))

(defn row-tri
  "The triangular number at the end of row n."
  [n]
  (last (take n tri)))

(defn row-num
  "Returns the row number the position belongs to."
  [pos]
  (inc (count (take-while #(> pos %) tri))))


;;; Functions for building up a board.

(defn connect
  "Form a mutual connection between two positions."
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbor))
            board
            [[pos destination] [destination pos]])
    board))

(defn connect-right
  [board max-pos pos]
  (let [neighbor (inc pos)
        destination (inc neighbor)]
    (if-not (or (triangular? neighbor) (triangular? pos))
      (connect board max-pos pos neighbor destination)
      board)))

(defn connect-down-left
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ row pos)
        destination (+ 1 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(defn connect-down-right
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ 1 row pos)
        destination (+ 1 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(defn add-pos
  "Pegs the position and performs connections."
  [board max-pos pos]
  (let [pegged-board (assoc-in board [pos :pegged] true)]
    (reduce (fn [new-board connection-creation-fn]
              (connection-creation-fn new-board max-pos pos))
            pegged-board
            [connect-right connect-down-left connect-down-right])))

(defn new-board
  "Creates a new board with the given number of rows."
  [rows]
  (let [initial-board {:rows rows}
        max-pos (row-tri rows)]
    (reduce (fn [board pos]
              (add-pos board max-pos pos))
            initial-board
            (range 1 (inc max-pos)))))


;;; Functions for making moves.

(defn pegged?
  "Does the board have a peg at the given position?"
  [board pos]
  (get-in board [pos :pegged]))

(defn remove-peg
  "Remove the peg at the given position from the board."
  [board pos]
  (assoc-in board [pos :pegged] false))

(defn place-peg
  "Place a peg in the board at the given position."
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn move-peg
  "Take a peg out of pos1 and place it in pos2."
  [board pos1 pos2]
  (place-peg (remove-peg board pos1) pos2))

(defn valid-moves
  "Return a map of all valid moves for pos, where the key is the destination and
  the value is the jumped position."
  [board pos]
  (into {}
        (filter (fn [[destination jumped]]
                  (and (not (pegged? board destination))
                       (pegged? board jumped)))
                (get-in board [pos :connections]))))

(defn valid-move?
  "Return the jumped position if the move from pos1 to pos2 is valid, nil
  otherwise."
  [board pos1 pos2]
  (get (valid-moves board pos1) pos2))

(defn can-move?
  "Do any of the pegged positions have valid moves?"
  [board]
  (some (comp not-empty (partial valid-moves board))
        (map first (filter #(get (second %) :pegged) board))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
