(ns tovi.accounts.db
	(:require [tovi.utils :as utils]
						[honeysql.core :as honey]
						[tovi.db :refer [query-one]]
						[honeysql.helpers :as helpers]
						[buddy.hashers :refer [check encrypt]]))

(defn ^:private get-user [db key filter]
	(-> (helpers/select :*)
		(helpers/from :users)
		(helpers/where := key filter)
		honey/format
		(query-one db)))

(defn ^:private update-user [db values id]
	(-> (helpers/update :users)
		(helpers/set0 values)
		(helpers/where := :id id)
		honey/format
		(query-one db)))

(defn ^:private check-pw [db key value pw]
	(let [user (get-user db key value)
				checked-pw? (and user (check pw (:password user)))]
		(when checked-pw? user)))

(defn ^:private last-signin [db id]
	(update-user db {:last_signin (utils/get-timestamp)} id))

(defn signup [db {:keys [name last-name email pw phone]}]
	(-> (helpers/insert-into :users)
		(helpers/columns :name :last_name :email :password :phone)
		(helpers/values [[name last-name email (encrypt pw) phone]])
		honey/format
		(query-one db)
		(utils/dissoc-values [:password :active :last_signin :created])))

(defn signin [db {:keys [email pw]}]
	(when-let [user (check-pw db :email email pw)]
		(last-signin db (:id user))
		(utils/dissoc-values user [:password :active :last_signin :created])))

(defn update-account [db {:keys [id name last-name]}]
	(update-user db {:name name :last_name last-name} id))

(defn change-pw [db {:keys [id pw new-pw]}]
	(when-let [checked-pw? (check-pw db :id id pw)]
		(update-user db {:password (encrypt new-pw)} id)))

(defn signout [_] nil)