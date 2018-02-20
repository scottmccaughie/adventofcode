(ns adventofcode.day9
  (:require [clojure.java.io :as io]))

(def stream (slurp (io/resource "day9-input")))

(defn remove-garbage
  [rf]
  (let [state (atom {})]
    (fn
      ([] (rf))
      ([result] (rf result))
      ([result input]
       (let [{:keys [ignore-next garbage]} @state]
         (cond

           ignore-next (do (swap! state dissoc :ignore-next)
                           result)

           (= input \!) (do (swap! state assoc :ignore-next true)
                            result)

           garbage (do (when (= input \>)
                         (swap! state dissoc :garbage))
                       result)

           (= input \<) (do (swap! state assoc :garbage true)
                            result)

           :else (rf result input)))))))

(defn score-groups
  [rf]
  (let [current-depth (atom 0)]
    (fn
      ([] (rf))
      ([result] (rf result))
      ([result input]
       (cond
         (= input \{) (do (swap! current-depth inc)
                          result)

         (= input \}) (let [score @current-depth]
                        (swap! current-depth dec)
                        (rf result score))

         :else result)))))

; scope to merge remove-garbage and count-garbage
(defn count-garbage
  [rf]
  (let [state (atom {})]
    (fn
      ([] (rf))
      ([result] (let [result (rf result (:garbage-chars @state))]
                  (rf result)))
      ([result input]
       (let [{:keys [ignore-next garbage]} @state]
         (cond

           ignore-next (do
                         (swap! state dissoc :ignore-next)
                         result)

           (= input \!) (do (swap! state assoc :ignore-next true)
                            result)

           garbage (do (if (= input \>)
                         (swap! state dissoc :garbage)
                         (swap! state update :garbage-chars (fnil inc 0)))
                       result)

           (= input \<) (do (swap! state assoc :garbage true)
                            result)

           :else result))))))

(defn part-1
  []
  (transduce
    (comp
      remove-garbage
      score-groups)
    +
    stream))

(defn part-2
  []
  (first (sequence
           count-garbage
           stream)))