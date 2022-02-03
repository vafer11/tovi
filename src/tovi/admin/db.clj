(ns tovi.admin.db
	(:require [tovi.utils :as utils]
						[honeysql.core :as honey]
						[honeysql.helpers :as helpers]
						[buddy.hashers :refer [encrypt]]
						[tovi.db :refer [insert select select-by update-by delete-by]]))

(defn get-all-users [db]
	(mapv
		#(dissoc % :password)
		(select db :users)))

(defn get-user-by-id [db id]
	(->
		(select-by db :users [:= :id id])
		(dissoc :password)))

(defn create-user [db {:keys [name last-name email pw phone]}]
	(-> (insert db :users {:name name
												 :last_name last-name
												 :email email
												 :password (encrypt pw)
												 :phone phone})
		(dissoc :password)))

(defn update-user [db id {:keys [name last-name phone]}]
	(-> (update-by db :users {:name name
														:last_name last-name
														:phone phone}
				[:= :id id])
		(dissoc :password)))

(defn delete-user [db id]
	(delete-by db :users [:= :id id]))