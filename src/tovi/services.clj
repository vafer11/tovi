(ns tovi.services
  (:require
   [reitit.coercion.malli]
   [reitit.ring.malli]
   [malli.util :as mu]
   [reitit.ring :as ring]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.exception :as exception] 
   [reitit.ring.coercion :as coercion]
   [reitit.dev.pretty :as pretty]
   [muuntaja.core :as m]
   [tovi.middleware.middleware :as middleware]
   [tovi.middleware.auth :refer [wrap-jwt-authenticated]]
   [tovi.accounts.routes :refer [account-routes]]
   [tovi.admin.routes :refer [admin-routes]]
   [tovi.orders.routes :refer [product-routes]]
   [tovi.recipes.routes :refer [recipe-routes ingredient-routes]]))

(def routes
  [["/swagger.json"
    {:get {:no-doc true
           :swagger {:info {:title "Tovi API Reference"
                            :description "The Tovi API is organized around REST."
                            :version "1.0.0"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/api/v1"
    account-routes
    admin-routes
    ingredient-routes
    recipe-routes
    product-routes]])

(defn router-config [db]
  {:exception pretty/exception
   :data      {:db db
               :securityDefinitions {:api_key {:type "apiKey"
                                               :name "Authorization"
                                               :in "header"}}
               :coercion (reitit.coercion.malli/create
                          {;; set of keys to include in error messages
                           :error-keys #{#_:type :coercion :in :schema :value :errors :humanized #_:transformed}
                           ;; schema identity function (default: close all map schemas)
                           :compile mu/closed-schema
                           ;; strip-extra-keys (effects only predefined transformers)
                           :strip-extra-keys true
                           ;; add/set default values
                           :default-values true
                           ;; malli options
                           :options nil})
               :muuntaja   m/instance
               :middleware [swagger/swagger-feature
                            muuntaja/format-negotiate-middleware
                            muuntaja/format-response-middleware
                            exception/exception-middleware
                            muuntaja/format-request-middleware
                            coercion/coerce-request-middleware
                            coercion/coerce-response-middleware 
                            wrap-jwt-authenticated
                            ; The first middleware to be executed
                            middleware/db]}})

(defn create-app [db]
  (ring/ring-handler
  (-> routes (ring/router (router-config db)))
   (ring/routes
    (swagger-ui/create-swagger-ui-handler {:path "/"}))
   {:middleware [middleware/cors]}))