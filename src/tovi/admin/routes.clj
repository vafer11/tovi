(ns tovi.admin.routes 
  (:require [tovi.admin.handlers :as handler]
            [tovi.admin.schema :as schema]
            [tovi.responses :as response]
            [tovi.middleware.auth :refer [wrap-authenticated?]]))

(def admin-routes
  ["/admin/user"
   {:swagger {:tags ["admin"]}
    :middleware [[wrap-authenticated?]]}
   [""
    {:get {:summary "Get all users"
           :parameters {:header [:map [:authorization :string]]} 
           :responses {200 {:body schema/users-request}}
           :handler handler/get-users}
     :post {:summary "Create a new user"
            :parameters {:header [:map [:authorization :string]] :body schema/create-user-request}
            :responses {201 {:body schema/user} 412 {:body response/errors}}
            :handler handler/create-user}}]
   ["/id/:id"
    {:get {:summary "Get user by id"
           :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]]}
           :responses {200 {:body schema/user} 404 {:body response/errors}}
           :handler handler/get-user-by-id}
     :put {:summary "Update a user by id"
           :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]] :body schema/update-user-request}
           :responses {200 {:body response/success} 412 {:body response/errors}}
           :handler handler/update-user}
     :delete {:summary "Delete a user by id"
              :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]]}
              :responses {200 {:body response/success} 412 {:body response/errors}}
              :handler handler/delete-user}}]])