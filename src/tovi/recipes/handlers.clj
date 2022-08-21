(ns tovi.recipes.handlers
  (:require [tovi.exceptions :as exc]
            [ring.util.response :as rr]
            [tovi.recipes.db :as database]))

;; :::::::::: Recipe Handlers :::::::::: ;;
(defn create-recipe [{:keys [parameters db]}]
  (try
    (if-let [recipe (database/insert-recipe db (:body parameters))]
      (rr/created "" recipe)
      (rr/status {:body [{:error-key :412
                          :msg "Recipe could not been added"}]} 412))
    (catch Exception e
      (exc/handle-exception e))))

(defn get-recipes [{:keys [_ db]}]
  (try
    (rr/response (database/get-recipes db))
    (catch Exception e
      (exc/handle-exception e))))

(defn update-recipe [{:keys [parameters db]}]
  (try
    (let [id (-> parameters :path :id) body (:body parameters)
          result (database/update-recipe db id body)]
      (if (not= 0 (:next.jdbc/update-count result))
        (rr/response {:success (format "Recipe with id %s updated successfully" id)})
        (rr/status {:body [{:error-key :412
                            :msg (format "Recipe with id %s could not be updated" id)}]} 412)))
    (catch Exception e
      (exc/handle-exception e))))

(defn get-recipe-by-id [{:keys [parameters db]}]
  (try
    (let [id (-> parameters :path :id)]
      (if-let [recipe (database/get-recipe-by-id db id)]
        (rr/response recipe)
        (rr/not-found [{:error-key :404
                        :msg (format "Recipe with id %s does not exist" id)}])))
    (catch Exception e
      (exc/handle-exception e))))

(defn delete-recipe [{:keys [parameters db]}]
  (try
    (let [id (-> parameters :path :id)
          result (database/delete-recipe db id)]
      (if (not= 0 (:next.jdbc/update-count result))
        (rr/response {:success (format "Recipe with id %s successfully deleted" id)})
        (rr/not-found [{:error-key :404
                        :msg (format "Recipe with id %s does not exist" id)}])))
    (catch Exception e
      (exc/handle-exception e))))

;; :::::::::: Ingredient Handlers :::::::::: ;;
(defn create-ingredient [{:keys [parameters db]}]
  (try
    (if-let [ingredient (database/insert-ingredient db (:body parameters))]
      (rr/created "" ingredient)
      (rr/status {:body [{:error-key :412
                          :msg "Ingredient could not be added"}]} 412))
    (catch Exception e
      (exc/handle-exception e))))

(defn get-ingredients [{:keys [_ db]}]
  (try
    (rr/response (database/get-ingredients db)) 
    (catch Exception e
      (exc/handle-exception e))))

(defn update-ingredient [{:keys [parameters db]}]
  (try
    (let [id (-> parameters :path :id) body (:body parameters)
          result (database/update-ingredient db id body)]
      (if (not= 0 (:next.jdbc/update-count result))
        (rr/response {:success (format "Ingredient with id %s updated successfully" id)})
        (rr/status {:body [{:error-key :412
                            :msg (format "Ingredient with id %s could not be updated" id)}]} 412)))
    (catch Exception e
      (exc/handle-exception e))))

(defn get-ingredient-by-id [{:keys [parameters db]}]
  (try
    (let [id (-> parameters :path :id)]
      (if-let [ingredient (database/get-ingredient-by-id db id)]
        (rr/response ingredient)
        (rr/not-found [{:error-key :404
                        :msg (format "Ingredient with id %s does not exist" id)}])))
    (catch Exception e
      (exc/handle-exception e))))

(defn delete-ingredient [{:keys [parameters db]}]
  (try
    (let [id (-> parameters :path :id)
          result (database/delete-ingredient db id)]
      (if (not= 0 (:next.jdbc/update-count result))
        (rr/response {:success (format "Ingredient with id %s successfully deleted" id)})
        (rr/not-found [{:error-key :404
                        :msg (format "Ingredient with id %s could not be deleted" id)}])))
    (catch Exception e
      (exc/handle-exception e))))