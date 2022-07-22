(ns tovi.orders.db
  (:require [next.jdbc.sql :as sql]
            [honeysql.core :as honey]
            [tovi.db :refer [rs-config]]
            [honeysql.helpers :as helpers]))

(defn insert-product [db product]
  (sql/insert! db :products product rs-config))

(defn get-products [db]
  (sql/query db  ["SELECT * FROM products"] rs-config))

(defn get-product-by-id [db id]
  (-> (sql/query db ["SELECT * FROM products WHERE id = ?" id] rs-config)
      first))

(defn update-product [db id product]
  (sql/update! db :products product {:id id} rs-config))

(defn delete-product [db id]
  (sql/delete! db :products {:id id} rs-config))