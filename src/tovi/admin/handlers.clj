(ns tovi.admin.handlers
	(:require [tovi.exceptions :as exc]
						[ring.util.response :as rr]
						[tovi.admin.db :as database]))

(defn get-users [{:keys [parameters db]}]
	(try
		(if-let [users (database/get-users db)]
			(rr/response users))
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
		(let [id (get-in parameters [:path :id]) body (:body parameters)
					result (database/update-user db id body)]
			(if (not= 0 (:next.jdbc/update-count result))
				(rr/response {:success (format "User with id %d successfully updated" id)})
				(rr/status {:body [(format "User with id %d could not been updated " id)]} 412)))
		(catch Exception e
			(exc/handle-exception e))))

(defn delete-user [{:keys [parameters db]}]
	(try
		(let [id (-> parameters :path :id)
					result (database/delete-user db id)]
			(if (not= 0 (:next.jdbc/update-count result))
				(rr/response {:success (format "User with id %s successfully deleted" id)})
				(rr/status {:body [(format "User with id %s could not been deleted" id)]} 412)))
		(catch Exception e
			(exc/handle-exception e))))