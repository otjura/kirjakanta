(ns kirjakanta.core
  (:require [kirjakanta.ui] [kirjakanta.db])
  (:gen-class :main true))

(defn -main [& args]
  (kirjakanta.db/initialize-database)
  (kirjakanta.ui/kantaview))

