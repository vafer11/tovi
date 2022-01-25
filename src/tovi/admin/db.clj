(ns tovi.admin.db
	(:require [honeysql.core :as honey]
						[honeysql.helpers :as helpers]
						[buddy.hashers :refer [encrypt]]
						[tovi.db :refer [query query-one]]))

(defn get-all-users [db]
	(-> (helpers/select :id :name :last_name :email)
		(helpers/from :users)
		honey/format
		(query db)))

(defn get-user-by-id [db id]
	(-> (helpers/select :id :name :last_name :email)
		(helpers/from :users)
		(helpers/where := :id id)
		honey/format
		(query-one db)))

(defn create-user [db {:keys [name last-name email pw phone]}]
	(-> (helpers/insert-into :users)
		(helpers/columns :name :last_name :email :password :phone)
		(helpers/values [[name last-name email (encrypt pw) phone]])
		honey/format
		(query-one db)
		(dissoc :password)))

(defn update-user [db id {:keys [name last-name phone]}]
	(-> (helpers/update :users)
		(helpers/set0 {:name name :last_name last-name :phone phone})
		(helpers/where := :id id)
		honey/format
		(query-one db)
		(dissoc :password)))

(defn delete-user [db id]
	(-> (helpers/delete-from :users)
		(helpers/where := :id id)
		honey/format
		(query-one db)
		(dissoc :password)))