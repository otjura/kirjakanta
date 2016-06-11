(ns kirjakanta.core 
  (:gen-class)
  (:import (javax.sql ConnectionEvent RowSetEvent StatementEvent))
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]))

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname "kirjakanta.db"})

(def table (jdbc/create-table-ddl :books [[:id :integer :primary :key :autoincrement]
                                          [:title :text]
                                          [:author :text]
                                          [:year :text]]))

(defstruct book :title :author :year)

(defn -main [& args]
  (initialize-database))

(defn initialize-database []
  (if-not (first (map #(.exists (io/as-file %)) '("./kirjakanta.db")))
    (do (jdbc/execute! db [table])
        (println "Luotiin uusi kirjatietokanta."))
    (do (println "Tietokanta on olemassa."))))
