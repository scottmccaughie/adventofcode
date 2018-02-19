(ns adventofcode.day3)

(def direction-to-coordinate-adjustments
  {:up    [0 1]
   :down  [0 -1]
   :left  [-1 0]
   :right [1 0]})

(defn record-coordinate
  [{:keys [current-x current-y current-val target-val] :as acc} dir val-fn]
  (let [[x-adj y-adj] (direction-to-coordinate-adjustments dir)
        new-x (+ current-x x-adj)
        new-y (+ current-y y-adj)
        acc (-> acc
            (assoc :current-x new-x)
            (assoc :current-y new-y))
        new-val (val-fn acc)
        new-acc (-> acc
                    (assoc-in [:spiral [new-x new-y]] new-val)
                    (assoc :current-val new-val))]
    (if (>= new-val target-val)
      (reduced new-acc)
      new-acc)))

(defn record-coordinate-with-incrementing-vals
  []
  (fn [acc dir]
    (record-coordinate acc dir #(inc (:current-val %)))))

(defn sum-neighbours
  [{:keys [current-x current-y spiral] :as acc}]
  (let [neighbour-positions
        (for [x [-1 0 1]
              y [-1 0 1]
              :when (not= 0 x y)]
          [(+ current-x x)
           (+ current-y y)])]
    (reduce + (keep #(get spiral %) neighbour-positions))))

(defn record-coordinate-by-summing-neighbours
  []
  (fn [acc dir]
    (record-coordinate acc dir sum-neighbours)))

(defn initialise-spiral
  [target]
  {:current-x   0
   :current-y   0
   :current-val 1
   :target-val  target
   :spiral      {[0 0] 1}})

(defn generate-spiral-build-steps
  []
  (mapcat (fn [[d1 d2] amt]
         (concat (repeat amt d1)
                 (repeat amt d2)))
       (cycle [[:right :up] [:left :down]])
       (map inc (range))))

(defn part-1
  "manhattan distance of target from 1 (which is stored at 0,0)"
  [target]
  (let [{:keys [current-x current-y]}
        (reduce (record-coordinate-with-incrementing-vals)
                (initialise-spiral target)
                (generate-spiral-build-steps))]
    (reduce + (map #(Math/abs %) [current-x current-y]))))

(defn part-2
  ""
  [target]
  (let [{:keys [current-val]}
        (reduce (record-coordinate-by-summing-neighbours)
                (initialise-spiral target)
                (generate-spiral-build-steps))]
    current-val))












