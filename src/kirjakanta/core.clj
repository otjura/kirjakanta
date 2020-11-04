(ns kirjakanta.core
  (:require [kirjakanta.ui :as ui]
            [kirjakanta.db :as db])
  (:gen-class :main true))

(defn -main []
  (db/initialize-database)
  (ui/kantaview))

