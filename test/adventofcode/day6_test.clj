(ns adventofcode.day6-test
  (:require [clojure.test :refer :all]
            [adventofcode.day6 :refer :all]))

(deftest part-1-test
  (is (= 12841 (part-1))))

(deftest part-2-test
  (is (= 8038 (part-2))))