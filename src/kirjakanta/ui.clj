(ns kirjakanta.ui
  (:gen-class)
  (:require [kirjakanta.db :as db]
            [clojure.pprint :as pprint])
  (:import [javax.swing JFrame JPanel JLabel JButton JTextField JTextArea]
           [java.awt.event ActionListener]))

(def idstr "ID")
(def titlestr "OTSIKKO")
(def authorstr "TEKIJÄ")
(def yearstr "VUOSI")
 
(defn kantaview []
  (declare print-entries add-entry delete-entry edit-entry reset-fields); makes funcalls in let work
  (let [query-button (doto (JButton. "Kysely"); JButton buttonque = new JButton("Kysely")
                       (.addActionListener (proxy [ActionListener] []
                                             (actionPerformed [e]
                                               (print-entries)))))
        add-button (doto (JButton. "Lisää")
                     (.addActionListener (proxy [ActionListener] []
                                                      (actionPerformed [e]
                                                        (add-entry)))))

        delete-button (doto (JButton. "Poista")
                        (.addActionListener (proxy [ActionListener] []
                                                            (actionPerformed [e]
                                                              (delete-entry)))))

        edit-button (doto (JButton. "Muokkaa")
                      (.addActionListener (proxy [ActionListener] []
                                                        (actionPerformed [e]
                                                          (edit-entry)))))
        ok-button (JButton. "Valmis")
        result-label (JLabel. "Tulos:")
        result-field (JTextArea. "Tervetuloa kirjakantaan!" 20 40); row col
        id-field (JTextField. idstr 40); col
        title-field (JTextField. titlestr 40); deftxt col
        author-field (JTextField. authorstr 40)
        year-field (JTextField. yearstr 40)
        view-panel (doto (JPanel.); JPanel viewpanel = new JPanel()
                     (.add query-button); viewpanel.add(query-button)
                     (.add add-button)
                     (.add delete-button)
                     (.add edit-button)
                     (.add edit-button)
                     (.add ok-button)
                     (.add result-label)
                     (.add result-field)
                     (.add id-field)
                     (.add title-field)
                     (.add author-field)
                     (.add year-field))
        main-frame (doto (JFrame. "Kirjakanta")
                     (.setContentPane view-panel)
                     (.setSize 640 500)
                     (.setResizable false)
                     ;; (.setDefaultCloseOperation(JFrame/EXIT_ON_CLOSE)) ;this kills the Cider
                     (.setVisible true))
        ]
    
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
          (let [info (first (db/get-by-id id)); seq podracing
                id2 (str (first info))
                title (str (first (rest info)))
                author (str (first (rest (rest info))))
                year (str (first (rest (rest (rest info)))))]
            (do (.setText id-field id2) 
                (.setText title-field title)
                (.setText author-field author)
                (.setText year-field year))
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
        (do (.setText id-field idstr)
            (.setText title-field titlestr)
            (.setText author-field authorstr)
            (.setText year-field yearstr)))

    ))

