(ns tovi.recipes.routes
  (:require [tovi.recipes.schema :as schema]
            [tovi.responses :as response]
            [tovi.recipes.handlers :as handler]
            [tovi.middleware.auth :refer [wrap-authenticated?]]))

(def recipe-routes
  ["/recipes"
   {:swagger {:tags ["recipes"]}}
   [""
    {:post {:summary "Create a new recipe"
            :parameters {:header [:map [:authorization :string]] :body schema/create-recipe-request}
            :responses {201 {:body [:map [:recipe_id :int]]} 412 {:body response/errors}}
            :handler handler/create-recipe}
     :get {:summary "Get all recipes"
           :parameters {:header [:map [:authorization :string]]}
           :responses {200 {:body schema/recipes}}
           :handler handler/get-recipes}}]
   ["/:id"
    {:get {:summary "Get recipe by id"
           :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]]}
           :responses {200 {:body schema/recipe} 404 {:body response/errors}}
           :handler handler/get-recipe-by-id}
     :put {:summary "Update recipe"
           :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]] :body schema/update-recipe-request}
           :responses {200 {:body response/success} 412 {:body response/errors}}
           :handler handler/update-recipe}
     :delete {:summary "Delete recipe"
              :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]]}
              :responses {200 {:body response/success} 404 {:body response/errors}}
              :handler handler/delete-recipe}}]])

(def ingredient-routes
  ["/ingredients"
   {:swagger {:tags ["ingredients"]}}
   [""
    {:post {:summary "Create a new ingredients"
            :parameters {:header [:map [:authorization :string]] :body schema/ingredient-request}
            :responses {201 {:body schema/ingredient} 412 {:body response/errors}}
            :handler handler/create-ingredient}
     :get {:summary "Get all ingredients"
           :middleware [wrap-authenticated?]
           :parameters {:header [:map [:authorization :string]]}
           :responses {200 {:body schema/ingredients}}
           :handler handler/get-ingredients}}]
   ["/:id"
    {:get {:summary "Get ingredient by id"
           :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]]}
           :responses {200 {:body schema/ingredient} 404 {:body response/errors}}
           :handler handler/get-ingredient-by-id}
     :put {:summary "Update ingredient"
           :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]] :body schema/ingredient-request}
           :responses {200 {:body response/success} 412 {:body response/errors}}
           :handler handler/update-ingredient}
     :delete {:summary "Delete ingredient"
              :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]]}
              :responses {200 {:body response/success} 404 {:body response/errors}}
              :handler handler/delete-ingredient}}]])
