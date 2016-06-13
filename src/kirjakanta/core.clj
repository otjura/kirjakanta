(ns kirjakanta.core 
  (:gen-class)
  (:import (javax.sql ConnectionEvent RowSetEvent StatementEvent)
           (javax.swing JFrame JPanel JLabel JButton)
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as sql]))

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname "kirjakanta.db"})

(def table (sql/create-table-ddl :books [[:id :integer :primary :key :autoincrement]
                                         [:title :text]
                                         [:author :text]
                                         [:year :text]]))

(def query-all "select * from books")

(def query-titles "select title from books")

(def query-authors "select author from books")

(def query-years "select year from books")

(defstruct book :title :author :year)

(defn -main [& args]
  (initialize-database))

(defn initialize-database []
  (if-not (first (map #(.exists (io/as-file %)) '("./kirjakanta.db")))
    (do (sql/execute! db [table])
        (println "Luotiin uusi kirjatietokanta."))
    (do (println "Tietokanta on olemassa."))))

(defn add-entry
  ([title author year]
   (sql/insert! db :books {:title title :author author :year year}))
  ([title author]
   (sql/insert! db :books {:title title :author author}))
  ([title]
   (sql/insert! db :books {:title title})))

(defn edit-entry [id what new-value]
  (case what
    "title" (sql/update! db :books {:id id :title new-value})
    "author" (sql/update! db :books {:id id :author new-value})
    "year" (sql/update! db :books {:id id :year new-value})
    "Ei ole olemassa."))
