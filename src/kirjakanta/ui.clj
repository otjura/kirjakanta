(ns kirjakanta.ui
  (:gen-class)
  (:require [kirjakanta.db :as db]
            [clojure.pprint :as pprint])
  (:import [javax.swing JFrame JPanel JLabel JButton JTextField JTextArea]
           [java.awt.event ActionListener]
           [java.awt GridLayout]))

(declare print-entries add-entry delete-entry edit-entry reset-fields)

(def idstr "ID")
(def titlestr "OTSIKKO")
(def authorstr "TEKIJÄ")
(def yearstr "VUOSI")
(def greetingstr "Tervetuloa Kirjakantaan!")
(def ok-button (JButton. "Valmis"))
(def result-label (JLabel. "Tulos:"))
(def result-field (JTextArea. greetingstr 10 40)) ; rows cols
(def id-label (JLabel. idstr))
(def id-field (JTextField. "" 20)) ; default-txt cols
(def title-label (JLabel. titlestr))
(def title-field (JTextField. "" 20))
(def author-label (JLabel. authorstr))
(def author-field (JTextField. "" 20))
(def year-label (JLabel. yearstr))
(def year-field (JTextField. "" 20))
(def query-button (doto (JButton. "Kysely"); JButton buttonque = new JButton("Kysely")
                    (.addActionListener (proxy [ActionListener] []
                                          (actionPerformed [e]
                                            (print-entries))))))
(def add-button (doto (JButton. "Lisää")
                  (.addActionListener (proxy [ActionListener] []
                                        (actionPerformed [e]
                                          (add-entry))))))

(def delete-button (doto (JButton. "Poista")
                     (.addActionListener (proxy [ActionListener] []
                                           (actionPerformed [e]
                                             (delete-entry))))))

(def edit-button (doto (JButton. "Muokkaa")
                   (.addActionListener (proxy [ActionListener] []
                                         (actionPerformed [e]
                                           (edit-entry))))))

; Specifying the number of columns affects the layout only when the number of rows is set to zero
(def input-panel (doto (JPanel. (new GridLayout 0 2 10 10))
                   (.add id-label)
                   (.add id-field)
                   (.add title-label)
                   (.add title-field)
                   (.add author-label)
                   (.add author-field)
                   (.add year-label)
                   (.add year-field)))

(def control-panel (doto (JPanel. (new GridLayout 0 4 10 10))
                     (.add query-button)
                     (.add add-button)
                     (.add delete-button)
                     (.add edit-button)
                     (.add ok-button)))

(def view-panel (doto (JPanel.)
                  (.add result-label)
                  (.add result-field)
                  (.add input-panel)
                  (.add control-panel)))

(defn kantaview []
  (doto (JFrame. "Kirjakanta")
    (.setContentPane view-panel)
    (.setSize 640 400) ; golden ratio is 640 400
    (.setResizable true)
    (.setDefaultCloseOperation (JFrame/EXIT_ON_CLOSE))
    (.setVisible true)))

(defn print-entries []
  (.setText result-field (db/pprint-sql (db/query db/allq))))

(defn add-entry []
  (let [title (.getText title-field)
        author (.getText author-field)
        year (.getText year-field)]
    (if-not (and (= title titlestr) (= author authorstr) (= year yearstr))
      (do (db/add-entry title author year)
          (reset-fields)
          (print-entries))
      (if-not (and (= title titlestr) (= author authorstr))
        (do (db/add-entry title author)
            (reset-fields)
            (print-entries))
        (if-not (and (= title titlestr))
          (do (db/add-entry title)
              (reset-fields)
              (print-entries))
          (.setText result-field "Anna ainakin kirjan nimi!"))))))

(defn delete-entry []
  (let [id (.getText id-field)]
    (if-not (= id idstr)
      (do (db/delete-entry id)
          (reset-fields)
          (print-entries))
      (.setText result-field "Anna teoksen ID jonka haluat poistaa!"))))

(defn edit-entry []
  (let [id (.getText id-field)]
    (if (= id idstr)
      (.setText result-field (pprint/cl-format nil "Anna ID ja muokkaa haluamiasi kenttiä alla!~%Muokattuasi tietoja paina Valmis-painiketta tallentaaksesi!"))
      (let [info (first (db/get-by-id id))
            id2 (str (first info))
            title (str (first (rest info)))
            author (str (first (rest (rest info))))
            year (str (first (rest (rest (rest info)))))]
        (.setText id-field id2)
        (.setText title-field title)
        (.setText author-field author)
        (.setText year-field year)
        (.addActionListener
         ok-button
         (proxy [ActionListener] []
           (actionPerformed [e] ;TODO FIX gets old values for some reason on second OK
             (do (if-not (or (= title (.getText title-field)) (= title titlestr))
                   (db/edit-entry id 1 (.getText title-field)))
                 (if-not (or (= author (.getText author-field)) (= author authorstr))
                   (db/edit-entry id 2 (.getText author-field)))
                 (if-not (or (= year (.getText year-field)) (= year yearstr))
                   (db/edit-entry id 3 (.getText year-field)))
                 (reset-fields)
                 (print-entries)))))))))

(defn reset-fields []
  (.setText id-field "")
  (.setText title-field "")
  (.setText author-field "")
  (.setText year-field ""))
