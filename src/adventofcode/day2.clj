(ns adventofcode.day2
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def raw-spreadsheet
  (slurp (io/resource "day2-spreadsheet")))

(defn format-raw-spreadsheet
  "given a 'spreadsheet' in string format where rows are
   new-line separated and numeric values are tab separated, yield
   a vector of vectors of ints"
  [raw-spreadsheet]
  (let [rows (string/split raw-spreadsheet #"\n")
        values-vector (mapv #(string/split % #"\t") rows)]
    (mapv #(map (fn [x] (Integer/parseInt x)) %) values-vector)))

(defn row-stats
  "yields a map containing the max and min values
  contained within the row provided"
  [row]
  (loop [row row
         mx Integer/MIN_VALUE
         mn Integer/MAX_VALUE]
    (if (seq row)
      (let [[candidate & rest-of-row] row]
        (recur rest-of-row (max mx candidate) (min mn candidate)))
      {:mx mx
       :mn mn})))

(defn score-by-max-and-min
  [row]
  (let [{:keys [mx mn]} (row-stats row)]
    (- mx mn)))

(defn factor?
  [numerator denominators]
  (loop [denominators denominators]
    (when (seq denominators)
      (let [result (/ numerator (first denominators))]
        (if (integer? result)
          result
          (recur (rest denominators)))))))

(defn score-by-divisible-numbers
  [row]
  (let [sorted-row (sort > row)]
    (loop [candidate-numbers sorted-row]
      (if (seq candidate-numbers)
        (let [[numerator & denominators] candidate-numbers
              factor (factor? numerator denominators)]
          (if factor
            factor
            (recur denominators)))))))

(defn score-spreadsheet
  [score-fn]
  (let [rows (format-raw-spreadsheet raw-spreadsheet)]
    (reduce + (map score-fn rows))))

(defn score-spreadsheet-1
  []
  (score-spreadsheet score-by-max-and-min))

(defn score-spreadsheet-2
  []
  (score-spreadsheet score-by-divisible-numbers))