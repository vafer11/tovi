(ns tovi.orders.db
	(:require [honeysql.core :as honey]
						[tovi.db :refer [insert select select-by update-by delete-by]]
						[honeysql.helpers :as helpers]))

(defn insert-product [db {:keys [name recipe-id]}]
	(insert db :products {:name name :recipe_id recipe-id}))

(defn get-products [db]
	(select db :products))

(defn get-product-by-id [db id]
	(select-by db :products [:= :id id]))

(defn update-product [db id {:keys [name]}]
	(update-by db :products {:name name} [:= :id id]))

(defn delete-product [db id]
	(delete-by db :products [:= :id id]))