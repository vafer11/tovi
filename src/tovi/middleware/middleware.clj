(ns tovi.middleware.middleware
  (:require
   [buddy.auth :refer [authenticated?]]
   [ring.middleware.cors :refer [wrap-cors]]))

(def db
  {:name ::db
   :compile (fn [{:keys [db]} _]
              (fn [handler]
                (fn [req]
                  (handler (assoc req :db db)))))})

(def cors
  {:name ::wrap-cors
   :compile (fn [_ _]
              (fn [handler]
                (wrap-cors
                 handler
                 :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :put :post :delete])))})