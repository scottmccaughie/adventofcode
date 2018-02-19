(ns adventofcode.day1-test
  (:require [clojure.test :refer :all]
            [adventofcode.day1 :refer :all]))

(deftest test-captcha-1
  (is (= 1047 (solve-captcha-1 captcha-input))))

(deftest test-captcha-2
  (is (= 982 (solve-captcha-2 captcha-input))))
