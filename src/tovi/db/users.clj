(ns tovi.db.users
	(:require [buddy.hashers :refer [encrypt]]
						[tovi.db.core :as db]
						[honeysql.core :as honey]
						[honeysql.helpers :as helpers]))

(defn get-all-users []
	(try
		(let [users (-> (helpers/select :*)
									(helpers/from :users)
									honey/format
									db/db-query)
					result (map #(dissoc % :password) users)]
			result)
		(catch Exception e
			(println e))))

(defn get-user-by-id [id]
	(try
		(let [user (-> (helpers/select :id :name :last_name :email)
								 (helpers/from :users)
								 (helpers/where := :id id)
								 honey/format
								 db/db-query-one)]
			user)
		(catch Exception e
			(println e))))

(defn create-user [{:keys [name last_name email pw]}]
	(try
		(let [encrypted-pw (encrypt pw)
					user (-> (helpers/insert-into :users)
								 (helpers/columns :name :last_name :email :password)
								 (helpers/values [[name last_name email encrypted-pw]])
								 honey/format
								 db/db-query-one)
					result (dissoc user :password)]
			result)
		(catch Exception e
			(println e))))


(defn update-user [id {:keys [name last_name]}]
	(try
		(let [user (-> (helpers/update :users)
								 (helpers/set0 {:name name :last_name last_name})
								 (helpers/where := :id id)
								 honey/format
								 db/db-query-one
								 (dissoc :password))]
			user)
		(catch Exception e
			(println e))))

(defn delete-user [id]
	(try
		(let [result (-> (helpers/delete-from :users)
									 (helpers/where := :id id)
									 honey/format
									 db/db-query-one
									 (dissoc :password))]
			result)
		(catch Exception e
			(println e))))
