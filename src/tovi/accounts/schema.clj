(ns tovi.accounts.schema)

(def signup-request
  [:map
   [:name :string]
   [:last_name :string]
   [:email :string]
   [:phone {:optional true} :string]
   [:password :string]
   [:confirm_pw :string]])

(def signin-request
  [:map 
   [:email :string] 
   [:password :string]])

(def update-account-request
  [:and
   [:map
    [:id :int]
    [:name {:optional true} :string]
    [:last_name {:optional true} :string]
    [:phone {:optional true} :string]]
   [:fn {:error/message "At least one field is required"}
    (fn [{:keys [name last_name phone]}] 
      (some #(not (nil? %1)) [name last_name phone]))]])

(def change-pw-request
  [:map
   [:id :int]
   [:current_pw :string]
   [:new_pw :string]])


(def signup-response 
  [:map 
   [:id :int] 
   [:name :string]
   [:last_name :string]
   [:email :string]
   [:token :string]])