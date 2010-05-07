(ns com.tripotamus.WeldEstimator
  (:gen-class)
  (:import (java.awt.event KeyEvent WindowAdapter)
           (javax.swing JFrame JLabel JPanel JTabbedPane SwingConstants
                        UIManager)))

(defn save-before-exit []
	(println "function 'save-before-exit' called"))

;; build 'Define New Weld' panel ;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def define-weld-panel (doto (JPanel.)
  (.add (JLabel. "define-weld-panel" SwingConstants/CENTER))))

;; build 'Current Welds' panel ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def current-welds-panel (doto (JPanel.)
  (.add (JLabel. "current-welds-panel" SwingConstants/CENTER))))

;; build tabbed pane ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def tabbed-pane (doto (JTabbedPane.)
  (.addTab "Define New Weld" define-weld-panel)
  (.setMnemonicAt 0 KeyEvent/VK_1)
  (.addTab "Current Welds" current-welds-panel)
  (.setMnemonicAt 1 KeyEvent/VK_2)))

(defn init-GUI []
  (let [frame (JFrame. "Weld Estimator")]
    (doto frame
      (.setContentPane tabbed-pane)
      (.addWindowListener
        (proxy [WindowAdapter] []
          (windowClosing [event]
            (do (save-before-exit)
                (.dispose frame)
                (System/exit 0)))))
      (.setSize 800 500)
      (.setLocationRelativeTo frame)
      (.setVisible true))))

(defn -main []
  (UIManager/setLookAndFeel (UIManager/getSystemLookAndFeelClassName))
  (init-GUI))
