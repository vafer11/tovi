(ns tovi.admin.schema)

(def create-user-request
  [:map
   [:name :string]
   [:last_name :string]
   [:email :string]
   [:phone {:optional true} :string]
   [:password :string]
   [:confirm_pw :string]])

(def update-user-request
  [:and
   [:map
    [:name {:optional true} :string]
    [:last_name {:optional true} :string]
    [:phone {:optional true} :string]]
   [:fn {:error/message "At least one field is required"}
    (fn [{:keys [name last_name phone]}]
      (some #(not (nil? %1)) [name last_name phone]))]])

(def user
  [:map
   [:id :int]
   [:name :string]
   [:last_name :string]
   [:email :string]])

(def users-request
  [:vector user])
