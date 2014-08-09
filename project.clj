(defproject fitbit-bot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ; :plugins [[lein-autoreload "0.1.0"]]
  :dependencies [
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.5"]
                 [clj-http "0.9.2"]
                 [clj-oauth "1.5.1"]
                 [clj-time "0.8.0"]
                 [slingshot "0.10.3"]
                 ]
  :main ^:skip-aot fitbit-bot.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
