(ns tovi.admin.db
	(:require [tovi.utils :as utils]
						[next.jdbc.sql :as sql]
						[honeysql.core :as honey]
						[tovi.db :refer [rs-config]]
						[honeysql.helpers :as helpers]
						[buddy.hashers :refer [encrypt]]))

(defn get-users [db]
	(sql/query
		db
		["SELECT id, name, last_name, email, created, last_signin, active, phone FROM users"]
		rs-config))

(defn get-user-by-id [db id]
	(->
		(sql/query db
			["SELECT id, name, last_name, email, created, last_signin, active, phone
			FROM users
			WHERE id = ?" id]
			rs-config)
		first))

(defn create-user [db user]
	(let [values (-> user
								 (update-in [:password] encrypt)
								 (dissoc :confirm_pw))]
		(-> (sql/insert! db :users values rs-config)
			(utils/dissoc-values [:password :active :last_signin :created]))))

(defn update-user [db id user]
	(-> (sql/update! db :users user {:id id} rs-config)
		(dissoc :password)))

(defn delete-user [db id]
	(sql/delete! db :users {:id id} rs-config))