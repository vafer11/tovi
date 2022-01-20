(ns tovi.exceptions
	(:require [clojure.tools.logging :as log]
						[ring.util.response :as rr]))

(defn handle-signup-exception [exception]
	(log/error exception "Sign up exception:")
	(if (and (instance? java.sql.SQLException exception)
				   (-> exception
						 (.getMessage)
						 (.startsWith "ERROR: duplicate key value violates unique constraint")))
		(rr/status {:body ["User with the selected email already exists"]} 412)
		(rr/status {:body ["Server error occurred when signin up"]} 500)))

(defn handle-exception [exception]
	(log/error exception)
	(rr/status {:body ["Server error occurred when signin up"]} 500))
