(ns tovi.routes.users
	(:require [tovi.handlers.users :as users]
						[tovi.spec :as s]))

(def users-routes
	["/users"
	 {:swagger {:tags ["users"]}}
	 [""
		{:get {:summary "Get all users"
					 :handler users/get-all-users
					 :responses {200 {:body ::s/users-response}}}
		 :post {:summary "Create a new user"
						:parameters {:body ::s/signup-form}
						:responses {200 {:body ::s/user-response}}
						:handler users/create-user}}]
	 ["/id/:id"
		{:get {:summary "Get user by id"
					 :parameters {:path {:id int?}}
					 :handler users/get-user-by-id}
		 :put {:summary "Update a user by id"
					 :parameters {:path {:id int?} :body ::s/update-user-form}
					 :handler users/update-user}
		 :delete {:summary "Delete a user by id"
							:parameters {:path {:id int?}}
							:handler users/delete-user}}]])