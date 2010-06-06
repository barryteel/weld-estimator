(ns com.tripotamus.WeldEstimator
  (:gen-class)
  (:import (java.awt.event ItemListener KeyEvent WindowAdapter)
           (javax.swing BorderFactory ButtonGroup JFrame JLabel JPanel
                        JRadioButton JTabbedPane SwingConstants UIManager))
  (:use    (clojure.contrib [miglayout :only (miglayout components)])))

(defn save-before-exit []
	(println "function 'save-before-exit' called"))

;; build 'Define New Weld' panel ;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def v-radio (doto (JRadioButton. "V" true)
  (.setEnabled false)))
(def u-radio (doto (JRadioButton. "U")
  (.setEnabled false)))

(defn groove-panel []
  (let [panel (miglayout (JPanel.)
    v-radio
    u-radio)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Groove Type")))))

(declare joint-changed)

(def joint-listener
  (proxy [ItemListener] []
    (itemStateChanged [e]
      (joint-changed e))))

(def butt-radio (doto (JRadioButton. "Butt" true)
  (.addItemListener joint-listener)))
(def corner-radio (doto (JRadioButton. "Corner")
  (.addItemListener joint-listener)))
(def edge-radio (doto (JRadioButton. "Edge")
  (.addItemListener joint-listener)))
(def lap-radio (doto (JRadioButton. "Lap")
  (.addItemListener joint-listener)))
(def tee-radio (doto (JRadioButton. "Tee")
  (.addItemListener joint-listener)))

(def joint-group (doto (ButtonGroup.)
  (.add butt-radio)
  (.add corner-radio)
  (.add edge-radio)
  (.add lap-radio)
  (.add tee-radio)))

(defn joint-panel []
  (let [panel (miglayout (JPanel.) :layout :flowy
    butt-radio
    corner-radio
    edge-radio
    lap-radio
    tee-radio)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Joint Type")))))

(defn new-weld-panel []
  (let [panel (miglayout (JPanel.)
    (groove-panel) "cell 0 0"
    (joint-panel) "cell 0 1")] panel))

;; build 'Current Welds' panel ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def current-welds-panel (doto (JPanel.)
  (.add (JLabel. "current-welds-panel" SwingConstants/CENTER))))

;; build tabbed pane ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def tabbed-pane (doto (JTabbedPane.)
  (.addTab "Define New Weld" (new-weld-panel))
  (.setMnemonicAt 0 KeyEvent/VK_1)
  (.addTab "Current Welds" current-welds-panel)
  (.setMnemonicAt 1 KeyEvent/VK_2)))

(defn joint-changed [e]
  (cond
    (= (.getSource e) butt-radio)
      (do
        (println "butt"))
    (= (.getSource e) corner-radio)
      (do
        (println "corner"))
    (= (.getSource e) edge-radio)
      (do
        (println "edge"))
    (= (.getSource e) lap-radio)
      (do
        (println "lap"))
    (= (.getSource e) tee-radio)
      (do
        (println "tee"))))

(defn init-GUI []
  (let [frame (JFrame. "Weld Estimator")]
    (doto frame
      (.setContentPane tabbed-pane)
      (.addWindowListener
        (proxy [WindowAdapter] []
          (windowClosing [e]
            (do (save-before-exit)
                (.dispose frame)
                (System/exit 0)))))
      (.setSize 800 500)
      (.setLocationRelativeTo frame)
      (.setVisible true))))

(defn -main []
  (UIManager/setLookAndFeel (UIManager/getSystemLookAndFeelClassName))
  (init-GUI))
