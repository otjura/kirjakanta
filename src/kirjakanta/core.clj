(ns kirjakanta.core 
  (:gen-class)
  (:import [javax.sql ConnectionEvent RowSetEvent StatementEvent]
           [javax.swing JFrame JPanel JLabel JButton])
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as sql]))

(defstruct book :id :title :author :year)

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname "kirjakanta.db"})

(def books-table (sql/create-table-ddl :books [[:id :integer :primary :key :autoincrement]
                                         [:title :text]
                                         [:author :text]
                                         [:year :text]]))

(def query-all "SELECT * FROM books")
(def query-ids "SELECT id FROM books")
(def query-titles "SELECT title FROM books")
(def query-authors "SELECT author FROM books")
(def query-years "SELECT year FROM books")

(defn initialize-database
  []
  (if-not (first (map #(.exists (io/as-file %)) '("./kirjakanta.db")))
    (do (sql/execute! db [books-table])
        (println "Luotiin uusi kirjatietokanta."))
    (do (println "Tietokanta on olemassa."))))

(defn query
  [query]
  (sql/query db query))

(defn add-entry
  ([title author year]
   (sql/insert! db :books {:title title :author author :year year}))
  ([title author]
   (sql/insert! db :books {:title title :author author}))
  ([title]
   (sql/insert! db :books {:title title})))

(defn edit-entry
  [id what new-value]
  (case what
    "title" (sql/update! db :books {:id id :title new-value})
    "author" (sql/update! db :books {:id id :author new-value})
    "year" (sql/update! db :books {:id id :year new-value})
    "Ei ole olemassa."))

(defn delete-entry
  [id]
  (sql/delete! db :books ["id = ?" id]))



(defn -main [& args]
  (initialize-database))
