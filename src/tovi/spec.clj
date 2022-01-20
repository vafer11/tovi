(ns tovi.spec
	(:require [clojure.spec.alpha :as s]))

(s/def ::id int?)
(s/def ::name string?)
(s/def ::last-name string?)
(s/def ::email string?)
(s/def ::pw string?)
(s/def ::new-pw string?)
(s/def ::confirm-pw string?)
(s/def ::recipe-id int?)

;;:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
;; REQUESTS BODY SPEC
;;:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
(s/def ::signup (s/keys :req-un [::name ::last-name ::email ::pw ::confirm-pw]))
(s/def ::signin (s/keys :req-un [::email ::pw]))
(s/def ::update-account (s/keys :req-un [::id ::name ::last-name]))
(s/def ::change-pw (s/keys :req-un [::id ::pw ::new-pw ::confirm-pw]))
(s/def ::update-user (s/keys :req-un [::name ::last-name]))
(s/def ::create-product (s/keys :req-un [::name ::recipe-id]))