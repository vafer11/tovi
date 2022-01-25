(ns tovi.admin.handlers
	(:require [tovi.exceptions :as exc]
						[ring.util.response :as rr]
						[tovi.admin.db :as database]))

(defn get-all-users [{:keys [parameters db]}]
	(try
		(if-let [users (database/get-all-users db)]
			(rr/response users)
			(rr/not-found ["Empty list of users"]))
		(catch Exception e
			(exc/handle-exception e))))

(defn get-user-by-id [{:keys [parameters db]}]
	(try
		(let [id (get-in parameters [:path :id])]
			(if-let [user (database/get-user-by-id db id)]
				(rr/response user)
				(rr/not-found [(format "User with id %d does not exit" id)])))
		(catch Exception e
			(exc/handle-exception e))))

;; Administrators could create users
(defn create-user [{:keys [parameters db]}]
	(try
		(if-let [user (database/create-user db (:body parameters))]
			(rr/created "" {:user user})
			(rr/status {:body ["Invalid values"]} 412))
		(catch Exception e
			(exc/handle-exception e))))

(defn update-user [{:keys [parameters db]}]
	(try
		(let [id (get-in parameters [:path :id]) body (:body parameters)]
			(if-let [user (database/update-user db id body)]
				(rr/response {:success (format "User with id %d successfully updated" id)})
				(rr/status {:body ["Invalid values"]} 412)))
		(catch Exception e
			(exc/handle-exception e))))

(defn delete-user [{:keys [parameters db]}]
	(try
		(if-let [deleted-user (->> (get-in parameters [:path :id])
														(database/delete-user db))]
			(rr/response {:success "User successfully deleted"})
			(rr/status {:body ["User could not been deleted"]} 412))
		(catch Exception e
			(exc/handle-exception e))))