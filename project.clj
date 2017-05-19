(defproject metrics-tracker "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :creds :gpg}}
  :dependencies [
                 [org.clojure/clojure "1.9.0-alpha16"]
                 [http-kit "2.2.0"]
                 [cheshire "5.7.1"]
                 [com.datomic/datomic-pro "0.9.5561"]
                 ]
  :profiles {
             :dev {:source-paths ["src" "dev"]
                   :dependencies [
                                  [org.clojure/tools.nrepl "0.2.11"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  ]
                   }
             }
  )
