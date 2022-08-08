(defproject tovi "0.1.0"
  :description "Rest full api"
  :url "https://github.com/vafer11/tovi"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.logging "1.2.4"]
                 [metosin/reitit "0.5.15"]
                 [metosin/malli "0.8.9"]
                 [ring/ring-jetty-adapter "1.9.4"]
                 [ring "1.9.4"]
                 [ring-cors "0.1.13"]
                 [integrant "0.8.0"]
                 [integrant/repl "0.3.2"]
                 [buddy/buddy-hashers "1.8.1"]
                 [buddy/buddy-auth "3.0.1"]
                 [buddy/buddy-sign "3.4.1"]
                 [honeysql "1.0.461"]
                 [org.postgresql/postgresql "42.1.3"]
                 [com.github.seancorfield/next.jdbc "1.2.737"]
                 [environ "1.2.0"]]
  :main tovi.core/-main
  :repl-options {:init-ns user}
  :profiles {:default [:base :system :user :provided :dev :local-dev]
             :dev {:main user
                   :source-paths ["dev"]
                   :plugins [[lein-environ "1.2.0"]]}})
