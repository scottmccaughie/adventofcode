(ns adventofcode.day8
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(def registers (map #(string/split % #" ") (-> (io/resource "day8-input")
                                              slurp
                                              (string/split #"\n")
                                              )))

#_(def registers (map #(string/split % #" ") (-> "b inc 5 if a > 1\na inc 1 if b < 5\nc dec -10 if a >= 1\nc inc -20 if c == 10"
                                               (string/split #"\n"))))

(def safe-add (fnil + 0))
(def safe-subtract (fnil - 0))

(def safe-op-mapping
  {"inc" safe-add
   "dec" safe-subtract
   "<=" <=
   ">=" >=
   "==" =
   "!=" not=
   ">" >
   "<" <})

; spec to coerce?
(defn process-register
  [acc [target-reg op amt _ test-reg test-op test-amt :as register]]
  (let [amt (read-string amt)
        test-reg-val (get acc test-reg 0)
        test-amt (read-string test-amt)
        op (safe-op-mapping op)
        test-op (safe-op-mapping test-op)]
    (cond-> acc
            (test-op test-reg-val test-amt) (update target-reg op amt))))

(defn part-1
  []
  (->>
    registers
    (reduce process-register {})
    vals
    (reduce max)))

(defn part-2
  []
  (->>
    registers
    (reductions process-register {})
    (remove empty?)
    (mapcat vals)
    (reduce max)))