(ns tovi.accounts.db
	(:require [tovi.utils :as utils]
						[honeysql.core :as honey]
						[tovi.db :refer [insert select select-by update-by delete-by]]
						[honeysql.helpers :as helpers]
						[buddy.hashers :refer [check encrypt]]))

(defn ^:private check-pw [db key value pw]
	(let [user (select-by db :users [:= key value])
				checked-pw? (and user (check pw (:password user)))]
		(when checked-pw? user)))

(defn ^:private last-signin [db id]
	(update-by db :users {:last_signin (utils/get-timestamp)} [:= :id id]))

(defn signup [db {:keys [name last-name email pw phone]}]
	(-> (insert db :users {:name name :last_name last-name :email email :password (encrypt pw) :phone phone})
		(utils/dissoc-values [:password :active :last_signin :created])))

(defn signin [db {:keys [email pw]}]
	(when-let [user (check-pw db :email email pw)]
		(last-signin db (:id user))
		(utils/dissoc-values user [:password :active :last_signin :created])))

(defn update-account [db {:keys [id name last-name phone]}]
	(update-by db :users {:name name :last_name last-name :phone phone} [:= :id id]))

(defn change-pw [db {:keys [id pw new-pw]}]
	(when-let [checked-pw? (check-pw db :id id pw)]
		(update-by db :users {:password (encrypt new-pw)} [:= :id id])))

(defn signout [_] nil)