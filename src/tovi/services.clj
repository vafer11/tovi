(ns tovi.services
	(:require [reitit.ring :as ring]
		[reitit.swagger :as swagger]
		[reitit.swagger-ui :as swagger-ui]
		[reitit.ring.middleware.muuntaja :as muuntaja]
		[reitit.ring.middleware.exception :as exception]
		[reitit.coercion.spec]
		[reitit.ring.coercion :as coercion]
		[reitit.dev.pretty :as pretty]
		[muuntaja.core :as m]
		[tovi.middleware.middleware :as middleware]
		[tovi.middleware.auth :refer [wrap-jwt-authenticated]]
		[tovi.accounts.routes :refer [account-routes]]
		[tovi.admin.routes :refer [admin-routes]]))

(def routes
	[["/swagger.json"
		{:get {:no-doc true
					 :swagger {:info {:title "Comment System API"}}
					 :handler (swagger/create-swagger-handler)}}]
	 ["/api"
		account-routes
		admin-routes]])

(defn create-app [db]
	(ring/ring-handler
		(ring/router routes
			{:exception pretty/exception
			 :data      {:db db
									 :securityDefinitions {:api_key {:type "apiKey"
																									 :name "Authorization"
																									 :in "header"}}
									 :coercion   reitit.coercion.spec/coercion
									 :muuntaja   m/instance
									 :middleware [swagger/swagger-feature
																muuntaja/format-negotiate-middleware
																muuntaja/format-response-middleware
																exception/exception-middleware
																muuntaja/format-request-middleware
																coercion/coerce-request-middleware
																coercion/coerce-response-middleware
																; The first middleware to be executed
																wrap-jwt-authenticated
																middleware/db]}})
		(ring/routes
			(swagger-ui/create-swagger-ui-handler
				{:path "/"}))))