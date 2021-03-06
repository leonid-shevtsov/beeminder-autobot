(ns beeminder-autobot.beeminder
  (:use [slingshot.slingshot :only [try+]])
  (:require [clj-http.client :as http]
            beeminder-autobot.settings
            )
  )

(def settings (:beeminder beeminder-autobot.settings/settings))

(def base-url "https://www.beeminder.com/api/v1")

(defn datapoints-url [username goal]
  (str base-url "/users/" username "/goals/" goal "/datapoints.json")
  )

(defn log-datapoint [goal request-id timestamp value comment]
  (let [
        datapoints-url (datapoints-url (:username settings) goal)
        params {
                :value value
                :comment comment
                :requestid request-id
                :timestamp timestamp
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
