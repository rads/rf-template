(ns {{namespace}}.server.api
  (:require [clojure.string :as string]
            [sparkledriver.browser :as browser]
            [sparkledriver.element :as element]))

(defn get-shopping-list [{:keys [workflowy-id] :as request-params}]
  (browser/with-browser [browser (browser/make-browser)]
    (let [url (str "https://workflowy.com/s/" workflowy-id)
          items (-> browser
                    (browser/fetch! url)
                    (element/find-by-css* ".mainTreeRoot .task .content"))]
      (->> items
           (map element/inner-html)
           (remove string/blank?)
           (doall)))))

(defn workflowy-handler [{:keys [body] :as request}]
  (let [response {:items (get-shopping-list body)}]
    {:status 200
     :headers {"Content-Type" "application/edn"}
     :body response}))

(def routes
  ["/" [["workflowy" workflowy-handler]]])
