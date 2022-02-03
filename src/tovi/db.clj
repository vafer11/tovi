(ns tovi.db
	(:require [next.jdbc :as jdbc]
						[honeysql.core :as honey]
						[next.jdbc.result-set :as result-set]
						[honeysql.helpers :as h]))

(defn query [query db]
	(jdbc/execute! db query {:return-key true
													 :builder-fn result-set/as-unqualified-maps}))

(defn query-one [query db]
	(jdbc/execute-one! db query {:return-keys true
															 :builder-fn result-set/as-unqualified-maps}))

(defn insert [db table values]
	(-> (h/insert-into table)
		(h/values [values])
		honey/format
		(query-one db)))

(defn select [db table]
	(-> (h/select :*)
		(h/from table)
		honey/format
		(query db)))

(defn update-by [db table values condition]
	(-> (h/update table)
		(h/set0 values)
		(h/where condition)
		honey/format
		(query-one db)))

(defn select-by [db table condition]
	(-> (h/select :*)
		(h/from table)
		(h/where condition)
		honey/format
		(query-one db)))

(defn delete-by [db table condition]
	(-> (h/delete-from table)
		(h/where condition)
		honey/format
		(query-one db)))