(ns tovi.accounts.routes
  (:require [tovi.spec :as s]
            [tovi.responses :as response]
            [tovi.accounts.handlers :as handler]
            [tovi.middleware.auth :refer [wrap-authenticated?]]))

(def account-routes
  ["/account"
   {:swagger {:tags ["account"]}}
   ["/signup"
    {:post {:summary "Sign up a new account"
            :middleware [wrap-authenticated?]
            :parameters {:body ::s/signup}
            :responses {201 {:body response/signup} 412 {:body response/errors}}
            :handler handler/signup}}]
   ["/signin"
    {:post {:summary "Sign in into your account with your user and password"
            :middleware [wrap-authenticated?]
            :parameters {:body ::s/signin}
            :responses {200 {:body response/signin} 412 {:body response/errors}}
            :handler handler/signin}}]
   ["/update"
    {:put {:summary "Update your account"
           :middleware [wrap-authenticated?]
           :parameters {:header {:authorization string?} :body ::s/update-account}
           :responses {200 {:body response/success} 412 {:body response/errors}}
           :handler handler/update-account}}]
   ["/changepw"
    {:put {:summary "Change your password account"
           :middleware [wrap-authenticated?]
           :parameters {:header {:authorization string?} :body ::s/change-pw}
           :responses {200 {:body response/success} 412 {:body response/errors}}
           :handler handler/change-pw}}]

   ["/signout"
    {:put {:summary "Sign out from your account"
           :middleware [wrap-authenticated?]
           :parameters {:header {:authorization string?}}
           :handler handler/signout}}]])