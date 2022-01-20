(ns tovi.orders.handlers
	(:require [tovi.exceptions :as exc]
						[clojure.spec.alpha :as s]
						[ring.util.response :as rr]
						[tovi.orders.db :as database]))

(defn create-product [{:keys [parameters db]}]
	(try
		(if-let [product (database/insert-product db (:body parameters))]
			(rr/created "" {:product-id "1"})
			(rr/status 412 {:errors ["Product could not been added"]}))
		(catch Exception e
			(exc/handle-exception e))))

(defn get-products [{:keys [parameters db]}]
	{:status 200 :body {:result :ok}})

(defn get-products [{:keys [parameters db]}]
	{:status 200 :body {:result :ok}})

(defn get-product-by-id [{:keys [parameters db]}]
	{:status 200 :body {:result :ok}})

(defn update-product [{:keys [parameters db]}]
	{:status 200 :body {:result :ok}})

(defn delete-product [{:keys [parameters db]}]
	{:status 200 :body {:result :ok}})

(defn create-order [{:keys [parameters db]}]
	{:status 200 :body {:result :ok}})

