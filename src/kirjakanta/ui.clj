(ns kirjakanta.ui
  (:gen-class)
  (:require [kirjakanta.core :as core])
  (:import [javax.swing JFrame JPanel JLabel JButton JTextField JTextArea]
           [javax.swing.event DocumentListener]
           [java.awt CardLayout]
           [java.awt.event ActionListener]))


(defn kantaview
  []    ;; viewpanel JPanel section
  (let [buttonque (JButton. "Kysely") ; JButton buttonque = new JButton("Kysely")
        buttonadd (JButton. "Lisää")
        buttondel (JButton. "Poista")
        buttonedt (JButton. "Muokkaa")
        labelcmd (JLabel. "Komento:")
        inputfield (JTextField. 40);col
        labelrst (JLabel. "Tulos:")
        resultfield (JTextArea. 20 40);row col
        viewpanel (doto (JPanel.) ;JPanel viewpanel = new JPanel()
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
        donebtn (JButton. "Valmis!")
        entrypanel (doto (JPanel.)
                     (.add titlet)
                     (.add titlef)
                     (.add authort)
                     (.add authorf)
                     (.add yeart)
                     (.add yearf)
                     (.add donebtn))
                     
        ;; JPanel with CardLayout
        cardpanel (doto (JPanel. (CardLayout.))
                    (.add viewpanel, "VIEW")
                    (.add entrypanel, "ENTRY"))
              
        ;; application JFrame
        frame (doto (JFrame. "Kirjakanta")
                (.setContentPane cardpanel)
                (.setSize 640 480)
                (.setResizable false)
                ;(.setDefaultCloseOperation(JFrame/EXIT_ON_CLOSE));this kills the Cider
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
    
    ;; java interop
   ;; (. object-expr-or-classname-symbol method-or-member-symbol optional-args*)
  (defn switchpanel
    []
    ;;(.setText resultfield "FUCK YOU RETARD")) ;works 
    (.next cardpanel))
   

  (.addActionListener buttonque (proxy [ActionListener] []
                                  (actionPerformed [e] (print-entries))))
  (.addActionListener buttonadd (proxy [ActionListener] []
                                  (actionPerformed [e] (switchpanel))))
  (.addActionListener donebtn (proxy [ActionListener] []
                                  (actionPerformed [e] (add-entry))))
  
  ))

