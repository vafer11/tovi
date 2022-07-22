(ns tovi.utils)

(defn get-timestamp [] 
  (-> (java.util.Date.)
      (.getTime)
      (java.sql.Timestamp.)))

(defn dissoc-values [obj keys] 
  (reduce dissoc obj keys))