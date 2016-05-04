(ns mksmarthack.system
  (:require
   [aero.core :as aero]
   [clojure.java.io :as io]
   [mksmarthack.core :refer [new-listener]]
   [com.stuartsierra.component :refer [system-map system-using]]))

(defn configure [system profile]
  (->>
   (aero/read-config (io/resource "config.edn") {:profile profile})
   (merge-with merge system)))

(defn new-system-map []
  (system-map
   :listener (new-listener)))

(defn new-dependency-map []
  {})

(defn new-system
  "Create the production system"
  [profile]
  (system-using (configure (new-system-map) profile) (new-dependency-map)))
