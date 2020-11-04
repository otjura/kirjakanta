(defproject kirjakanta "0.15"
  :description "Yksinkertainen kirjatietokanta"
  :url "https://github.com/otjura/kirjakanta"
  :license {:name "The MIT License (MIT)"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.xerial/sqlite-jdbc "3.8.11.2"]]
  :main kirjakanta.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
