(ns tovi.db
	(:require [next.jdbc :as jdbc]
						[next.jdbc.result-set :as result-set]))

(defn query [query db]
	(jdbc/execute! db query {:return-key true
													 :builder-fn result-set/as-unqualified-maps}))

(defn query-one [query db]
	(jdbc/execute-one! db query {:return-keys true
															 :builder-fn result-set/as-unqualified-maps}))
