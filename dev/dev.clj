(ns dev
  (:require
   [reloaded.repl :refer [system init start stop go reset reset-all]]
   [clojure.pprint :refer (pprint)]
   [clojure.reflect :refer (reflect)]
   [clojure.repl :refer (apropos dir doc find-doc pst source)]
   [mksmarthack.system :refer [new-system]]))

(reloaded.repl/set-init! #(new-system {:port 4000}))
