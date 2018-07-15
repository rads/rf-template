(defproject {{raw-name}} "0.1.0-SNAPSHOT"
  :plugins [[lein-figwheel "0.5.14"]
            [lein-doo "0.1.9"]]
  :dependencies [[bidi "2.1.3"]
                 [com.taoensso/timbre "4.10.0"]
                 [com.google.guava/guava "25.0-jre"]
                 [clj-http "3.9.0"]
                 [cljs-ajax "0.7.3"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [district0x/re-frame-interval-fx "1.0.2"]
                 [duct/server.http.jetty "0.2.0"]
                 [integrant "0.6.3"]
                 [fundingcircle/joist "0.1.3"]
                 [ring/ring-core "1.6.3"]
                 [reagent "0.8.0"]
                 [re-frame "0.10.5"]
                 [sparkledriver "0.2.2"]]
  :profiles {:provided {:dependencies [[org.clojure/clojure "1.9.0"]
                                       [org.clojure/clojurescript "1.10.238"]]}
             :dev {:source-paths ["src" "dev"]
                   :dependencies [[figwheel-sidecar "0.5.14"]
                                  [org.clojure/tools.nrepl "0.2.10"]
                                  [com.cemerick/piggieback "0.2.2"]
                                  [org.clojure/test.check "0.9.0"]
                                  [javax.servlet/servlet-api "2.5"]
                                  [ring/ring-devel "1.6.3"]]}}
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :doo {:build "test"}
  :figwheel {:server-logfile "logs/figwheel-logfile.log"
             :validate-config :ignore-unknown-keys}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src" "dev"]
                        :figwheel {:on-jsload {{namespace}}.browser/on-jsload}
                        :compiler {:main {{namespace}}.browser
                                   :optimizations :none
                                   :output-to "resources/public/js/main.js"
                                   :output-dir "resources/public/js/out"
                                   :asset-path "js/out"
                                   :source-map-timestamp true
                                   :aot-cache true}}
                       {:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:main {{namespace}}.browser.test-runner
                                   :optimizations :none
                                   :output-to "target/test.js"
                                   :output-dir "target/test"
                                   :aot-cache true}}]})
