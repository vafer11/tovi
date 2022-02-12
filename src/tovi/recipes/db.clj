(ns tovi.recipes.db
	(:require [next.jdbc :as jdbc]
						[tovi.utils :as utils]
						[next.jdbc.sql :as sql]
						[honeysql.core :as honey]
						[honeysql.helpers :as helpers]
						[tovi.db :refer [rs-config insert query]]))

;; :::::::::: Ingredients db functions :::::::::: ;;
(defn insert-ingredient [db ingredient]
	(sql/insert! db :ingredients ingredient rs-config))

(defn update-ingredient [db id ingredient]
	(sql/update! db :ingredients ingredient {:id id} rs-config))

(defn delete-ingredient [db id]
	(sql/delete! db :ingredients {:id id} rs-config))

(defn get-ingredients [db]
	(sql/query db  ["SELECT * FROM ingredients"] rs-config))

(defn get-ingredient-by-id [db id]
	(-> (sql/query db ["SELECT * FROM ingredients WHERE id = ?" id] rs-config)
		first))

;; :::::::::: Recipes db functions :::::::::: ;;
(defn insert-recipe [db {:keys [ingredients] :as recipe}]
	(let [recipe (dissoc recipe :ingredients)]
		(jdbc/with-transaction [tx db]
			(let [recipe_id (:id (sql/insert! tx :recipes recipe rs-config))
						fun (fn [acc v] (conj acc (assoc v :recipe_id recipe_id)))
						ingredients (reduce fun [] ingredients)]
				(insert ingredients :recipes_ingredients tx)
				{:recipe_id recipe_id}))))

(defn update-recipe [db id {:keys [ingredients] :as recipe}]
	(let [recipe (dissoc recipe :ingredients)]
		(jdbc/with-transaction [tx db]
			(sql/update! tx :recipes recipe {:id id} rs-config)
			;next recipes-ingredients relationship need to be updated (unit and quantity)
			)))

(defn delete-recipe [db id]
	(sql/delete! db :recipes {:id id}))

(defn get-recipes [db]
	(sql/query db ["SELECT * FROM recipes"] rs-config))

; Ver si esto se puede hacer mejor
(defn get-recipe-by-id [db id]
	(println id)
	(let [recipe (-> (sql/query db ["SELECT * FROM recipes WHERE id = ?" id] rs-config) first)
				result (query {:select [:ri.ri_id :i.name :ri.unit :ri.quantity]
											 :from [[:recipes :r]]
											 :join [[:recipes_ingredients :ri] [:= :r.id :recipe_id]
															[:ingredients :i] [:= :ri.ingredient_id :i.id]]
											 :where [:= :r.id id]} db)
				fun (fn [acc {:keys [ri_id name unit quantity]}]
							(conj acc {:ri_id ri_id :name name :unit unit :quantity quantity}))]
		(->> result (reduce fun []) (assoc recipe :ingredients))))
