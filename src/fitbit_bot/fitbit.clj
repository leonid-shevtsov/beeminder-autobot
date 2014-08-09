(ns fitbit-bot.fitbit
  (:require [oauth.client :as oauth]
            [clj-http.client :as http]
            fitbit-bot.settings
            )
  )

(def oauth-settings (:fitbit fitbit-bot.settings/settings))

(def api-base "https://api.fitbit.com")

(def consumer (oauth/make-consumer (:app-key oauth-settings)
                                   (:app-secret oauth-settings)
                                   "https://api.fitbit.com/oauth/request_token"
                                   "https://api.fitbit.com/oauth/access_token"
                                   "https://www.fitbit.com/oauth/authorize"
                                   :hmac-sha1))

(defn auth-header [method url params]
  (let [
        credentials (oauth/credentials consumer
                                       (:user-key oauth-settings)
                                       (:user-secret oauth-settings)
                                       method
                                       url
                                       params)
        ]
    (oauth/authorization-header credentials)
    )
  )

(defn main-sleep-data [date]
  (let [
        url (str api-base "/1/user/-/sleep/date/" date ".json")
        auth-headers {:authorization (auth-header :GET url {})}
        sleep-items (-> (http/get url {:headers auth-headers, :as :json}) :body :sleep)
        ]
    (first (filter :isMainSleep sleep-items))
    )
  )


(comment "To obtain tokens"
  (use 'fitbit-bot.fitbit)
  (require '[oauth.client :as oauth])
  ;; Fetch a request token that a OAuth User may authorize
  (def request-token (oauth/request-token consumer))

  ;; Send the User to this URI for authorization, they will be able
  ;; to choose the level of access to grant the application and will
  ;; then be redirected to the callback URI provided with the
  ;; request-token.
  (oauth/user-approval-uri consumer (:oauth_token request-token))

  (def verifier "string returned from fitbit")
  ;; Assuming the User has approved the request token, trade it for an access token.
  ;; The access token will then be used when accessing protected resources for the User.
  (def access-token-response (oauth/access-token consumer request-token verifier))

  (println (:oauth_token access-token-response) (:oauth_token_secret access-token-response))
  )
