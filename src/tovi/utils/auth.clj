(ns tovi.utils.auth
  (:require [buddy.sign.jwt :as jwt]
            [buddy.auth.backends :as backends]))

(def jwt-secret "*********")
(def backend (backends/jws {:secret jwt-secret}))

(defn get-token [user]
  (let [token (jwt/sign user jwt-secret)]
    (str "Token " token)))
