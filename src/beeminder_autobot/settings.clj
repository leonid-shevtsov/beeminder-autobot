(ns beeminder-autobot.settings
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            )
  )

(def settings (-> "settings.json" io/reader (#(json/read % :key-fn keyword))))
