(ns tovi.middleware.auth
  (:require [buddy.auth :refer [authenticated?]]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [tovi.utils.auth :refer [backend]]))


;Enables authentication for your ring handler
;set the :identity key of the request to the valid token
(defn wrap-jwt-authenticated [handler]
  (wrap-authentication handler backend))

; Check if the user is authenticated,
; if the request has a valid token in :identity key
(defn wrap-authenticated? [handler]
  (fn [request]
    (if (authenticated? request)
      (handler request)
      {:status 401 :body [{:error-key :401
                           :msg "Unauthorized"}]})))
