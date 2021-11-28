(ns tovi.handlers.auth
	(:require [tovi.db.auth :as database]
						[tovi.utils.auth :as auth]))

(defn signup [{:keys [parameters db]}]
	(let [user (database/signup (:body parameters))]
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
	(let [user (database/signin (:body parameters))]
		(if user
			{:status 200 :body {:result :ok
													:msg "User successfully signed in"
													:errors []
													:user user
													:token (auth/get-token user)}}
			{:status 200 :body {:result :ko
													:msg "Invalid user or password"
													:errors ["Invalid user or password"]}})))

(defn signout [{:keys [db]}]
	(println "db: " db)
	{:status 200 :body "ok"})