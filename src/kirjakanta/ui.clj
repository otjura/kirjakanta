(ns kirjakanta.ui
  (:gen-class)
  (:import [javax.swing JFrame JPanel JLabel JButton JTextField JTextArea]))


(defn kantaview
  []
  (let [buttonque (JButton. "Kysely")
        buttonadd (JButton. "Lisaa")
        buttondel (JButton. "Poista")
        buttonedt (JButton. "Muokkaa")
        dscrpt1 (JLabel. "Komento:")
        inputfield (JTextField. 40);col
        dscrpt2 (JLabel. "Tulos:")
        resultfield (JTextArea. 20 40);row col
        panel (doto (JPanel.)
                (.add buttonque)
                (.add buttonadd)
                (.add buttondel)
                (.add buttonedt)
                (.add dscrpt1)
                (.add inputfield)
                (.add dscrpt2)
                (.add resultfield))
        frame (doto (JFrame. "Kirjakanta")
                (.setContentPane panel)
                (.setSize 640 480)
                (.setVisible true))]
    ))
    
    
    ;;(.setSize frame 640 480)
   ;;(.setContentPane frame panel)
    ;;(.add panel buttonq)
      ;;(.setVisible frame true))
