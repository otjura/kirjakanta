(ns kirjakanta.core 
  (:gen-class :main true)
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as sql]
            [clojure.string :as st]))

(defstruct book :id :title :author :year)

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname "kirjakanta.db"})

(def books-table (sql/create-table-ddl :books [[:id :integer :primary :key :autoincrement]
                                               [:title :text]
                                               [:author :text]
                                               [:year :text]]))

(def allq "SELECT * FROM books")
(def idsq "SELECT id FROM books")
(def titlesq "SELECT title FROM books")
(def authorsq "SELECT author FROM books")
(def yearsq "SELECT year FROM books")
(def idgetq "SELECT * FROM books WHERE id=")

(defn initialize-database []
  (if-not (first (map #(.exists (io/as-file %)) '("./kirjakanta.db")))
    (do (sql/execute! db [books-table])
        (println "Luotiin uusi kirjatietokanta."))
    (do (println "Tietokanta on olemassa."))))

(defn query [query-string]
  (sql/query db query-string))

(defn add-entry ; overloaded
  ([title author year]
   (sql/insert! db :books {:title title :author author :year year}))
  ([title author]
   (sql/insert! db :books {:title title :author author}))
  ([title]
   (sql/insert! db :books {:title title})))

(defn get-by-id [id]
  (query (str idgetq id)))

(defn edit-entry [id what new-value]
  (case what
    1 (sql/update! db :books {:title new-value} ["id = ?" id])
    2 (sql/update! db :books {:author new-value} ["id = ?" id])
    3 (sql/update! db :books {:year new-value} ["id = ?" id])
    "Ei ole olemassa."))

(defn delete-entry [id]
  (sql/delete! db :books ["id = ?" id]))

(defn pprint-sql [query]
  (let [tmp (map (fn [x] (st/replace (str x) #"[{}:\"]" "")) query)]
    (st/join "\n" tmp)))

(defn -main [& args]
  (initialize-database)
  (kirjakanta.ui/kantaview))

