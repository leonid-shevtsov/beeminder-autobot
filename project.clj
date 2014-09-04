(defproject beeminder-autobot "2.1.0"
  :description "Beeminding more things in less time"
  :url "https://github.com/leonid-shevtsov/beeminder-autobot"
;  :plugins [[lein-autoreload "0.1.0"]]
  :dependencies [
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.5"]
                 [clj-http "0.9.2"]
                 [clj-oauth "1.5.1"]
                 [mavericklou/clj-oauth2 "0.5.2"]
                 [clj-time "0.8.0"]
                 [slingshot "0.10.3"]
                 [com.github.kyleburton/clj-xpath "1.4.3"]
                 ]
  :main ^:skip-aot beeminder-autobot.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
