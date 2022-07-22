(ns tovi.system
  (:require
   [next.jdbc :as jdbc]
   [integrant.core :as ig]
   [tovi.config :as config]
   [tovi.services :as services]
   [ring.adapter.jetty :as jetty]))

(def system-config
  {::jetty {:handler (ig/ref ::handler) :port config/port}
   ::handler {:db (ig/ref ::db)}
   ::db {:db-config config/db}})

(defmethod ig/init-key ::jetty [_ {:keys [handler port]}]
  (println "Server running on port" port)
  (jetty/run-jetty handler {:port port :join? false}))

(defmethod ig/init-key ::handler [_ {:keys [db]}]
  (services/create-app db))

(defmethod ig/init-key ::db [_ {:keys [db-config]}]
  (jdbc/get-datasource db-config))

(defmethod ig/halt-key! ::jetty [_ jetty]
  (.stop jetty))

(defn start [] (ig/init system-config))