(ns tovi.accounts.handlers
	(:require [tovi.accounts.db :as database]
						[tovi.utils.auth :as auth]))

(defn signup [{:keys [parameters db]}]
	(let [user (database/signup db (:body parameters))]
		(if user
			{:status 200 :body {:result :ok
													:msg "User successfully signed up"
													:errors []
													:user user
													:token (auth/get-token user)}}
			{:status 200 :body {:result :ko
													:msg "Invalid values"
													:errors ["Invalid values"]}})))

(defn signin [{:keys [parameters db]}]
	(let [user (database/signin db (:body parameters))]
		(if user
			{:status 200 :body {:result :ok
													:msg "User successfully signed in"
													:errors []
													:user user
													:token (auth/get-token user)}}
			{:status 200 :body {:result :ko
													:msg "Invalid user or password"
													:errors ["Invalid user or password"]}})))

(defn update-account [{:keys [parameters db]}]
	(let [id (get-in parameters [:path :id])
				body (:body parameters)
				user (database/update-account db id body)]
		(if user
			{:status 200 :body {:result :ok
													:msg "Account successfully updated"
													:errors []
													:user user}}
			{:status 200 :body {:result :ko
													:msg "Account could not been updated"
													:errors ["Account could not been updated"]}})))

(defn signout [{:keys [db]}]
	(println "db: " db)
	{:status 200 :body "ok"})