(ns tovi.admin.db
	(:require [honeysql.core :as honey]
						[honeysql.helpers :as helpers]
						[buddy.hashers :refer [encrypt]]
						[tovi.db :refer [query query-one]]))

;; Admin user functions

(defn get-all-users [db]
	(try
		(let [users (-> (helpers/select :*)
									(helpers/from :users)
									honey/format
									(query db))
					result (map #(dissoc % :password) users)]
			result)
		(catch Exception e
			(println e))))

(defn get-user-by-id [db id]
	(try
		(let [user (-> (helpers/select :id :name :last_name :email)
								 (helpers/from :users)
								 (helpers/where := :id id)
								 honey/format
								 (query-one db))]
			user)
		(catch Exception e
			(println e))))

(defn create-user [db {:keys [name last_name email pw]}]
	(try
		(let [encrypted-pw (encrypt pw)
					user (-> (helpers/insert-into :users)
								 (helpers/columns :name :last_name :email :password)
								 (helpers/values [[name last_name email encrypted-pw]])
								 honey/format
								 (query-one db))
					result (dissoc user :password)]
			result)
		(catch Exception e
			(println e))))

(defn update-user [db id {:keys [name last_name]}]
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

(defn delete-user [db id]
	(try
		(let [result (-> (helpers/delete-from :users)
									 (helpers/where := :id id)
									 honey/format
									 (query-one db)
									 (dissoc :password))]
			result)
		(catch Exception e
			(println e))))