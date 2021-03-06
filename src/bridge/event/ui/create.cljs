(ns bridge.event.ui.create
  (:require bridge.event.spec
            [bridge.ui.routes :as ui.routes]
            [bridge.ui.component.date :as ui.date]
            [bridge.ui.util :refer [<== ==>]]
            [clojure.spec.alpha :as s]
            [reagent.core :as r]))

;; TODO display errors
;; TODO clear form on save, but only if there are no errors to display

(defn create-event [_]
  (let [*form (r/atom {})]
    (fn []
      [:div.column.is-two-fifths
       [:h3.title.is-4.spaced "Create a new event"]

       [:div.field
        [:label.label "Event Title"]
        [:div.control
         [:input.input
          {:type        "text"
           :placeholder "Event Title"
           :on-change   #(swap! *form assoc :event/title
                                (.. % -currentTarget -value))}]]]

       [:div.field
        [:label.label "Event Date(s)"]
        [:div.control
         [ui.date/select-dates *form
          :event/start-date nil
          :event/end-date nil]]]

       [:div.field.is-grouped
        [:div.control
         [:button.button.is-link
          (cond-> {:href     "javascript:"
                   :on-click #(==> [:bridge.event.ui/save-new-event!
                                    (<== [:bridge.ui/active-chapter])
                                    @*form])}
            (not (s/valid? :bridge/new-event @*form))
            (assoc :disabled "disabled"))
          "Create this event"]]
        [:div.control
         [:a.button.is-text (ui.routes/turbo-links "/app/events") "Cancel"]]]])))
