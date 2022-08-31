(ns tovi.recipes.db
  (:require [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [tovi.db :refer [rs-config insert]]))

;; :::::::::: Ingredients db functions :::::::::: ;;
(defn insert-ingredient [db ingredient]
  (sql/insert! db :ingredients ingredient rs-config))

(defn update-ingredient [db id ingredient]
  (sql/update! db :ingredients ingredient {:id id} rs-config))

(defn delete-ingredient [db id]
  (sql/delete! db :ingredients {:id id} rs-config))

(defn get-ingredients [db]
  (sql/query db ["SELECT * FROM ingredients"] rs-config))

(defn get-ingredient-by-id [db id]
  (first (sql/query db ["SELECT * FROM ingredients WHERE id = ?" id] rs-config)))

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

(defn update-recipe [db id {:keys [ingredients] :as recipe}]
  (let [recipe (dissoc recipe :ingredients)]
    (jdbc/with-transaction [tx db]
      (sql/update! tx :recipes recipe {:id id} rs-config)
      (update-recipes_ingredients tx id ingredients))))

(defn delete-recipe [db id]
  (sql/delete! db :recipes {:id id}))

(defn join-recipe-ingredients [recipes ingredients]
  (reduce
   (fn [acc {:keys [id] :as recipe}]
     (let [recipe-ingredients (filter #(= id (:recipe_id %1)) ingredients)]
       (-> recipe
           (assoc :ingredients recipe-ingredients)
           (cons acc))))
   []
   recipes))

(defn get-recipes [db]
  (let [recipes (sql/query db ["SELECT * FROM recipes"] rs-config)
        recipes-ingredients (sql/query db  ["SELECT ri.ri_id, ri.recipe_id, ri.ingredient_id, i.name, ri.quantity, ri.percentage 
                                             FROM recipes as r 
                                             INNER JOIN recipes_ingredients as ri ON r.id = recipe_id 
                                             INNER JOIN ingredients as i ON ri.ingredient_id = i.id"] rs-config)]
    (join-recipe-ingredients recipes recipes-ingredients)))

(defn get-recipe-by-id [db id]
  (let [recipes (sql/query db ["SELECT * FROM recipes WHERE id = ?" id] rs-config)
        recipes-ingredients (sql/query db  ["SELECT ri.ri_id, ri.recipe_id, ri.ingredient_id, i.name, ri.quantity, ri.percentage 
                                             FROM recipes as r 
                                             INNER JOIN recipes_ingredients as ri ON r.id = recipe_id 
                                             INNER JOIN ingredients as i ON ri.ingredient_id = i.id
                                             WHERE r.id = ?" id] rs-config)]
    (first (join-recipe-ingredients recipes recipes-ingredients))))