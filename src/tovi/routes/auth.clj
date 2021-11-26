(ns tovi.routes.auth
	(:require [tovi.handlers.auth :as auth]
						[tovi.spec :as s]))

(def auth-routes
	["/auth"
	 {:swagger {:tags ["users"]}}
	 ["/signin"
		{:post {:summary "Sign in into your account with your user and password"
						:parameters {:body ::s/signin-form}
						:responses {200 {:body ::s/user-response}}
						:handler auth/signin}}]
	 ["/signup"
		{:post {:summary "Sign up a new account"
						:parameters {:body ::s/signup-form}
						:responses {200 {:body ::s/user-response}}
						:handler auth/signup}}]
	 ["/signout"
		{:post {:summary "Sign out from your account"
						:handler auth/signout}}]])