(ns {{namespace}}.browser.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [{{namespace}}.browser-test]
            [taoensso.timbre :as log]))

(log/set-level! :info)

(doo-tests
 '{{namespace}}.browser-test)
