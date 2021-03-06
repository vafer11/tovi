(ns tovi.responses
  (:require [spec-tools.data-spec :as ds]))

(def user
  {:id int?
   :name string?
   :last_name string?
   :email string?})

(def product
  {:id int?
   :name string?
   :recipe_id int?
   :unit string?
   :weight int?})

(def recipe
  {:id int?
   :name string?
   :description string?
   :steps string?
   :user_id int?})

(def ingredient
  {:id int?
   :name string?})

(def signup {:id int? :token string?})
(def signin {:user user :token string?})
(def create-user {:user user})
(def users [user])
(def errors [string?])
(def success {:success string?})
(def products [product])
(def recipes [recipe])
(def ingredients [ingredient])