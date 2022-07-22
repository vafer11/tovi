(ns tovi.accounts.db
  (:require [tovi.utils :as utils]
            [next.jdbc.sql :as sql]
            [honeysql.core :as honey]
            [tovi.db :refer [rs-config]]
            [honeysql.helpers :as helpers]
            [buddy.hashers :refer [check encrypt]]))

(defn ^:private check-pw [db [k v] password]
  (let [query (format "SELECT * FROM users WHERE %s = ?" k)
        user (-> (sql/query db [query v] rs-config) first)
        checked-pw? (and user (check password (:password user)))]
    (when checked-pw? user)))

(defn ^:private last-signin [db id]
  (let [values {:last_signin (utils/get-timestamp)}]
    (sql/update! db :users values {:id id} rs-config)))

(defn signup [db values]
  (let [values (-> values
                   (update-in [:password] encrypt)
                   (dissoc :confirm_pw))]
    (-> (sql/insert! db :users values rs-config)
        (utils/dissoc-values [:password :active :last_signin :created]))))

(defn signin [db {:keys [email password]}]
  (when-let [user (check-pw db ["email" email] password)]
    (last-signin db (:id user))
    (utils/dissoc-values user [:password :active :last_signin :created])))

(defn update-account [db {:keys [id] :as account}]
  (let [values (dissoc account :id)]
    (sql/update! db :users values {:id id} rs-config)))

(defn change-pw [db {:keys [id password new_pw]}]
  (when-let [checked-pw? (check-pw db ["id" id] password)]
    (let [values {:password (encrypt new_pw)}]
      (sql/update! db :users values {:id id} rs-config))))

(defn signout [_] nil)