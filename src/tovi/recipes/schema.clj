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
      [:quantity :int]
      [:percentage :int]]]]])

(def update-recipe-request
  [:and
   [:map
    [:name {:optional true} :string]
    [:steps {:optional true} :string]
    [:ingredients {:optional true}
     [:vector
      [:map
       [:ri_id {:optional true} :int]
       [:ingredient_id :int]
       [:quantity :int]
       [:percentage :int]
       [:operation {:optional true} :string]]]]]
   [:fn {:error/message "At least one field is required"}
    (fn [{:keys [name steps ingredients]}]
      (some #(not (nil? %1)) [name steps ingredients]))]])

(def recipe
  [:map
   [:id :int]
   [:name :string]
   [:steps :string]
   [:user_id :int]
   [:ingredients
    [:vector 
     [:map
      [:ri_id :int]
      [:ingredient_id :int]
      [:name :string]
      [:percentage :int]
      [:quantity :int]]]]])

(def recipes
  [:vector recipe])

(def ingredient
  [:map 
   [:id :int]
   [:name :string]])

(def ingredients
  [:vector ingredient])

(def ingredient-request
  [:map 
   [:name :string]])