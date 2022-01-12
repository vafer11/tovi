(ns tovi.accounts.db
	(:require [buddy.hashers :refer [check encrypt]]
						[tovi.db :refer [query-one]]
						[honeysql.core :as honey]
						[honeysql.helpers :as helpers]))

(defn signup [db {:keys [name last_name email pw]}]
	(try
		(let [encrypted-pw (encrypt pw)
					user (-> (helpers/insert-into :users)
								 (helpers/columns :name :last_name :email :password)
								 (helpers/values [[name last_name email encrypted-pw]])
								 honey/format
								 (query-one db))
					result (dissoc user :password)]
			(if result result nil))
		(catch Exception e
			(println e))))

(defn signin [db {:keys [email pw]}]
	(try
		(let [user (-> (helpers/select :*)
								 (helpers/from :users)
								 (helpers/where := :email email)
								 honey/format
								 (query-one db))
					result (dissoc user :password)
					valid-user? (and user (check pw (:password user)))]
			(if valid-user? result nil))
		(catch Exception e
			(println e))))

(defn update-account [db id {:keys [name last_name]}]
	(try
		(let [user (-> (helpers/update :users)
								 (helpers/set0 {:name name :last_name last_name})
								 (helpers/where := :id id)
								 honey/format
								 (query-one db)
								 (dissoc :password))]
			user)
		(catch Exception e
			(println e))))

(defn signout [_] nil)