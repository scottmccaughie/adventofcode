(ns adventofcode.day8
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(def registers (-> (io/resource "day8-input")
                   slurp
                   (string/split #"\n")
                   #_(string/split #" ")))

(def safe-inc (fnil inc 0))
(def safe-dec (fnil dec 0))

(def safe-op-mapping
  {"inc" safe-inc
   "dec" safe-dec})

(defn part-1
  []
  (reduce
    (fn [acc [target-reg op amt _ test-reg test-op test-amt]]
      (let [test-reg-val (get acc test-reg 0)
            op (safe-op-mapping op)
            test-op (-> test-op read-string eval)]          ; we trust the ops :D
        (cond-> acc
                (test-op test-reg-val test-amt) (update acc target-reg op))))
    {}
    registers))