(ns {{namespace}}.server
  (:require [bidi.ring :as bidi]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.pprint :as pprint]
            [clojure.string :as string]
            [duct.server.http.jetty]
            [integrant.core :as ig]
            [{{namespace}}.server.api :as api]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.resource :refer [wrap-resource]]))

(defn- edn-content-type?
  "Check the headers of a request or response for a Content-Type starting with
  application/transit"
  [r]
  (when-let [content-type (or (get-in r [:headers "Content-Type"])
                              (get-in r [:headers "content-type"]))]
    (string/starts-with? content-type "application/edn")))

(defn wrap-edn-response
  "Coerce response body to transit when a application/transit Content-Type is set."
  [handler]
  (fn [request]
    (let [response (handler request)]
      (if (edn-content-type? response)
        (update response :body #(with-out-str (pprint/pprint %)))
        response))))

(defn wrap-edn-request
  "Parse request body when content-type starts with application/transit."
  [handler]
  (fn [{:keys [body headers] :as request}]
    (if (edn-content-type? request)
      (handler (update request :body (comp edn/read-string slurp)))
      (handler request))))

(defn index-handler [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (slurp (io/resource "public/index.html"))})

(defn not-found-handler [_]
  {:status 404
   :headers {"Content-Type" "text/plain"}
   :body "Not Found"})

(def routes
  ["/" [["api" [api/routes]]
        ["" index-handler]
        [true not-found-handler]]])

(def app-handler (bidi/make-handler routes))

(def wrapped-handler
  (-> #'app-handler
      (wrap-reload)
      (wrap-edn-request)
      (wrap-edn-response)
      (wrap-resource "public")))

(def config
  {:duct.server.http/jetty
   {:port 3000
    :handler wrapped-handler}})

(def system (atom nil))

(defn start []
  (reset! system (ig/init config)))

(defn stop []
  (when @system
    (ig/halt! @system)
    (reset! system nil)))

(defn -main [& args]
  (start))
