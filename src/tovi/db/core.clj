(ns tovi.db.core
	(:require [next.jdbc :as jdbc]
						[next.jdbc.result-set :as result-set]))

(def db-config {:dbtype "postgresql"
								:dbname "******"
								:host "localhost"
								:user "******"
								:password "******"})

(defn db []
	(jdbc/get-datasource db-config))

(defn db-query [query]
	(jdbc/execute! (db) query {:return-key true
														 :builder-fn result-set/as-unqualified-maps}))

(defn db-query-one [query]
	(jdbc/execute-one! (db) query {:return-keys true
																 :builder-fn result-set/as-unqualified-maps}))

