(ns mksmarthack.listener
  (:require
   [com.stuartsierra.component :refer [Lifecycle using]]
   [clojure.tools.logging :refer :all]
   [bidi.vhosts :refer [vhosts-model make-handler]]
   [schema.core :as s]
   [yada.yada :as yada]))

(s/defrecord Listener []
    Lifecycle
    (start [component]

      (let [vhosts-model
            (vhosts-model
             ["http://localhost:4000" ["/" (yada/handler "Hi")]])

            listener (yada/listener vhosts-model {:port 4000})]

        (infof "Starting web listener on port %s" 4000)
        (assoc component :listener listener)))

    (stop [component]
      (when-let [close (-> component :listener :close)]
        (close))
      (dissoc component :listener)))

(defn new-listener []
  (using
   (map->Listener {})
   []))
