(ns {{namespace}}.browser.test-utils
  (:require [clojure.test :as test]
            [async-error.core :refer [<?]]
            [clojure.core.async :as async :refer [go]]))

(defn async
  ([f] (async {} f))
  ([{:keys [timeout-millis] :as options} f]
   (test/async done-fn
     (let [timeout-millis (or timeout-millis 500)
           done-ch (async/timeout timeout-millis)
           done-wrapper (fn []
                          (async/put! done-ch true)
                          (async/close! done-ch))]
       (go
        (let [passed? (<? done-ch)]
          (when-not passed?
            (-> (str "Test timed out after % millseconds" timeout-millis)
                (ex-info options)
                (throw)))
          (done-fn)))
       (f done-wrapper)))))
