(ns tovi.middleware.middleware
	(:require [buddy.auth :refer [authenticated?]]))

(def db
	{:name ::db
	 :compile (fn [{:keys [db]} _]
							(fn [handler]
								(fn [req]
									(handler (assoc req :db db)))))})