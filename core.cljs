(ns calculator-app.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]))

;; --- App State ---
(defonce state (r/atom {:a "" :b "" :result nil}))

;; --- Calculator Logic ---
(defn parse-number [s]
  (js/parseFloat s))

(defn calculate [op]
  (let [{:keys [a b]} @state
        x (parse-number a)
        y (parse-number b)
        result (case op
                 "+" (+ x y)
                 "-" (- x y)
                 "*" (* x y)
                 "/" (if (not= y 0) (/ x y) "Err: /0")
                 "Invalid")]
    (swap! state assoc :result result)))

;; --- UI ---
(defn calculator []
  [:div.calculator
   [:h2 "ClojureScript Calculator"]
   [:input {:type "text"
            :placeholder "Enter number A"
            :value (:a @state)
            :on-change #(swap! state assoc :a (.. % -target -value))}]
   [:input {:type "text"
            :placeholder "Enter number B"
            :value (:b @state)
            :on-change #(swap! state assoc :b (.. % -target -value))}]
   [:div
    [:button {:on-click #(calculate "+")} "+ Add"]
    [:button {:on-click #(calculate "-")} "- Sub"]
    [:button {:on-click #(calculate "*")} "* Mul"]
    [:button {:on-click #(calculate "/")} "/ Div"]]
   [:div.result
    [:h3 "Result: " (str (:result @state))]]])

;; --- Mount ---
(defn ^:dev/after-load start []
  (rdom/render [calculator]
               (.getElementById js/document "app")))

(defn init []
  (start))

