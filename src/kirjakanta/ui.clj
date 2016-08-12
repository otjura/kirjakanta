(ns kirjakanta.ui
  (:gen-class)
  (:require [kirjakanta.core :as core])
  (:import [javax.swing JFrame JPanel JLabel JButton JTextField JTextArea]
           [javax.swing.event DocumentListener]
           [java.awt.event ActionListener]))


(defn kantaview []
  (declare print-entries add-entry delete-entry edit-entry reset-fields)
  (let [query-button (JButton. "Kysely") ; JButton buttonque = new JButton("Kysely")
                     
        add-button (doto (JButton. "Lisaa")
                     (on-action add-entry))
        delete-button (doto (JButton. "Poista")
                        (on-action delete-entry))
        edit-button (doto (JButton. "Muokkaa")
                      (on-action edit-entry))
        result-label (JLabel. "Tulos:")
        result-field (JTextArea. 20 40) ; row col
        id-field (JTextField. "ID" 40) ; col
        title-field (JTextField. "OTSIKKO" 40) ; deftxt col
        author-field (JTextField. "TEKIJÄ" 40)
        year-field (JTextField. "VUOSI" 40)
        view-panel (doto (JPanel.) ; JPanel viewpanel = new JPanel()
                     (.add query-button) ; viewpanel.add(query-button)
                     (.add add-button)
                     (.add delete-button)
                     (.add edit-button)
                     (.add edit-button)
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
      (.setText result-field (core/pprint-sql (core/query core/allq))))

    (defn add-entry []
      (let [title (.getText title-field)
            author (.getText author-field)
            year (.getText year-field)]
        (if-not (and (= title "OTSIKKO") (= author "TEKIJÄ") (= year "VUOSI"))
          (do (core/add-entry title author year)
              (reset-fields)
              (print-entries))
          (if-not (and (= title "OTSIKKO") (= author "TEKIJÄ"))
            (do (core/add-entry title author)
                (reset-fields)
                (print-entries))
            (if-not (and (= title "OTSIKKO"))
              (do (core/add-entry title)
                  (reset-fields)
                  (print-entries))
              (.setText result-field "Anna ainakin kirjan nimi!"))))))
      
    (defn delete-entry []
      (let [id (.getText id-field)]
        (if-not (= id "ID") 
          (do (core/delete-entry id)
              (reset-fields)
              (print-entries))
          (.setText result-field "Anna teoksen ID jonka haluat poistaa!"))))
    
    (defn edit-entry []
      (let [id (.getText id-field)
            title (.getText title-field)
            author (.getText author-field)
            year (.getText year-field)]
        (if (= id "ID")
          (.setText result-field "Anna ID ja muokkaa haluamiasi kenttiä alla!")
          (let [info (first (core/get-by-id id))
                id (str (first info))
                title (str (first (rest info)))
                author (str (first (rest (rest info))))
                year (str (first (rest (rest (rest info)))))] ;seq
            (do (.setText id-field id) 
                (.setText title-field title)
                (.setText author-field author)
                (.setText year-field year))))))
    
    (defn reset-fields []
        (do (.setText id-field "ID")
            (.setText title-field "OTSIKKO")
            (.setText author-field "TEKIJÄ")
            (.setText year-field "VUOSI")))

    ;; queryButton.addActionListener(new ActionListener () {
    ;;                                public void actionPerformed(ActionEvent e) {
    ;;                                  printEntries(); }});
    (.addActionListener query-button (proxy [ActionListener] []
                                       (actionPerformed [e] (print-entries))))

    (.addActionListener add-button (proxy [ActionListener] []
                                     (actionPerformed [e] (add-entry))))

    (.addActionListener delete-button (proxy [ActionListener] []
                                        (actionPerformed [e] (delete-entry))))

    (.addActionListener edit-button (proxy [ActionListener] []
                                      (actionPerformed [e] (edit-entry))))
  ))

