(ns kirjakanta.ui
  (:gen-class)
  (:require [kirjakanta.core :as core])
  (:import [javax.swing JFrame JPanel JLabel JButton JTextField JTextArea]
           [javax.swing.event DocumentListener]
           [java.awt.event ActionListener]))


(defn kantaview
  []
  ;; viewpanel JPanel section
  (let [buttonque (doto (JButton. "Kysely") ; buttonque = new JButton("Kysely")
                    (.addActionListener (proxy [ActionListener] []
                                          (actionPerformed [e] (print-entries)))))
        buttonadd (doto (JButton. "Lisää")
                    (.addActionListener (proxy [ActionListener] []
                                          (actionPerformer [e] (switch2entrypanel)))))
        buttondel (JButton. "Poista")
        buttonedt (JButton. "Muokkaa")
        labelcmd (JLabel. "Komento:")
        inputfield (JTextField. 40);col
        labelrst (JLabel. "Tulos:")
        resultfield (JTextArea. 20 40);row col
        viewpanel (doto (JPanel.)
                    (.add buttonque)
                    (.add buttonadd)
                    (.add buttondel)
                    (.add buttonedt)
                    (.add labelcmd)
                    (.add inputfield)
                    (.add labelrst)
                    (.add resultfield))
        ;; entrypanel JPanel section
        titlet (JLabel. "NIMI")
        authort (JLabel. "KIRJAILIJA")
        yeart (JLabel. "VUOSI")
        titlef (JTextField. 40)
        authorf (JTextField. 40)
        yearf (JTextField. 40)
        donebtn (doto (JButton. "Valmis!")
                 (.addActionListener (proxy [ActionListener] []
                                       (actionPerformed [e] (add-entry)))))
        entrypanel (doto (JPanel.)
                     (.add titlet)
                     (.add titlef)
                     (.add authort)
                     (.add authorf)
                     (.add yeart)
                     (.add yearf)
                     (.add donebtn))
        ;; application JFrame
        frame (doto (JFrame. "Kirjakanta")
                (.setContentPane viewpanel)
                (.setSize 640 480)
                (.setResizable false)
                (.setVisible true))]
    
    (defn print-entries
      []
      (.setText resultfield (core/pprint-sql (core/query core/allq))))

    (defn add-entry
      []
      (let [title (.getText titlef)
            author (.getText authorf)
            year (.getText yearf)]
        (core/add-entry title author year)))
  
  (defn switch2entrypanel
    []
    (frame (.setContentPane panel))
    (.invalidate frame)
    (.validate frame))
  ))

