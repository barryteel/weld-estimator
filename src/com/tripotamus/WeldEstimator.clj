(ns com.tripotamus.WeldEstimator
  (:gen-class)
  (:import (java.awt.event WindowAdapter)
           (javax.swing JFrame UIManager)))

(defn save-before-exit []
	(println "function 'save-before-exit' called"))

(defn init-GUI []
  (let [frame (JFrame. "Weld Estimator")]
    (doto frame
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
