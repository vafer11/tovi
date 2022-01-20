(ns tovi.responses
	(:require [spec-tools.data-spec :as ds]))

(def user
	{:id int?
	 :name string?
	 :last_name string?
	 :email string?})

(def signup {:id int? :token string?})
(def signin {:user user :token string?})
(def create-user {:user user})
(def users [user])
(def errors [string?])
(def success {:success string?})