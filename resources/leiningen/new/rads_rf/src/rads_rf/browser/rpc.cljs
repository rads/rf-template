(ns {{namespace}}.browser.rpc
  (:require [ajax.protocols :as ajax-protocols]
            [cljs.pprint :as pprint]
            [cljs.reader :as reader]
            [ajax.interceptors :as ajax-interceptors]))

(def ^:private edn-request-format
  {:write #(with-out-str (pprint/pprint %))
   :content-type "application/edn"})

(defn- edn-read-fn [response]
  (reader/read-string (ajax-protocols/-body response)))

(def ^:private edn-response-format
  (ajax-interceptors/map->ResponseFormat
   {:read edn-read-fn
    :description "EDN"
    :content-type ["application/edn"]}))

(defn request [options]
  (let [{:keys [on-success on-failure uri timeout params]} options]
    {:method :post
     :uri uri
     :timeout (or timeout 10000)
     :params params
     :response-format edn-response-format
     :format edn-request-format
     :on-success on-success
     :on-failure on-failure}))
