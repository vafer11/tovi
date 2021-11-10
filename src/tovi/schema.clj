(ns tovi.schema
	(:require [schema.core :as s]))

(def s-string s/Str)
(def s-int s/Int)
(def errors [s/Str])

(def login-form {:name s/Str :pw s/Str})
(def update-user-form {:name s/Str :last_name s/Str})
(def user-form {:name s/Str :last_name s/Str :email s/Str :pw s/Str :confirm_pw s/Str})

(def user-db {:id s/Int :name s/Str :last_name s/Str :email s/Str})
(def users-db [user-db])

(def user-response {:result s/Keyword :msg s/Str :errors errors :user (s/maybe user-db)})
(def users-response {:result s/Keyword :msg s/Str :errors errors :users users-db})