(ns tovi.handlers.users
	(:require [tovi.db.users :as database]))

(defn get-all-users [{:keys [db]}]
	(let [users (database/get-all-users)]
		(if users
			{:status 200 :body {:result :ok
													:msg "Users retrieved successfully"
													:errors []
													:users users}}
			{:status 200 :body {:result :ko
													:msg "Empty list of users"
													:errors ["Empty list of users"]
													:users nil}})))

(defn get-user-by-id [{:keys [parameters]}]
	(let [id (get-in parameters [:path :id])
				user (database/get-user-by-id id)]
		(if user
			{:status 200 :body {:result :ok
													:msg "User retrieved successfully"
													:errors []
													:user user}}
			{:status 200 :body {:result :ko
													:msg (str "User with id " id "does not exit")
													:errors [(str "User with id " id "does not exit")]
													:user nil}})))

;; Administrators could create new users.
;; That functionality is available just for specific situations.
(defn create-user [{:keys [parameters]}]
	(let [body (:body parameters)
				user (database/create-user body)]
		(if user
			{:status 200 :body {:result :ok
													:msg "User successfully added"
													:errors []
													:user user}}
			{:status 200 :body {:result :ko
													:msg "User could not been created"
													:errors ["User could not been created"]
													:user nil}})))

(defn update-user [{:keys [parameters]}]
	(let [id (get-in parameters [:path :id])
				body (:body parameters)
				user (database/update-user id body)]
		(if user
			{:status 200 :body {:result :ok
													:msg "User successfully updated"
													:errors []
													:user user}}
			{:status 200 :body {:result :ko
													:msg "User could not been updated"
													:errors ["User could not been updated"]
													:user nil}})))

(defn delete-user [{:keys [parameters]}]
	(let [id (get-in parameters [:path :id])
				deleted-user (database/delete-user id)]
		(if deleted-user
			{:status 200 :body {:result :ok
													:msg "User successfully deleted"
													:errors ["User successfully deleted"]
													:user deleted-user}}
			{:status 200 :body {:result :ok
													:msg "User could not been deleted"
													:errors ["User could not been deleted"]
													:user nil}})))
