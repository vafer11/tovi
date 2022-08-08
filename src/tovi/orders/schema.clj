(ns tovi.orders.schema)

(def create-product-request
  [:map
   [:name :string]
   [:recipe_id :int]
   [:weight :int]])

(def update-product-request
  [:and
   [:map
    [:name {:optional true} :string]
    [:weight {:optional true} :int]]
   [:fn {:error/message "At least one field is required"}
    (fn [{:keys [name weight]}]
      (some #(not (nil? %1)) [name weight]))]])

(def product
  [:map
   [:id :int]
   [:name :string]
   [:recipe_id :int]
   [:weight :int]])

(def products
  [:vector product])