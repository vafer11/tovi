(ns tovi.recipes.db
	(:require [tovi.utils :as utils]
						[honeysql.core :as honey]
						[tovi.db :refer [insert select select-by update-by delete-by]]
						[honeysql.helpers :as helpers]))

(defn insert-recipe [db {:keys [name description steps user-id]}]
	(insert db :recipes {:name name :description description :steps steps :user_id user-id}))

(defn update-recipe [db id {:keys [name description steps]}]
	(update-by db :recipes {:name name :description description :steps steps} [:= :id id]))

(defn get-recipes [db]
	(select db :recipes))

(defn get-recipe-by-id [db id]
	(select-by db :recipes [:= :id id]))

(defn delete-recipe [db id]
	(delete-by db :recipes [:= :id id]))