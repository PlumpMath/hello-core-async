(ns hello-core-async.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [cljs.core.async :refer [<! put! chan]]))

; We will call channels === queues for the ease of explanation
;; <!    - take a val from the channel 
;; put!  - asynchronously put val into the channel 
;; chan  - create a channel 

(defn listen [el type]
  (let [out (chan)]
    (events/listen el type
                   (fn [e] (put! out e)))
    out))


(def clicks (listen (dom/getElement "search") "click"))

(go
  (while true
    (let [e (<! clicks)]
      (.log js/console e))))
