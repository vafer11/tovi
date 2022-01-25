(ns tovi.orders.db
	(:require [honeysql.core :as honey]
						[tovi.db :refer [query-one query]]
						[honeysql.helpers :as helpers]))

(defn insert-product [db {:keys [name recipe-id]}]
	(-> (helpers/insert-into :products)
		(helpers/columns :name :recipe_id)
		(helpers/values [[name recipe-id]])
		honey/format
		(query-one db)))

(defn get-products [db]
	(-> (helpers/select :*)
		(helpers/from :products)
		honey/format
		(query db)))

(defn get-product-by-id [db id]
	(-> (helpers/select :*)
		(helpers/from :products)
		(helpers/where := :id id)
		honey/format
		(query-one db)))

(defn update-product [db id {:keys [name]}]
	(-> (helpers/update :products)
		(helpers/set0 {:name name})
		(helpers/where := :id id)
		honey/format
		(query-one db)))

(defn delete-product [db id]
	(-> (helpers/delete-from :products)
		(helpers/where := :id id)
		honey/format
		(query-one db)))