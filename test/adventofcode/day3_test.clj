(ns adventofcode.day3-test
  (:require [clojure.test :refer :all]
            [adventofcode.day3 :refer :all]))

(deftest part-1-test
  (is (= 480 (part-1 347991))))

(deftest part-2-test
  (is (= 349975 (part-2 347991))))
