(ns venia.spec
  (:require #?(:clj [clojure.spec :as s]
               :cljs [cljs.spec :as s])))

(s/def :venia/query-name keyword?)
(s/def :venia/fields (s/coll-of (s/or :venia/field keyword?
                                      :venia/nested-field (s/cat :venia/nested-field-root keyword? :venia/nested-field-children :venia/fields))))
(s/def :venia/args (s/keys :opt [:venia/alias]))
(s/def :venia/query (s/cat :query :venia/query-name :args (s/? :venia/args) :fields :venia/fields))
(s/def :venia/query-with-fragment (s/cat :query :venia/query-name :args (s/? :venia/args) :fragment-name keyword?))
(s/def :venia/alias keyword?)

(s/def :fragment/name keyword?)
(s/def :fragment/type keyword?)
(s/def :fragment/fields :venia/fields)
(s/def :venia/fragment (s/keys :req [:fragment/name :fragment/type :fragment/fields]))


(s/def :venia/advanced-query (s/keys :req []
                                     :opt [:venia/query :venia/query-with-fragment
                                           :venia/alias :venia/fragment]))

(s/def :venia/query-def (s/coll-of (s/or :venia/query-vector (s/coll-of :venia/query)
                                         :venia/query-map (s/coll-of :venia/advanced-query))
                                   :min-count 1))

(defn query->spec [query]
  (s/conform :venia/query-def query))