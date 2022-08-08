(ns tovi.responses)

(def errors
  [:vector
   [:map
    [:error-key :keyword]
    [:msg :string]]])

(def success
  [:map
   [:success :string]])