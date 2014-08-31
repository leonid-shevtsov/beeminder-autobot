(ns beeminder-autobot.github
  (:use clojure.java.io)
  (use [clj-xpath.core :only [$x $x:tag $x:text $x:attrs $x:attrs* $x:node xml->doc]])
  (:require [clj-http.client :as http]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [beeminder-autobot.time-logic :as time-logic]
            beeminder-autobot.settings))

(def settings (:github beeminder-autobot.settings/settings))

(defn parse-log-entry [[text time-text]]
  (let [matcher (re-matcher #"^.+ pushed to (.+) at (.+)/(.+)$" text)
        local-time (time-logic/in-local-timezone (f/parse time-text))
        groups (when (re-find matcher) (re-groups matcher))
        [branch owner repo] (when groups (rest groups))
        ]
    {:operation (if groups :push :unknown)
     :text text
     :time local-time
     :branch branch
     :owner owner
     :type (if (contains? (-> settings :work :organizations set) owner) :work :personal)
     :repo repo
     }))

(defn push-log-entries []
  (let [xml (xml->doc (:body (http/get (:feed-url settings))))
        raw-log-entries (map #(vector
                            ($x:text "title" %)
                            ($x:text "published" %))
                         ($x "//entry" xml))
        log-entries (map parse-log-entry raw-log-entries)
        ]
    (filter #(= :push (:operation %)) log-entries)))

; to get url:
;
; curl -u leonid-shevtsov 'https://api.github.com/feeds'
;
; look for "current_user_actor_url"
