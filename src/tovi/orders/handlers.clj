(ns tovi.orders.handlers
	(:require [tovi.exceptions :as exc]
						[clojure.spec.alpha :as s]
						[ring.util.response :as rr]
						[tovi.orders.db :as database]))

(defn create-product [{:keys [parameters db]}]
	(try
		(if-let [product (database/insert-product db (:body parameters))]
			(rr/created "" product)
			(rr/status {:body ["Product could not been added"]} 412))
		(catch Exception e
			(exc/handle-fk-exception e "products"))))

(defn get-products [{:keys [parameters db]}]
	(try
		(if-let [products (database/get-products db)]
			(rr/response products)
			(rr/not-found ["Empty list of products"]))
		(catch Exception e
			(exc/handle-exception e))))

(defn get-product-by-id [{:keys [parameters db]}]
	(try
		(let [id (-> parameters :path :id)]
			(if-let [product (database/get-product-by-id db id)]
				(rr/response product)
				(rr/not-found [(format "Product with the id %d does not exits" id)])))
		(catch Exception e
			(exc/handle-exception e))))

(defn update-product [{:keys [parameters db]}]
	(try
		(let [id (-> parameters :path :id) body (:body parameters)]
			(if-let [product (database/update-product db id body)]
				(rr/response {:success "Product successfully updated"})
				(rr/status {:body ["Product could not been updated"]})))
		(catch Exception e
			(exc/handle-exception e))))

(defn delete-product [{:keys [parameters db]}]
	(try
		(let [id (-> parameters :path :id)]
			(if-let [product (database/delete-product db id)]
				(rr/response {:success (format "Product with id %s has been successfully deleted" id)})
				(rr/status {:body ["Product could not been deleted"]} 412)))
		(catch Exception e
			(exc/handle-exception e))))

(defn create-order [{:keys [parameters db]}]
	{:status 200 :body {:result :ok}})

