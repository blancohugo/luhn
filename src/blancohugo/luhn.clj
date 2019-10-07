(ns blancohugo.luhn
  (:require [clojure.string :as str]))

(defn str->int
  "Transforms string into integer"
  [string]
  (Integer. (str string)))

(defn double-digit->single-digit
  "Transforms double digit number into single digit"
  [number]
  (if (>= number 10)
    (- number 9)
    number))

(defn double-every-second-digit
  "Doubles every second digit element"
  [index value]
  (-> value
      (* (inc (mod index 2)))
      double-digit->single-digit))

(defn has-invalid-chars?
  "Checks for any characters other than digits"
  [string]
  (->> string
       (re-find #"[^\d]+")
       empty?
       not))

(defn valid-number?
  "Checks if a filtered number is valid"
  [string]
  (->> string
       (map str->int)
       reverse
       (map-indexed double-every-second-digit)
       (reduce +)
       (#(mod % 10))
       (= 0)))

(defn valid?
  "Validate a string using the Luhn algorithm"
  [string]
  (let [string (str/replace string #"\s" "")]
    (cond
      (<= (count string) 1) false
      (has-invalid-chars? string) false
      :else (valid-number? string))))
