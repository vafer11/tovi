(ns tovi.routes.services
	(:require [reitit.ring :as ring]
						[reitit.swagger :as swagger]
						[reitit.swagger-ui :as swagger-ui]
						[reitit.ring.middleware.muuntaja :as muuntaja]
						[reitit.ring.middleware.exception :as exception]
						[reitit.coercion.schema]
						[reitit.ring.coercion :as coercion]
						[reitit.dev.pretty :as pretty]
						[muuntaja.core :as m]
						[tovi.middleware.middleware :as middleware]
						[tovi.routes.auth :refer [auth-routes]]
						[tovi.routes.users :refer [users-routes]]))


(def routes
	[["/swagger.json"
		{:get {:no-doc true
					 :swagger {:info {:title "Comment System API"}}
					 :handler (swagger/create-swagger-handler)}}]
	 ["/api"
		auth-routes
		users-routes]])

(defn create-app [db]
	(ring/ring-handler
		(ring/router routes
			{:exception pretty/exception
			 :data      {:db db
									 :coercion   reitit.coercion.schema/coercion
									 :muuntaja   m/instance
									 :middleware [swagger/swagger-feature
																muuntaja/format-negotiate-middleware
																muuntaja/format-response-middleware
																exception/exception-middleware
																muuntaja/format-request-middleware
																coercion/coerce-request-middleware
																coercion/coerce-response-middleware
																; The first middleware to be executed
																middleware/db]}})
		(ring/routes
			(swagger-ui/create-swagger-ui-handler
				{:path "/"}))))