(ns adventofcode.day8-test
  (:require
    [adventofcode.day8 :refer :all]
    [clojure.test :refer :all]))

(deftest part-1-test
  (is (= 5966 (part-1))))

(deftest part-2-test
  (is (= 6347 (part-2))))