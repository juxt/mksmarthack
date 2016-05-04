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

(defn osgb->latlng [eastings northings]
  (let [crsFac
        (org.geotools.referencing.ReferencingFactoryFinder/getCRSAuthorityFactory "EPSG" nil)
        wgs84crs (.createCoordinateReferenceSystem crsFac "4326")
        osgbCrs (.createCoordinateReferenceSystem crsFac "27700")
        op (.createOperation (new org.geotools.referencing.operation.DefaultCoordinateOperationFactory)
                             osgbCrs wgs84crs)
        pos (new org.geotools.geometry.GeneralDirectPosition eastings northings)
        latlng (.. op getMathTransform (transform pos pos))]

    [(.getOrdinate latlng 0) (.getOrdinate latlng 1)]
    ))

;; (osgb->latlng 488917 251298)


(defn add-latlng [toilet]
  (assoc toilet
         "LatLng"
         (osgb->latlng
          (Long/parseLong (get toilet "GeoX"))
          (Long/parseLong (get toilet "GeoY")))))

(defn get-toilets []
  (let [data (csv/read-csv
              (slurp "http://www.milton-keynes.gov.uk/assets/attach/21191/Public%20Toilets.csv"))
        header (first data)
        rows (rest data)]
    (map add-latlng
     (map #(zipmap header %) rows))))

(defn routes []
  ["/"
   [
    ["toilets" (yada/resource
                {:methods
                 {:get
                  {:produces #{"application/json"
                               "application/edn"
                               "application/transit+json"
                               "application/transit+msgpack"
                               "text/csv"}
                   :response (fn [ctx]
                               (case (yada/content-type ctx)
                                 "text/csv"
                                 (slurp "http://www.milton-keynes.gov.uk/assets/attach/21191/Public%20Toilets.csv")
                                 ("application/json" "application/edn")
                                 (get-toilets)


                                 )
                               )}}}
                )]
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
