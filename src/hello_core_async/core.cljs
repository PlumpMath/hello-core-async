(ns hello-core-async.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [goog.net.XhrIo :as xhr]
            [cljs.core.async :refer [<! put! chan]])
  (:import [goog Uri]))

; We will call channels === queues for the ease of explanation
;; <!    - take a val from the channel 
;; put!  - asynchronously put val into the channel 
;; chan  - create a channel 
;; go    - 


(def yt-search-url
 "https://www.googleapis.com/youtube/v3/search?key=AIzaSyDqwW1i0Ul8s9yN6nKD5MAhnCCSTzOJDyg&part=snippet&q=")

(defn listen [el type]
  (let [out (chan)]
    (events/listen el type
                   (fn [e] (put! out e)))
    out))

(defn json [uri]
  (let [out (chan)]
    (xhr/send uri
      (fn [e]
        (let [items (-> e .-target .getResponseJson .-items)
              titles (map #(-> % .-snippet .-title) items)]
              (put! out titles))))
    out))

(defn query-url [q]
  (str yt-search-url q))

(defn user-query []
  (.-value (dom/getElement "query")))

(defn render-query [results]
  (str
    "<ul>"
    (apply str
      (for [result results]
        (str "<li>" result "</li>")))
    "</ul>"))

(defn init []
  (let [clicks (listen (dom/getElement "search") "click")
        results-view (dom/getElement "results")]
    (go (while true
          (<! clicks)
          (let [results (<! (json (query-url (user-query))))]
            (set! (.-innerHTML results-view) (render-query results)))))))

(init)