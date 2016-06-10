(ns kirjakanta.core 
  (:gen-class)
  (:import (javax.sql ConnectionEvent RowSetEvent StatementEvent))
  (:require [clojure.java.jdbc :as jdbc]))

(def database {:classname "org.sqlite.JDBC"
               :subprotocol "sqlite"
               :subname ".db"})

(defstruct book :title :author :year)

(defn -main [& args]
  ())


