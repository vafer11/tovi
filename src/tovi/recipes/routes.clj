(ns tovi.recipes.routes
	(:require [tovi.spec :as s]
		[tovi.responses :as response]
		[tovi.recipes.handlers :as handler]
		[tovi.middleware.auth :refer [wrap-authenticated?]]))

(def recipe-routes
	["/recipes"
	 {:swagger {:tags ["recipes"]}}
	 [""
		{:post {:summary "Create a new recipe"
						:parameters {:header {:authorization string?} :body ::s/create-recipe}
						:responses {201 {:body response/recipe} 412 {:body response/errors}}
						:handler handler/create-recipe}
		 :get {:summary "Get all recipes"
					 :middleware [wrap-authenticated?]
					 :parameters {:header {:authorization string?}}
					 :responses {200 {:body response/recipes} 404 {:body response/errors}}
					 :handler handler/get-recipes}}]
	 ["/id/:id"
		{:get {:summary "Get recipe by id"
					 :parameters {:header {:authorization string?} :path {:id int?}}
					 :responses {200 {:body response/recipe} 404 {:body response/errors}}
					 :handler handler/get-recipe-by-id}
		 :put {:summary "Update recipe"
					 :parameters {:header {:authorization string?} :path {:id int?} :body ::s/update-recipe}
					 :responses {200 {:body response/success} 412 {:body response/errors}}
					 :handler handler/update-recipe}
		 :delete {:summary "Delete recipe"
							:parameters {:header {:authorization string?} :path {:id int?}}
							:responses {200 {:body response/success} 404 {:body response/errors}}
							:handler handler/delete-recipe}}]])
