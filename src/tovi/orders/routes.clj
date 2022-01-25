(ns tovi.orders.routes
	(:require [tovi.spec :as s]
						[tovi.responses :as response]
						[tovi.orders.handlers :as handler]))


(def product-routes
	["/product"
	 {:swagger {:tags ["product"]}}
	 [""
		{:post {:summary  "Create a new product"
						:parameters {:header {:authorization string?} :body ::s/create-product}
						:responses {201 {:body response/product} 412 {:body response/errors}}
						:handler handler/create-product}
		 :get  {:summary "Get all products"
						:parameters {:header {:authorization string?}}
						:responses {200 {:body response/products}}
						:handler handler/get-products}}]
	 ["/id/:id"
		{:get {:summary    "Get a product by id"
					 :parameters {:header {:authorization string?} :path {:id int?}}
					 :responses {200 {:body response/product} 404 {:body response/errors}}
					 :handler handler/get-product-by-id}
		 :put {:summary    "Update a product"
					 :parameters {:header {:authorization string?} :path {:id int?} :body ::s/update-product}
					 :responses {200 {:body response/success} 412 {:body response/errors}}
					 :handler  handler/update-product}
		 :delete {:summary    "Delete a product"
							:parameters {:header {:authorization string?} :path {:id int?}}
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