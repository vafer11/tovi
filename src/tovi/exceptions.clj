(ns tovi.exceptions
	(:require [clojure.tools.logging :as log]
						[ring.util.response :as rr]))

;:::::::::::::: Check unique constraint exception ::::::::::::::;
(defn unique-constraint-exception? [msg constraint]
	(if (-> msg (.startsWith (format
														 "ERROR: duplicate key value violates unique constraint \"%s\""
														 constraint)))
		true
		false))

;:::::::::::::: Check foreign constraint exception ::::::::::::::;
(defn foreign-constraint-exception? [msg action table constraint]
	(if (-> msg (.startsWith (format
														 "ERROR: %s on table \"%s\" violates foreign key constraint \"%s\""
														 action table constraint)))
		true
		false))

(defn dispatch-handle-exception [exc]
	(log/error exc)
	(if-let [sql-exception? (instance? java.sql.SQLException exc)]
		(let [msg (-> exc (.getMessage))]
			(cond
				(unique-constraint-exception? msg "users_email_key") :users_email_key
				(unique-constraint-exception? msg "users_phone_key") :users_phone_key
				(foreign-constraint-exception? msg "insert or update" "products" "fk_products_recipe_id") :insert_fk_products_recipe_id
				(foreign-constraint-exception? msg  "update or delete" "recipes" "fk_products_recipe_id") :delete_fk_products_recipe_id
				:else :default-exception))
		(rr/status {:body ["Server error occurred"]} 500)))

(defmulti handle-exception dispatch-handle-exception)

(defmethod handle-exception :users_email_key [_]
	(rr/status {:body ["User with the selected email already exists"]} 412))

(defmethod handle-exception :users_phone_key [_]
	(rr/status {:body ["User with the selected phone already exists"]} 412))

(defmethod handle-exception :insert_fk_products_recipe_id [_]
	(rr/status {:body ["The recipe id does not exit"]} 412))

(defmethod handle-exception :delete_fk_products_recipe_id [_]
	(rr/status {:body ["This recipe can not be deleted, cause products reference it"]} 412))

(defmethod handle-exception :default-exception [_]
	(rr/status {:body ["Server error occurred"]} 500))