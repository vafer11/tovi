(ns tovi.routes.users
	(:require [tovi.handlers.users :as users]
						[tovi.schema :as s]))

(def users-routes
	["/users"
	 {:swagger {:tags ["users"]}}
	 [""
		{:get {:summary "Get all users"
					 :handler users/get-all-users
					 :responses {200 {:body s/users-response}}}
		 :post {:summary "Create a new user"
						:parameters {:body s/user-form}
						:responses {200 {:body s/user-response}}
						:handler users/create-user}}]
	 ["/id/:id"
		{:get {:summary "Get user by id"
					 :parameters {:path {:id s/s-int}}
					 :handler users/get-user-by-id}
		 :put {:summary "Update a user by id"
					 :parameters {:path {:id s/s-int}
												:body s/update-user-form}
					 :handler users/update-user}
		 :delete {:summary "Delete a user by id"
							:parameters {:path {:id s/s-int}}
							:handler users/delete-user}}]])