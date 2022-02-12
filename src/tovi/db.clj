(ns tovi.db
	(:require [next.jdbc :as jdbc]
						[honeysql.helpers :as h]
						[honeysql.core :as honey]
						[next.jdbc.result-set :as result-set]))

(def rs-config {:return-key true
								:builder-fn result-set/as-unqualified-maps})

(defn query [query db]
	(jdbc/execute! db (honey/format query) rs-config))

(defn query-one [query db]
	(jdbc/execute-one! db (honey/format query) rs-config))

(defn insert [values table db]
	(-> (h/insert-into table)
		(h/values values)
		(query-one db)))