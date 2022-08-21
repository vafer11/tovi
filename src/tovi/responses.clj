(ns tovi.responses)

(def error
  [:map
   [:error-key :keyword]
   [:msg :string]])

(def errors
  [:vector error])

(def success
  [:map
   [:success :string]])