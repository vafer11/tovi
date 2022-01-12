(ns tovi.admin.routes
	(:require [tovi.spec :as s]
						[tovi.admin.handlers :as handler]
						[tovi.middleware.auth :refer [wrap-authenticated?]]))

(def admin-routes
	["/admin/user"
	 {:swagger {:tags ["admin"]}}
	 [""
		{:get {:summary "Get all users"
					 :middleware [wrap-authenticated?]
					 :parameters {:header {:authorization string?}}
					 :handler handler/get-all-users
					 :responses {200 {:body ::s/users-response}}}
		 :post {:summary "Create a new user"
						:parameters {:body ::s/signup-form}
						:responses {200 {:body ::s/user-response}}
						:handler handler/create-user}}]
	 ["/id/:id"
		{:get {:summary "Get user by id"
					 :parameters {:path {:id int?}}
					 :handler handler/get-user-by-id}
		 :put {:summary "Update a user by id"
					 :parameters {:path {:id int?} :body ::s/update-user-form}
					 :handler handler/update-user}
		 :delete {:summary "Delete a user by id"
							:parameters {:path {:id int?}}
							:handler handler/delete-user}}]])