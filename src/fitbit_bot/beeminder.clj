(ns fitbit-bot.beeminder
  (:use [slingshot.slingshot :only [try+]])
  (:require [clj-http.client :as http]
            fitbit-bot.settings
            )
  )

(def settings (:beeminder fitbit-bot.settings/settings))

(def base-url "https://www.beeminder.com/api/v1")

(defn datapoints-url [username goal]
  (str base-url "/users/" username "/goals/" goal "/datapoints.json")
  )

(defn make-request-id [date]
  (.replaceAll date "-" "")
  )

(defn log-datapoint [date value comment]
  (let [
        datapoints-url (datapoints-url (:username settings) (:goal settings))
        request-id (make-request-id date)
        params {
                :value value
                :comment comment
                :requestid request-id
                :auth_token (:auth-token settings)
                }
        ]
    (try+
      (http/post datapoints-url {:form-params params, :as :json})
      (catch [:status 422] _
        nil; we're cool, it's just a duplicate submission
        )
      )
    )
  )
