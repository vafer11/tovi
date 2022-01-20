(ns tovi.orders.routes
	(:require [tovi.spec :as s]
						[tovi.orders.handlers :as handler]))


(def product-routes
	["/product"
	 {:swagger {:tags ["product"]}}
	 [""
		{:post {:summary  "Create a new product"
						:parameters {:header {:authorization string?}
												 :body ::s/create-product}
						;:responses {201 {:body {:recipe-id string?}}}
						:handler handler/create-product}
		 :get  {:summary "Get all products"
						;:parameters {}
						;:responses {}
						:handler handler/get-products
						}}]
	 ["/id/:id"
		{:get {:summary    "Get a product by id"
					 :parameters {:path {:id int?}}
					 ;responses {}
					 :handler handler/get-product-by-id}
		 :put {:summary    "Update a product"
					 :parameters {:path {:id int?}}
					 ;:responses {}
					 :handler  handler/update-product}
		 :delete {:summary    "Delete a product"
							:parameters {:path {:id int?}}
							;:responses {}
							:handler   handler/delete-product}}]])

(def order-routes
	["/order"
	 {:swagger {:tags ["account"]}}
	 [""
		{:post {:summary "Create new order"
						;:parameters {}
						;:responses {}
						:handler handler/create-order}}]])