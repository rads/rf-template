(ns leiningen.new.rads-rf
  (:require [leiningen.new.templates :refer [renderer year date project-name
                                             ->files sanitize-ns name-to-path
                                             multi-segment]]
            [leiningen.core.main :as main]))

(defn rads-rf [name]
  (let [render (renderer "rads_rf")
        main-ns (multi-segment (sanitize-ns name))
        data {:raw-name name
              :name (project-name name)
              :namespace main-ns
              :nested-dirs (name-to-path main-ns)
              :year (year)
              :date (date)}
        copy (fn
               ([a] [a (render a data)])
               ([a b] [a (render b data)]))]
    (main/info "Generating fresh 'lein new' rads-rf project.")
    (->files data
             (copy "dev/user.clj")
             (copy "project.clj")
             (copy "package.json")
             (copy "README.md")
             (copy ".gitignore")
             (copy "resources/public/index.html")
             (copy "resources/public/index.css")
             (copy "src/{{nested-dirs}}/server.clj" "src/rads_rf/server.clj")
             (copy "src/{{nested-dirs}}/server/api.clj" "src/rads_rf/server/api.clj")
             (copy "src/{{nested-dirs}}/browser.cljs" "src/rads_rf/browser.cljs")
             (copy "src/{{nested-dirs}}/browser/rpc.cljs" "src/rads_rf/browser/rpc.cljs")
             (copy "src/{{nested-dirs}}/browser/components.cljs" "src/rads_rf/browser/components.cljs")
             (copy "test/{{nested-dirs}}/browser_test.cljs" "test/rads_rf/browser_test.cljs")
             (copy "test/{{nested-dirs}}/browser/test_runner.cljs" "test/rads_rf/browser/test_runner.cljs")
             (copy "test/{{nested-dirs}}/browser/test_utils.cljs" "test/rads_rf/browser/test_utils.cljs"))))
