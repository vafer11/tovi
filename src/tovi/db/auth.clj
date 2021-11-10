(ns tovi.db.auth
	(:require [buddy.hashers :refer [check encrypt]]
						[honeysql.core :as honey]
						[honeysql.helpers :as helpers]
						[tovi.db.core :as db]))

(defn signin [{:keys [email pw]}]
	(try
		(let [user (-> (helpers/select :*)
								 (helpers/from :users)
								 (helpers/where := :email email)
								 honey/format
								 db/db-query-one)
					result (dissoc user :password)
					valid-user? (and user (check pw (:password user)))]
			(if valid-user? result nil))
		(catch Exception e
			(println e))))

(defn signup [{:keys [name last_name email pw]}]
	(try
		(let [encrypted-pw (encrypt pw)
					user (-> (helpers/insert-into :users)
								 (helpers/columns :name :last_name :email :password)
								 (helpers/values [[name last_name email encrypted-pw]])
								 honey/format
								 db/db-query-one)
					result (dissoc user :password)]
			(if result result nil))
		(catch Exception e
			(println e))))

(defn signout [_] nil)