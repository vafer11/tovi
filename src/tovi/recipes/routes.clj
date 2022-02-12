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
						:responses {201 {:body {:recipe_id int?}} 412 {:body response/errors}}
						:handler handler/create-recipe}
		 :get {:summary "Get all recipes"
					 :parameters {:header {:authorization string?}}
					 :responses {200 {:body response/recipes}}
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

(def ingredient-routes
	["/ingredients"
	 {:swagger {:tags ["ingredients"]}}
	 [""
		{:post {:summary "Create a new ingredients"
						:parameters {:header {:authorization string?} :body ::s/create-ingredient}
						:responses {201 {:body response/ingredient} 412 {:body response/errors}}
						:handler handler/create-ingredient}
		 :get {:summary "Get all ingredients"
					 :middleware [wrap-authenticated?]
					 :parameters {:header {:authorization string?}}
					 :responses {200 {:body response/ingredients}}
					 :handler handler/get-ingredients}}]
	 ["/id/:id"
		{:get {:summary "Get ingredient by id"
					 :parameters {:header {:authorization string?} :path {:id int?}}
					 :responses {200 {:body response/ingredient} 404 {:body response/errors}}
					 :handler handler/get-ingredient-by-id}
		 :put {:summary "Update ingredient"
					 :parameters {:header {:authorization string?} :path {:id int?} :body ::s/update-ingredient}
					 :responses {200 {:body response/success} 412 {:body response/errors}}
					 :handler handler/update-ingredient}
		 :delete {:summary "Delete ingredient"
							:parameters {:header {:authorization string?} :path {:id int?}}
							:responses {200 {:body response/success} 404 {:body response/errors}}
							:handler handler/delete-ingredient}}]])
