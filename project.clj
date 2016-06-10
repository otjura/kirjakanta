(defproject kirjakanta "0.1.0-SNAPSHOT"
  :description "Simple book database"
  :url "https://github.com/otjura/kirjakanta"
  :license {:name "The MIT License (MIT)"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.6.1"]]
  :main ^:skip-aot kirjakanta.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
