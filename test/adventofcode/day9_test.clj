(ns adventofcode.day9-test
  (:require
    [adventofcode.day9 :refer :all]
    [clojure.test :refer :all]))


(deftest part-1-test
  (is (= 14204 (part-1))))

(deftest part-2-test
  (is (= 6622 (part-2))))