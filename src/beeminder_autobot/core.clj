(ns beeminder-autobot.core
  (:require [beeminder-autobot.fitbit :as fitbit]
            [beeminder-autobot.github :as github]
            [beeminder-autobot.beeminder :as beeminder]
            [beeminder-autobot.time-logic :as time]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.coerce :as c])
  (:gen-class))

(defn log-sleep []
  (let [today-date (time/today)
        timestamp (int (c/to-epoch (t/now)))
        sleep-data (fitbit/main-sleep-data today-date)
        went-to-sleep (:startTime sleep-data)
        penalty (time/penalty today-date went-to-sleep)
        comment (str "Went to sleep at " went-to-sleep)
        request-id (.replaceAll today-date "-" "")
        ]
    (beeminder/log-datapoint (fitbit/settings :beeminder-goal) request-id timestamp penalty comment)))

(defn log-commit [commit]
  (let [goal (:beeminder-goal ((:type commit) github/settings))
        timestamp (c/to-epoch (:time commit))
        comment (str (f/unparse (f/formatter "HH:mm") (:time commit)) " " (:text commit))
        ; Note: using YYYYMMdd here only records one commit per day. This is intended by me.
        request-id (f/unparse (f/formatter "YYYYMMdd") (:time commit))]
    (beeminder/log-datapoint goal request-id timestamp 1 comment)))

(defn log-commits []
  (let [entries (github/push-log-entries)]
    (doall (map log-commit entries))))

(defn -main
  [& args]
  (log-sleep)
  (log-commits))
