(ns tovi.orders.db
	(:require [honeysql.core :as honey]
						[tovi.db :refer [query-one]]
						[honeysql.helpers :as helpers]))

(defn insert-product [db {:keys [name recipe-id]}]
	(-> (helpers/insert-into :products)
		(helpers/columns :name :recipe_id)
		(helpers/values [[name recipe-id]])
		honey/format
		(query-one db)))
