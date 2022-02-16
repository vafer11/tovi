(ns tovi.spec
	(:require [clojure.spec.alpha :as s]))

(s/def ::id int?)
(s/def ::name string?)
(s/def ::last_name string?)
(s/def ::phone string?)
(s/def ::email string?)
(s/def ::password string?)
(s/def ::new_pw string?)
(s/def ::confirm_pw string?)
(s/def ::recipe_id int?)
(s/def ::description string?)
(s/def ::steps string?)
(s/def ::user_id int?)
(s/def ::ingredient_id int?)
(s/def ::ri_id int?)
(s/def ::unit string?)
(s/def ::quantity int?)
(s/def ::operation string?)
(s/def ::weight int?)
(s/def ::ingredient (s/keys :req-un [::ingredient_id ::unit ::quantity]
											:opt-un [::ri_id ::operation]))

;;:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
;; REQUESTS BODY SPEC
;;:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
(s/def ::signup (s/keys :req-un [::name ::last_name ::email ::password ::confirm_pw]
									:opt-un [::phone]))

(s/def ::signin (s/keys :req-un [::email ::password]))
(s/def ::update-account (s/keys :req-un [::id (or ::name ::last_name ::phone)]))
(s/def ::change-pw (s/keys :req-un [::id ::password ::new_pw ::confirm_pw]))
(s/def ::update-user (s/keys :req-un [(or ::name ::last_name ::phone)]))

(s/def ::create-product (s/keys :req-un [::name ::recipe_id ::unit ::weight]))
(s/def ::update-product (s/keys :req-un [(or ::name ::unit ::weight)]))

(s/def ::create-recipe-ingredient (s/keys :req-un [::ingredient_id ::unit ::quantity]))
(s/def ::ingredients (s/coll-of ::create-recipe-ingredient))
(s/def ::create-recipe (s/keys :req-un [::name ::description ::steps ::user_id]
												 :opt-un [::ingredients]))

(s/def ::update-recipe-ingredient (s/keys :req-un [::ingredient_id ::unit ::quantity]
																		:opt-un [::ri_id ::operation]))
(s/def ::update-ingredients (s/coll-of ::update-recipe-ingredient))
(s/def ::update-recipe (s/keys :req-un [(or ::name ::description ::steps)]
												 :opt-un [::update-ingredients]))

(s/def ::create-ingredient (s/keys :req-un [::name]))
(s/def ::update-ingredient (s/keys :req-un [::name]))