(ns tovi.orders.routes
  (:require [tovi.orders.schema :as schema]
            [tovi.responses :as response]
            [tovi.orders.handlers :as handler]))

(def product-routes
  ["/product"
   {:swagger {:tags ["product"]}}
   [""
    {:post {:summary  "Create a new product"
            :parameters {:header [:map [:authorization :string]] :body schema/create-product-request}
            :responses {201 {:body schema/product} 412 {:body response/errors}}
            :handler handler/create-product}
     :get  {:summary "Get all products"
            :parameters {:header [:map [:authorization :string]]}
            :responses {200 {:body schema/products}}
            :handler handler/get-products}}]
   ["/id/:id"
    {:get {:summary    "Get a product by id"
           :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]]}
           :responses {200 {:body schema/product} 404 {:body response/errors}}
           :handler handler/get-product-by-id}
     :put {:summary    "Update a product"
           :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]] :body schema/update-product-request}
           :responses {200 {:body response/success} 412 {:body response/errors}}
           :handler  handler/update-product}
     :delete {:summary    "Delete a product"
              :parameters {:header [:map [:authorization :string]] :path [:map [:id :int]]}
              :responses {200 {:body response/success} 412 {:body response/errors}}
              :handler   handler/delete-product}}]])

(def order-routes
  ["/order"
   {:swagger {:tags ["account"]}}
   [""
    {:post {:summary "Create new order"
						;:parameters {}
						;:responses {}
            :handler handler/create-order}}]])