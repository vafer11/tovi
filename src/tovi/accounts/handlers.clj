(ns tovi.accounts.handlers
  (:require [tovi.exceptions :as exc]
            [tovi.utils.auth :as auth]
            [ring.util.response :as rr]
            [tovi.accounts.db :as database]))

(defn signup [{:keys [parameters db]}]
  (try
    (if-let [user (database/signup db (:body parameters))]
      (rr/created "" {:id (:id user)
                      :name (:name user)
                      :last_name (:last_name user)
                      :email (:email user)
                      :token (auth/get-token user)})
      (rr/status {:body [{:error-key :412 :msg "Invalid values"}]} 412))
    (catch Exception e
      (exc/handle-exception e))))

(defn signin [{:keys [parameters db]}]
  (try
    (if-let [user (database/signin db (:body parameters))]
      (rr/response (assoc user :token (auth/get-token user)))
      (rr/status {:body [{:error-key :412 :msg "Invalid email or password"}]} 412))
    (catch Exception e
      (exc/handle-exception e))))

(defn update-account [{:keys [parameters db]}]
  (try
    (let [result (database/update-account db (:body parameters))]
      (if (not= 0 (:next.jdbc/update-count result))
        (rr/response {:success "User successfully updated"})
        (rr/status {:body [{:error-key :412 :msg "User could not be updated"}]} 412)))
    (catch Exception e
      (exc/handle-exception e))))

(defn change-pw [{:keys [parameters db]}]
  (try
    (if (database/change-pw db (:body parameters))
      (rr/response {:success "Password successfully updated"})
      (rr/status {:body [{:error-key :412 :msg "The password could not be updated"}]} 412))
    (catch Exception e
      (exc/handle-exception e))))

(defn signout [{:keys [db]}]
  {:status 200 :body {:success "Successfully logged out"}})