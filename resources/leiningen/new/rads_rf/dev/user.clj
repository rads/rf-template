(ns user
  (:require [figwheel-sidecar.repl-api :as ra]
            [integrant.core :as ig]
            [{{namespace}}.server :as server])
  (:import (java.io StringWriter)))

(defn running? []
  (binding [*out* (StringWriter.)]
    (ra/figwheel-running?)))

(defn cljs []
  (when-not (running?)
    (ra/start-figwheel! "dev"))
  (ra/cljs-repl "dev"))

(defn start []
  (server/start))

(defn stop []
  (server/stop))

(defn restart []
  (server/stop)
  (server/start))

(defn dev []
  (start)
  (cljs))
