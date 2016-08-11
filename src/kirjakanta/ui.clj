(ns kirjakanta.ui
  (:gen-class)
  (:require [kirjakanta.core :as core])
  (:import [javax.swing JFrame JPanel JLabel JButton JTextField JTextArea]
           [javax.swing.event DocumentListener]
           [java.awt.event ActionListener]))


(defn kantaview []
  (declare reset-fields)
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
        title-field (JTextField. "NIMI" 40) ; deftxt col
        author-field (JTextField. "TEKIJA" 40)
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
        (if-not (and (= title "NIMI") (= author "TEKIJA") (= year "VUOSI"))
          (core/add-entry title author year)
          (if-not (and (= title "NIMI") (= author "TEKIJA"))
            (core/add-entry title author)
            (if-not (and (= title "NIMI"))
              (core/add-entry title)
              (.setText result-field "Anna ainakin kirjan nimi!")))))
      (reset-fields))
      

    (defn delete-entry []
      (let [id (.getText id-field)]
        (core/delete-entry id))
      (do (reset-fields)
          (print-entries)))
    
    (defn edit-entry []
      (let [id (.getText id-field)]))
    
    (defn reset-fields []
        (do (.setText title-field "NIMI")
            (.setText author-field "TEKIJA")
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

