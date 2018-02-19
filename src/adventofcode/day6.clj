(ns adventofcode.day6)

(def memory-banks [4	10	4	1	8	4	9	14	5	1	14	15	0	15	3	5])

(defn realloc-analysis
  [banks-input]
  (loop [banks banks-input
         max-idx Integer/MIN_VALUE
         max-size Integer/MIN_VALUE
         curr-idx 0]
    (if (seq banks)
      (let [[candidate & remaining] banks]
        (if (< max-size candidate)
          (recur remaining curr-idx candidate (inc curr-idx))
          (recur remaining max-idx max-size (inc curr-idx))))


      [(assoc banks-input max-idx 0) max-idx max-size])))

(defn reallocate
  [[banks realloc-idx realloc-size]]
  (loop [banks banks
         bank-idxs (->>
                     (count banks)
                     range
                     cycle
                     (drop-while #(not= % realloc-idx))
                     (drop 1))
         realloc-size realloc-size]
    (if (pos? realloc-size)
      (recur
        (update banks (first bank-idxs) inc)
        (drop 1 bank-idxs)
        (dec realloc-size))
      banks)))



(defn part-1
  []
  (loop [seen #{}
         banks memory-banks]
    (let [reallocated (-> banks
                          realloc-analysis
                          reallocate)]
      (if (seen reallocated)
        (inc (count seen))
        (recur (conj seen reallocated) reallocated)))))

(defn part-2
  []
  (loop [seen (sorted-set-by (fn [x y] (if (= x y) 0 1))) ; sorted set by insertion
         banks memory-banks]
    (let [reallocated (-> banks
                          realloc-analysis
                          reallocate)]
      (if (seen reallocated)
        (- (count seen) (.indexOf (seq seen) reallocated))
        (recur (conj seen reallocated) reallocated)))))