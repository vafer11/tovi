(ns tovi.recipes.schema)

(def create-recipe-request 
  [:map 
   [:user_id :int]
   [:name :string]
   [:steps :string]
   [:ingredients 
    {:optional true}
    [:vector
     [:map
      [:ingredient_id :int]
      [:quantity :int]]]]])

(def recipe 
  [:map
   [:id :int]
   [:name :string]
   [:steps :string]
   [:user_id :int]])

(def recipes-response
  [:vector recipe])

(def update-recipe-ingredient
  [:map
   [:ingredient_id :int]
   [:quantity :int]
   [:ri_id {:optional true} :int]
   [:operation {:optional true} :string]])

(def update-ingredients
  [:vector update-recipe-ingredient])

(def update-recipe-request
  [:and
   [:map 
    [:name {:optional true} :string]
    [:steps {:optional true} :string]
    [:update-ingredients {:optional true} update-ingredients]]
   [:fn {:error/message "At least one field is required"}
    (fn [{:keys [name steps]}]
      (some #(not (nil? %1)) [name steps]))]])

(def ingredient
  [:map 
   [:id :int]
   [:name :string]])

(def ingredients
  [:vector ingredient])

(def ingredient-request
  [:map 
   [:name :string]])