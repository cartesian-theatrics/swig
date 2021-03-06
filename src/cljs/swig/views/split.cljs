(ns swig.views.split
  (:require
   [swig.dispatch :as methods]
   [re-posh.core :as re-posh]
   [clojure.string :as str]
   [re-com.core :as re]))

(defn maps [f coll]
  (into #{} (map f) coll))

(defmethod methods/dispatch :swig.type/split
  [props]
  (let [split-id (:db/id props)
        children (sort-by :swig/index
                          @(re-posh/subscribe [:swig.subs.element/get-children
                                               split-id
                                               [:swig.type/view
                                                :swig.type/tab
                                                :swig.type/split]]))
        split    @(re-posh/subscribe [:swig.subs.split/get-split split-id])
        ops     (->> split :swig.split/ops :swig.operations/ops (maps :swig/type))]
    (methods/wrap props
                  [(case (:swig.split/orientation split) :horizontal re/h-split :vertical re/v-split)
                   :on-split-change #(re-posh/dispatch [:swig.events.split/set-split-percent (:db/id split) %])
                   :style           {:flex   "1 1 0%"
                                     :margin "0px"}
                   :attr            {:on-click (when (contains? ops :swig.operation/join)
                                                 (fn [event]
                                                   (when (and (-> event .-ctrlKey)
                                                              (-> event .-target .-className (str/includes? "split-splitter")))
                                                     (.stopPropagation event)
                                                     (re-posh/dispatch [:swig.events.view/join-views split-id]))))}
                   :initial-split   (:swig.split/split-percent split)
                   :panel-1 [methods/dispatch (first children)]
                   :panel-2 [methods/dispatch (second children)]])))
