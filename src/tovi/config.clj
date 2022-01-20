(ns tovi.config
	(:require [environ.core :refer [env]]))

(def port (read-string (:port env)))
(def db (:db env))
(def base-url (:base-url env))
(def logs-path (:logs-path env))
(def jwt-secret (:jwt-secret env))