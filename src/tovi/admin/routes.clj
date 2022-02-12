(ns tovi.admin.routes
	(:require [tovi.spec :as s]
						[tovi.responses :as response]
						[tovi.admin.handlers :as handler]
						[tovi.middleware.auth :refer [wrap-authenticated?]]))

(def admin-routes
	["/admin/user"
	 {:swagger {:tags ["admin"]}}
	 [""
		{:get {:summary "Get all users"
					 :parameters {:header {:authorization string?}}
					 :responses {200 {:body response/users}}
					 :handler handler/get-users}
		 :post {:summary "Create a new user"
						:parameters {:header {:authorization string?} :body ::s/signup}
						:responses {201 {:body response/create-user} 412 {:body response/errors}}
						:handler handler/create-user}}]
	 ["/id/:id"
		{:get {:summary "Get user by id"
					 :parameters {:header {:authorization string?} :path {:id int?}}
					 :responses {200 {:body response/user} 404 {:body response/errors}}
					 :handler handler/get-user-by-id}
		 :put {:summary "Update a user by id"
					 :parameters {:header {:authorization string?} :path {:id int?} :body ::s/update-user}
					 :responses {200 {:body response/success} 412 {:body response/errors}}
					 :handler handler/update-user}
		 :delete {:summary "Delete a user by id"
							:parameters {:header {:authorization string?} :path {:id int?}}
							:responses {200 {:body response/success} 412 {:body response/errors}}
							:handler handler/delete-user}}]])