(ns {{namespace}}.browser
  (:require [day8.re-frame.http-fx]
            [district0x.re-frame.interval-fx]
            [integrant.core :as ig]
            [joist.re-frame :as ui]
            [{{namespace}}.browser.components :as components]
            [reagent.core :as reagent]))

(def ^:private container
  (js/document.getElementById "Container"))

(def ^:private page-config
  {::components/dashboard
   {:components [(ig/ref ::components/shopping-list)]}

   ::components/shopping-list
   {:workflowy-id "WID.LXoXwkUAw7"}})

(defn- render []
  (let [{:keys [::components/dashboard]} (ig/init page-config)]
   (reagent/render [dashboard] container)))

(defn on-jsload []
  (render))

(defn ^:export -main [& args]
  (render))

