(ns tovi.system
	(:require [integrant.core :as ig]
						[ring.adapter.jetty :as jetty]
						[tovi.db.core :as db]
						[tovi.routes.services :as services]))

(def system-config
	{::jetty {:handler (ig/ref ::handler)
						:port 3000}
	 ::handler {:db (ig/ref ::db)}
	 ::db nil})

(defmethod ig/init-key ::jetty [_ {:keys [handler port]}]
	(println "Server running on port 3000")
	(jetty/run-jetty handler {:port port :join? false}))

(defmethod ig/init-key ::handler [_ {:keys [db]}]
	(services/create-app db))

(defmethod ig/init-key ::db [_ _]
	(db/db))

(defmethod ig/halt-key! ::jetty [_ jetty]
	(.stop jetty))

(defn start []
	(ig/init system-config))