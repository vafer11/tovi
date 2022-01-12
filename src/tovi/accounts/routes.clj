(ns tovi.accounts.routes
	(:require [tovi.accounts.handlers :as handler]
						[tovi.spec :as s]))

(def account-routes
	["/account"
	 {:swagger {:tags ["account"]}}
	 ["/signin"
		{:post {:summary "Sign in into your account with your user and password"
						:parameters {:body ::s/signin-form}
						:responses {200 {:body ::s/user-response}}
						:handler handler/signin}}]
	 ["/signup"
		{:post {:summary "Sign up a new account"
						:parameters {:body ::s/signup-form}
						:responses {200 {:body ::s/user-response}}
						:handler handler/signup}}]
	 ["/signout"
		{:post {:summary "Sign out from your account"
						:handler handler/signout}}]])