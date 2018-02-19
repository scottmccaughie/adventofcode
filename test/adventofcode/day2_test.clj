(ns adventofcode.day2-test
  (:require
    [adventofcode.day2 :refer :all]
    [clojure.test :refer :all]))

(def sample-1 "5\t1\t9\t5\n7\t5\t3\n2\t4\t6\t8")

(deftest sample-1-test
  (is (= 18 (score-spreadsheet-1 sample-1))))

(deftest sample-2-test
  (is (= 44887 (score-spreadsheet-1 raw-spreadsheet))))

(deftest spreadsheet-scoring-method-2-test
  (is (= 242 (score-spreadsheet-2 raw-spreadsheet))))
