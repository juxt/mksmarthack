(ns mksmarthack.core
  (:require
   [aleph.http :as http]
   [bidi.vhosts :refer [vhosts-model make-handler]]
   [byte-streams :as b]
   [cheshire.core :as json]
   [clojure.data.csv :as csv]
   [clojure.tools.logging :refer :all]
   [com.stuartsierra.component :refer [Lifecycle using]]
   [hiccup.core :refer [html]]
   [schema.core :as s]
   [yada.yada :as yada]
   ))

(defn routes []
  ["/"
   [
    ["index.html" (yada/resource
                   {:id ::index
                    :methods
                    {:get
                     {:response (fn [ctx]
                                  (html
                                   [:h1 "Welcome to MKJUG"]))
                      :produces "text/html"}}})]
    ["" (yada/redirect ::index)]]])

(s/defrecord Listener [port :- s/Int]
    Lifecycle
    (start [component]

      (let [vhosts-model
            (vhosts-model
             [(str "http://localhost:" port) (routes)])

            listener (yada/listener vhosts-model {:port port})]

        (infof "Starting web listener on port %s" port)
        (assoc component :listener listener)))

    (stop [component]
      (when-let [close (-> component :listener :close)]
        (close))
      (dissoc component :listener)))

(defn new-listener []
  (using
   (map->Listener {})
   []))
