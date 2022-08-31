(ns tovi.accounts.routes
  (:require [tovi.accounts.schema :as schema]
            [tovi.responses :as response]
            [tovi.accounts.handlers :as handler] 
            [tovi.middleware.auth :refer [wrap-authenticated?]]))

(def account-routes
  ["/account"
   {:swagger {:tags ["account"]}}
   ["/signup"
    {:post {:summary "Sign up a new account"
            :parameters {:body schema/signup-request}
            :responses {201 {:body schema/signup-response} 412 {:body response/error}}
            :handler handler/signup}}]
   ["/signin"
    {:post {:summary "Sign in into your account with your email and password"
            :parameters {:body schema/signin-request}
            :responses {200 {:body schema/signup-response} 412 {:body response/error}}
            :handler handler/signin}}]
   ["/update"
    {:put {:summary "Update your account"
           :middleware [wrap-authenticated?]
           :parameters {:header [:map [:authorization :string]] :body schema/update-account-request}
           :responses {200 {:body response/success} 412 {:body response/errors}}
           :handler handler/update-account}}]
   ["/changepw"
    {:put {:summary "Change your password account"
           :middleware [wrap-authenticated?]
           :parameters {:header [:map [:authorization :string]] :body schema/change-pw-request}
           :responses {200 {:body response/success} 412 {:body response/errors}}
           :handler handler/change-pw}}]
   ["/signout"
    {:put {:summary "Sign out from your account"
           :middleware [wrap-authenticated?]
           :parameters {:header [:map [:authorization :string]]}
           :handler handler/signout}}]])