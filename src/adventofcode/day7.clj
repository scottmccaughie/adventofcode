(ns adventofcode.day7
  (:require [clojure.string :as string]
            [clojure.set :as set]
            [clojure.java.io :as io]))

(def programs (->
                (io/resource "day7-input")
                (slurp)
                (string/split #"\n")))

(defn parent?
  [program-str]
  (string/includes? program-str "->"))

(defn extract-node
  [program-str]
  (-> program-str
      (string/split #" ")
      first))

(defn extract-weight
  [program-str]
  (-> program-str
      (string/split #" ")
      second
      (string/replace #"\(" "")
      (string/replace #"\)" "")
      (Integer/parseInt)))

(defn extract-children
  [program-str]
  (some-> program-str
      (string/split #" -> ")
      second
      (string/split #", ")))

(defn part-1
  []
  (let [parent-strs (filter parent? programs)
        parents (into #{} (map extract-node parent-strs))
        children (into #{} (mapcat extract-children parent-strs))]
    (first (set/difference parents children))))

(defn construct-tree
  [programs]
  (reduce (fn [tree program-str]
            (let [node (extract-node program-str)
                  weight (extract-weight program-str)
                  children (extract-children program-str)]
              (merge tree {node {:weight weight
                                 :children children}})))
          {}
          programs))

(defn score
  ([tree] (partial score tree))
  ([tree root]
    (let [{:keys [weight children]} (get tree root)
          child-weights (map (score tree) children)]
      (when (and (seq child-weights) (apply not= child-weights))
        (throw (ex-info (str "Unbalanced! " children (seq child-weights))
                        (zipmap children child-weights))))
      (reduce + weight child-weights))))

(defn balanced-unbalanced
  ; TODO balanced/unbalanced is messy
  [weights]
  (let [freqs (frequencies weights)
        balanced (ffirst (filter #(not= 1 (val %)) freqs))
        unbalanced (ffirst (filter #(= 1 (val %)) freqs))]
    [balanced unbalanced]))

(defn handle-unbalanced-exception
  [tree e]
  (let [data (ex-data e)
        [balanced unbalanced] (balanced-unbalanced (vals data))
        unbalanced-node-idx (.indexOf (vals data) unbalanced)
        {:keys [weight]} (tree (nth (keys data) unbalanced-node-idx))] ; unbalanced node
    (+ (- balanced unbalanced)
       weight)))

(defn part-2
  []
  (let [root (part-1)
        tree (construct-tree programs)
        {:keys [children]} (get tree root)]
    (try
      (doall (map (score tree) children))
      (catch Exception e
        (handle-unbalanced-exception tree e)))))