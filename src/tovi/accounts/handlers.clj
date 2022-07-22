(ns tovi.accounts.handlers
  (:require [tovi.exceptions :as exc]
            [tovi.utils.auth :as auth]
            [ring.util.response :as rr]
            [tovi.accounts.db :as database]))

(defn signup [{:keys [parameters db]}]
  (try
    (if-let [user (database/signup db (:body parameters))]
      (rr/created "" {:id (:id user) :token (auth/get-token user)})
      (rr/status {:body ["Invalid values"]} 412))
    (catch Exception e
      (exc/handle-exception e))))

(defn signin [{:keys [parameters db]}]
  (try
    (if-let [user (database/signin db (:body parameters))]
      (rr/response {:user user :token (auth/get-token user)})
      (rr/status {:body ["Invalid email or password"]} 412))
    (catch Exception e
      (exc/handle-exception e))))

(defn update-account [{:keys [parameters db]}]
  (try
    (let [result (database/update-account db (:body parameters))]
      (if (not= 0 (:next.jdbc/update-count result))
        (rr/response {:success "User successfully updated"})
        (rr/status {:body ["User could not be updated"]} 412)))
    (catch Exception e
      (exc/handle-exception e))))

(defn change-pw [{:keys [parameters db]}]
  (try
    (if (database/change-pw db (:body parameters))
      (rr/response {:success "Password successfully changed"})
      (rr/status {:body ["The password could not be updated"]} 412))
    (catch Exception e
      (exc/handle-exception e))))

(defn signout [{:keys [db]}]
  {:status 200 :body {:success "Successfully logged out"}})