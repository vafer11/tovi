(ns tovi.middleware.middleware)

(def db
	{:name ::db
	 :compile (fn [{:keys [db]} _]
							(fn [handler]
								(fn [req]
									(handler (assoc req :db db)))))})
