(ns {{namespace}}.browser.components
  (:require [integrant.core :as ig]
            [joist.re-frame :as ui]
            [{{namespace}}.browser.rpc :as rpc]
            [re-frame.core :as rf]
            [taoensso.timbre :as log]))

(defn- dashboard-render [_ [_ {:keys [components] :as config}]]
  [:div.Dashboard
   [:h1 "Dashboard"]
   (for [[index c] (map-indexed vector components)]
     ^{:key index}
     [:div.Dashboard-component [c]])])

(defmethod ig/init-key ::dashboard [_ config]
  (ui/component {:config config
                 :render dashboard-render}))

(rf/reg-event-fx
  ::shopping-list-load
  (fn [_ [_ {:keys [workflowy-id] :as config}]]
    {:http-xhrio (rpc/request {:uri "/api/workflowy"
                               :timeout 30000
                               :on-success [::shopping-list-fetched]
                               :on-failure [:http-no-on-failure]
                               :params {:workflowy-id workflowy-id}})}))

(rf/reg-event-fx
  ::shopping-list-init
  (fn [_ [_ config]]
    {:db {:shopping-list-items :loading}
     :dispatch [::shopping-list-load config]
     :dispatch-interval {:dispatch [::shopping-list-load config]
                         :id ::shopping-list-interval
                         :ms 15000}}))


(rf/reg-event-db
  ::shopping-list-fetched
  (fn [db [_ response]]
    (assoc db :shopping-list-items (:items response))))

(rf/reg-sub
  ::shopping-list-state
  (fn [db _]
    {:items (:shopping-list-items db)}))

(defn- shopping-list-render [{:keys [items remove]}]
  [:div.ShoppingList
   [:h2 "Shopping List"]
   [:ul
    (if (= items :loading)
      [:li "Loading..."]
      (for [item items]
        ^{:key item}
        [:li {:on-click #(remove item %)}
         item]))]])

(defn- shopping-list [options]
  (let [state @(rf/subscribe [::shopping-list-state options])
        remove #(rf/dispatch [::shopping-list-remove % options])]
    (shopping-list-render (merge {:remove remove} state options))))

#_(def shopping-list
    (ui/component {:defaults {:remove #(rf/dispatch [::shopping-list-remove %])}
                   :on-mount #(rf/dispatch [::shopping-list-init %])
                   :state #(rf/subscribe [::shopping-list-state %])
                   :render shopping-list-render}))

(defmethod ig/init-key ::shopping-list [_ config]
  (fn [options] [shopping-list (merge config options)]))

;(defprotocol ShoppingListDB
;  (shopping-list-init [component])
;  (shopping-list-sub [component]))
;
;(defprotocol Sub
;  (sub [component]))
;
;(defrecord ShoppingList [workflowy-id items remove]
;  ShoppingListDB
;  (shopping-list-remove [this item]
;    (rf/dispatch [::shopping-list-remove this item]))
;
;  Init
;  (init [this]
;   (rf/dispatch [::shopping-list-init this]))
;
;  Sub
;  (sub [this]
;    @(rf/subscribe [::shopping-list-props this]))
;
;  Render
;  (render [_]
;    [:div.ShoppingList
;     [:h2 "Shopping List"]
;     [:ul]
;     (if (= items :loading)
;       [:li "Loading..."]
;       (for [item items]
;         ^{:key item}
;         [:li {:on-click (partial shopping-list-remove item)}
;          item]))]))
;
;(defn factory [constructor defaults]
;  (fn [options]
;    (-> (constructor (merge defaults options))
;        (init))
;    (fn [options]
;      (as-> (constructor (merge defaults options)) $
;            (merge $ (sub $))
;            (render $)))))
;
;(def shopping-list-v2 (factory map->ShoppingList))
;
;(defmethod ig/init-key ::shopping-list-v2 [_ config]
;  (factory map->ShoppingList config)
;  (fn [options] [shopping-list-v2 (merge config options)]))
