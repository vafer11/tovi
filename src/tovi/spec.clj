(ns tovi.spec
	(:require [clojure.spec.alpha :as s]))

(s/def ::id int?)
(s/def ::name string?)
(s/def ::last_name string?)
(s/def ::email string?)
(s/def ::pw string?)
(s/def ::confirm_pw string?)
(s/def ::token string?)
(s/def ::errors (s/coll-of string?))
(s/def ::result keyword?)
(s/def ::msg string?)
(s/def ::user (s/keys :req-un [::id ::name ::last_name ::email]))
(s/def ::users (s/coll-of ::user))


(s/def ::signin-form (s/keys :req-un [::email ::pw]))
(s/def ::update-user-form (s/keys :req-un [::name ::last_name]))
(s/def ::signup-form (s/keys :req-un [::name ::last_name ::email ::confirm_pw ::pw]))
(s/def ::user-response (s/keys :req-un [::result ::msg ::errors] :opt-un [::user ::token]))
(s/def ::users-response (s/keys :req-un [::result ::msg ::errors] :opt-un [::users ::token]))

