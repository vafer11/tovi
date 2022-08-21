(ns tovi.exceptions
  (:require [clojure.tools.logging :as log]
            [ring.util.response :as rr]))

;:::::::::::::: Check unique constraint exception ::::::::::::::;
(defn unique-constraint-exception? [msg constraint]
  (if (-> msg (.startsWith (format
                            "ERROR: duplicate key value violates unique constraint \"%s\""
                            constraint)))
    true
    false))

;:::::::::::::: Check foreign constraint exception ::::::::::::::;
(defn foreign-constraint-exception? [msg action table constraint]
  (if (-> msg (.startsWith (format
                            "ERROR: %s on table \"%s\" violates foreign key constraint \"%s\""
                            action table constraint)))
    true
    false))

(defn dispatch-handle-exception [exc]
  ;(log/error exc)
  (if-let [sql-exception? (instance? java.sql.SQLException exc)]
    (let [msg (-> exc (.getMessage))]
      (cond
        (unique-constraint-exception? msg "users_email_key") :users-email-key
        (unique-constraint-exception? msg "users_phone_key") :users-phone-key
        (foreign-constraint-exception? msg "insert or update" "products" "fk_products_recipe_id") :insert-fk-products-recipe-id
        (foreign-constraint-exception? msg "update or delete" "recipes" "fk_products_recipe_id") :delete-fk-products-recipe-id
        (foreign-constraint-exception? msg "insert or update" "recipes" "fk_user") :insert-fk-user-id
        (foreign-constraint-exception? msg "insert or update" "recipes_ingredients" "fk_ingredients") :insert-fk-ingredient-id
        (foreign-constraint-exception? msg "insert or update" "recipes_ingredients" "fk_recipe") :recipes_ingredients-not-recipe-id
        :else :default-exception))
    (rr/status {:body [{:error-key :500
                        :msg "Server error occurred"}]} 500)))

(defmulti handle-exception dispatch-handle-exception)

(defmethod handle-exception :users-email-key [_]
  (rr/status {:body {:error-key :users-email-key
                     :msg "User with the selected email already exists"}} 412))

(defmethod handle-exception :users-phone-key [_]
  (rr/status {:body {:error-key :users-phone-key
                     :msg "User with the selected phone already exists"}} 412))

(defmethod handle-exception :insert-fk-products-recipe-id [_]
  (rr/status {:body [{:error-key :insert-fk-products-recipe-id
                      :msg "The recipe id does not exist"}]} 412))

(defmethod handle-exception :delete-fk-products-recipe-id [_]
  (rr/status {:body [{:error-key :delete-fk-products-recipe-id
                      :msg "This recipe can not be deleted, cause products reference it"}]} 412))

(defmethod handle-exception :insert-fk-user-id [_]
  (rr/status {:body [{:error-key :insert-fk-user-id
                      :msg "The user id does not exist"}]} 412))

(defmethod handle-exception :insert-fk-ingredient-id [_]
  (rr/status {:body [{:error-key :insert-fk-ingredient-id
                      :msg "The ingredient id does not exist"}]} 412))

(defmethod handle-exception :recipes_ingredients-not-recipe-id [_]
  (rr/status {:body [{:error-key :recipes_ingredients-not-recipe-id
                      :msg "The recipe id does not exist"}]} 412))

(defmethod handle-exception :default-exception [_]
  (rr/status {:body [{:error-key :default-exception
                      :msg "Server error occurred"}]} 500))