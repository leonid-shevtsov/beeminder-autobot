(ns beeminder-autobot.time-logic
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            beeminder-autobot.settings))

(defn format-date [time]
  (f/unparse (f/formatter "yyyy-MM-dd") time))

(defn today
  []
  (format-date (t/today-at 0 0)))

(defn penalty
  "Both parameters are strings so that outside app doesn't need to know about time types"
  [current-date reported-time]
  (let [
        expected-time (t/minus (f/parse current-date) (t/minutes 60))
        reported-time (f/parse reported-time)]
    (if (t/before? reported-time expected-time)
      0 ; yay! went to sleep on time
      (t/in-minutes (t/interval expected-time reported-time)))))

(def local-timezone (t/time-zone-for-offset (:time-zone-offset beeminder-autobot.settings/settings)))

(defn in-local-timezone [time]
  (t/to-time-zone time local-timezone))

