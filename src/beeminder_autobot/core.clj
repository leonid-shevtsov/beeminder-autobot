(ns beeminder-autobot.core
  (:require [beeminder-autobot.fitbit :as fitbit]
            [beeminder-autobot.beeminder :as beeminder]
            [beeminder-autobot.time-logic :as time]
            )
  (:gen-class))

(defn -main
  [& args]
  (let [
        today-date (time/today)
        sleep-data (fitbit/main-sleep-data today-date)
        went-to-sleep (:startTime sleep-data)
        penalty (time/penalty today-date went-to-sleep)
        comment (str "Went to sleep at " went-to-sleep)
        ]
    (beeminder/log-datapoint today-date penalty comment)
    )
  )
