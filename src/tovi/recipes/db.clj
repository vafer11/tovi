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
            fun (fn [acc v]
                  (conj acc (assoc v :recipe_id recipe_id)))
            ingredients (reduce fun [] ingredients)]
        (insert ingredients :recipes_ingredients tx)
        {:recipe_id recipe_id}))))

(defn update-recipes_ingredients [db recipe_id ingredients]
  (let [fun (fn [acc v]
              (let [{:keys [operation ri_id]} v
                    value (-> v (dissoc :operation) (assoc :recipe_id recipe_id))]
                (case operation
                  "insert"
                  (let [result (sql/insert! db :recipes_ingredients value rs-config)]
                    (if result (inc acc) acc))
                  "update"
                  (let [result (sql/update! db :recipes_ingredients value {:ri_id ri_id
                                                                           :recipe_id recipe_id} rs-config)]
                    (+ acc (:next.jdbc/update-count result)))
                  "delete"
                  (let [result (sql/delete! db :recipes_ingredients {:ri_id ri_id
                                                                     :recipe_id recipe_id} rs-config)]
                    (+ acc (:next.jdbc/update-count result)))
                  :default acc)))
        operation-count (reduce fun 0 ingredients)]
    {:next.jdbc/update-count operation-count}))

(defn update-recipe [db id {:keys [update-ingredients] :as recipe}]
  (let [recipe (dissoc recipe :update-ingredients)]
    (jdbc/with-transaction [tx db]
      (sql/update! tx :recipes recipe {:id id} rs-config)
      (update-recipes_ingredients tx id update-ingredients))))

(defn delete-recipe [db id]
  (sql/delete! db :recipes {:id id}))

(defn get-recipes [db]
  (sql/query db ["SELECT * FROM recipes"] rs-config))

(defn get-recipe-by-id [db id]
  (if-let [recipe (-> (sql/query db ["SELECT * FROM recipes WHERE id = ?" id] rs-config) first)]
    (let [result (sql/query db  ["SELECT ri.ri_id, i.name, ri.unit, ri.quantity
																	FROM recipes as r
																	INNER JOIN recipes_ingredients as ri ON r.id = recipe_id
																	INNER JOIN ingredients as i ON ri.ingredient_id = i.id
																	WHERE r.id = ?" id] rs-config)
          fun (fn [acc {:keys [ri_id name unit quantity]}]
                (conj acc {:ri_id ri_id :name name :unit unit :quantity quantity}))]
      (->> result (reduce fun []) (assoc recipe :ingredients)))))