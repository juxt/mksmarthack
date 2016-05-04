(defproject mksmarthack "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[aero "1.0.0-beta2"]
                 [bidi "2.0.9"]
                 [ch.qos.logback/logback-classic "1.1.5" :exclusions [org.slf4j/slf4j-api]]
                 [cheshire "5.5.0"]
                 [com.stuartsierra/component "0.3.1"]
                 [hiccup "1.0.5"]
                 [org.clojure/data.csv "0.1.3"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/jcl-over-slf4j "1.7.18"]
                 [org.slf4j/jul-to-slf4j "1.7.18"]
                 [org.slf4j/log4j-over-slf4j "1.7.18"]
                 [prismatic/schema "1.0.5"]
                 [reloaded.repl "0.2.1"]
                 [yada "1.1.11" :exclusions [bidi]]
                 [aleph "0.4.1-beta5"]
                 [org.geotools/gt-main "14.3"]
                 [org.geotools/gt-epsg-hsql "14.3"]
                 [org.geotools/gt-xml "14.3"]
                 #_[org.geotools/gt-main "2.7.0.1"]]

  :repositories [["geotools" "http://download.osgeo.org/webdav/geotools"]]

  :profiles {:dev
             {:dependencies
              [[org.clojure/clojure "1.8.0"]]
              :source-paths ["dev"]
              }})
