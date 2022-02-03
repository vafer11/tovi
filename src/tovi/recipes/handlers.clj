(ns tovi.recipes.handlers
	(:require [tovi.exceptions :as exc]
						[ring.util.response :as rr]
						[tovi.recipes.db :as database]))

(defn create-recipe [{:keys [parameters db]}]
	(try
		(if-let [recipe (database/insert-recipe db (:body parameters))]
			(rr/created "" recipe)
			(rr/status {:body ["Recipe could not been added"]} 412))
		(catch Exception e
			(exc/handle-exception e))))

(defn get-recipes [{:keys [parameters db]}]
	(try
		(if-let [recipes (database/get-recipes db)]
			(rr/response recipes)
			(rr/not-found ["Empty list of recipes"]))
		(catch Exception e
			(exc/handle-exception e))))

(defn update-recipe [{:keys [parameters db]}]
	(try
		(let [id (-> parameters :path :id) body (:body parameters)]
			(if-let [recipe (database/update-recipe db id body)]
				(rr/response {:success "Recipe updated successfully"})
				(rr/status {:body ["Recipe could not been updated"]} 412)))
		(catch Exception e
			(exc/handle-exception e))))

(defn get-recipe-by-id [{:keys [parameters db]}]
	(try
		(let [id (-> parameters :path :id)]
			(if-let [users (database/get-recipe-by-id db id)]
				(rr/response users)
				(rr/not-found [(format "Recipe with id %s does not exist" id)])))
		(catch Exception e
			(exc/handle-exception e))))

(defn delete-recipe [{:keys [parameters db]}]
	(try
		(let [id (-> parameters :path :id)]
			(if-let [recipe (database/delete-recipe db id)]
				(rr/response {:success (format "Recipe with id %s successfully deleted" id)})
				(rr/not-found [(format "Recipe with id %s does not exist" id)])))
		(catch Exception e
			(exc/handle-exception e))))