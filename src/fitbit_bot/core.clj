(ns fitbit-bot.core
  (:require [fitbit-bot.fitbit :as fitbit]
            [fitbit-bot.beeminder :as beeminder]
            [fitbit-bot.time-logic :as time]
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
