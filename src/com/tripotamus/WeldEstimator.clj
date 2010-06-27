(ns com.tripotamus.WeldEstimator
  (:gen-class)
  (:import (java.awt CardLayout)
           (java.awt.event ActionListener ItemListener KeyEvent WindowAdapter)
           (javax.swing BorderFactory ButtonGroup ImageIcon JButton JCheckBox
                        JComboBox JFrame JLabel JPanel JRadioButton JTabbedPane
                        JTextField SwingConstants UIManager))
  (:use    (clojure.contrib [miglayout :only (miglayout components)])))

(defn save-before-exit []
	(println "function 'save-before-exit' called"))

;; build 'Define New Weld' panel ;;;;;;;;;;;;;;;;;;;;;;;;;;;
(declare joint-state-changed)

(def joint-listener
  (proxy [ItemListener] []
    (itemStateChanged [e]
      (joint-state-changed e))))

(def butt-radio (doto (JRadioButton. "Butt" true)
  (.addItemListener joint-listener)))
(def joint-type (ref "butt"))
(def flush-corner-radio (doto (JRadioButton. "Corner, Flush")
  (.addItemListener joint-listener)))
(def open-corner-radio (doto (JRadioButton. "Corner, Open")
  (.addItemListener joint-listener)))
(def edge-radio (doto (JRadioButton. "Edge")
  (.addItemListener joint-listener)))
(def lap-radio (doto (JRadioButton. "Lap")
  (.addItemListener joint-listener)))
(def tee-radio (doto (JRadioButton. "Tee")
  (.addItemListener joint-listener)))

(def joint-group (doto (ButtonGroup.)
  (.add butt-radio)
  (.add flush-corner-radio)
  (.add open-corner-radio)
  (.add edge-radio)
  (.add lap-radio)
  (.add tee-radio)))

(defn joint-panel []
  (let [panel (miglayout (JPanel.) :layout :flowy; "debug"
    butt-radio
    flush-corner-radio
    open-corner-radio
    edge-radio
    lap-radio
    tee-radio)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Joint")))))

(declare groove-state-changed)

(def groove-listener
  (proxy [ItemListener] []
    (itemStateChanged [e]
      (groove-state-changed e))))

(def j-radio (doto (JRadioButton. "J")
  (.addItemListener groove-listener)
  (.setEnabled false)))
(def u-radio (doto (JRadioButton. "U")
  (.addItemListener groove-listener)
  (.setEnabled false)))
(def v-radio (doto (JRadioButton. "V" true)
  (.addItemListener groove-listener)
  (.setEnabled false)))

(def groove-group (doto (ButtonGroup.)
  (.add j-radio)
  (.add u-radio)
  (.add v-radio)))

(defn groove-panel []
  (let [panel (miglayout (JPanel.) :layout :flowy; "debug"
    j-radio
    u-radio
    v-radio)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Groove")))))

(declare system-state-changed)

(def system-listener
  (proxy [ItemListener] []
    (itemStateChanged [e]
      (system-state-changed e))))

(def imperial-radio (doto (JRadioButton. "Imperial" true)
  (.addItemListener system-listener)
  (.setEnabled false)))
(def metric-radio (doto (JRadioButton. "Metric")
  (.addItemListener system-listener)
  (.setEnabled false)))

(def system-group (doto (ButtonGroup.)
  (.add imperial-radio)
  (.add metric-radio)))

(defn system-panel []
  (let [panel (miglayout (JPanel.) :layout :flowy; "debug"
    imperial-radio
    metric-radio)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "System")))))

;; first card - Butt Joint
(def butt-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def vbd1     (JTextField. 5))   ;v-groove butt joint - depth 1
(def vba1     (JTextField. 5))   ;angle 1
(def vbb1x2   (JCheckBox.))      ;bevel 1 x2
(def vbb1x4   (JCheckBox.))
(def vbd2     (JTextField. 5))
(def vba2     (JTextField. 5))
(def vbb1&2x2 (JCheckBox.))      ;bevels 1 & 2 x2
(def vbd3     (JTextField. 5))
(def vba3     (JTextField. 5))
(def vbb3x2   (JCheckBox.))
(def vbd4     (JTextField. 5))
(def vba4     (JTextField. 5))
(def vbg      (JTextField. 5))   ;gap
(def vbt      (JTextField. 5))   ;thickness
(def vbl      (JTextField. 5))   ;length
(def vbrh1    (JTextField. 5))   ;reinforcement height 1
(def vbrw1    (JTextField. 5))   ;reinforcement width 1
(def vbr1x2   (JCheckBox.))      ;reinforcement 1 x2
(def vbrh2    (JTextField. 5))
(def vbrw2    (JTextField. 5))

(defn butt-inputs-panel []
  (let [panel (miglayout (JPanel.)
    :column "[right][][right][][right]0[]3[right]0[]15
             [right][][right][][right]0[]"
;   :layout "debug"
    (JLabel. "d1") "cell 0 0"
    vbd1 "cell 1 0"
    (JLabel. "a1") "cell 2 0"
    vba1 "cell 3 0"
    (JLabel. "2x") "cell 4 0"
    vbb1x2 "cell 5 0"
    (JLabel. "4x") "cell 6 0"
    vbb1x4 "cell 7 0"
    (JLabel. "d2") "cell 8 0"
    vbd2 "cell 9 0"
    (JLabel. "a2") "cell 10 0"
    vba2 "cell 11 0"
    (JLabel. "2x") "cell 12 0"
    vbb1&2x2 "cell 13 0"
    (JLabel. "d3") "cell 0 1"
    vbd3 "cell 1 1"
    (JLabel. "a3") "cell 2 1"
    vba3 "cell 3 1"
    (JLabel. "2x") "cell 4 1"
    vbb3x2 "cell 5 1"
    (JLabel. "d4") "cell 8 1"
    vbd4 "cell 9 1"
    (JLabel. "a4") "cell 10 1"
    vba4 "cell 11 1"
    (JLabel. "g") "cell 0 2"
    vbg "cell 1 2"
    (JLabel. "t") "cell 2 2"
    vbt "cell 3 2"
    (JLabel. "lg") "cell 8 2"
    vbl "cell 9 2"
    (JLabel. "rh1") "cell 0 3"
    vbrh1 "cell 1 3"
    (JLabel. "rw1") "cell 2 3"
    vbrw1 "cell 3 3"
    (JLabel. "2x") "cell 4 3"
    vbr1x2 "cell 5 3"
    (JLabel. "rh2") "cell 8 3"
    vbrh2 "cell 9 3"
    (JLabel. "rw2") "cell 10 3"
    vbrw2 "cell 11 3")] panel))

(defn butt-card []
  (let [panel (miglayout (JPanel.) :layout :flowy; "debug"
    butt-image :align :center
    (butt-inputs-panel))]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Butt Joint")))))

;; second card - Flush Corner Joint
(def flush-corner-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def vfcd1   (JTextField. 5))   ;v-groove flush corner joint - depth 1
(def vfca1   (JTextField. 5))   ;angle 1
(def vfcb1x2 (JCheckBox.))      ;bevel 1 x2
(def vfcd2   (JTextField. 5))
(def vfca2   (JTextField. 5))
(def vfcf    (JTextField. 5))   ;fillet
(def vfcg    (JTextField. 5))   ;gap
(def vfct    (JTextField. 5))   ;thickness
(def vfcl    (JTextField. 5))   ;length
(def vfcrh   (JTextField. 5))   ;reinforcement height
(def vfcrw   (JTextField. 5))   ;reinforcement width

(defn flush-corner-inputs-panel []
  (let [panel (miglayout (JPanel.)
    :column "[right][][right][][right]0[]15[right][][right][]"
;   :layout "debug"
    (JLabel. "d1") "cell 0 0"
    vfcd1 "cell 1 0"
    (JLabel. "a1") "cell 2 0"
    vfca1 "cell 3 0"
    (JLabel. "2x") "cell 4 0"
    vfcb1x2 "cell 5 0"
    (JLabel. "d2") "cell 6 0"
    vfcd2 "cell 7 0"
    (JLabel. "a2") "cell 8 0"
    vfca2 "cell 9 0"
    (JLabel. "f") "cell 0 1"
    vfcf "cell 1 1"
    (JLabel. "g") "cell 2 1"
    vfcg "cell 3 1"
    (JLabel. "t") "cell 6 1"
    vfct "cell 7 1"
    (JLabel. "lg") "cell 8 1"
    vfcl "cell 9 1"
    (JLabel. "rh") "cell 0 2"
    vfcrh "cell 1 2"
    (JLabel. "rw") "cell 2 2"
    vfcrw "cell 3 2")] panel))

(defn flush-corner-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    flush-corner-image
    (flush-corner-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Flush Corner Joint")))))

;; third card - Open Corner Joint
(def open-corner-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def ocf1   (JTextField. 5))   ;open corner joint - fillet 1
(def ocf1x2 (JCheckBox.))      ;fillet 1 x2
(def ocf2   (JTextField. 5))
(def ocl    (JTextField. 5))   ;length

(defn open-corner-inputs-panel []
  (let [panel (miglayout (JPanel.)
    :column "[right][][right]0[]15[right][]"
;   :layout "debug"
    (JLabel. "f1") "cell 0 0"
    ocf1 "cell 1 0"
    (JLabel. "2x") "cell 2 0"
    ocf1x2 "cell 3 0"
    (JLabel. "f2") "cell 4 0"
    ocf2 "cell 5 0"
    (JLabel. "lg") "cell 0 1"
    ocl "cell 1 1")] panel))

(defn open-corner-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    open-corner-image
    (open-corner-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Open Corner Joint")))))

;; fourth card - Edge Joint
(def edge-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def ved1   (JTextField. 5))   ;v-groove edge joint - depth 1
(def vea1   (JTextField. 5))   ;angle 1
(def veb1x2 (JCheckBox.))      ;bevel 1 x2
(def ved2   (JTextField. 5))
(def vea2   (JTextField. 5))
(def verh   (JTextField. 5))   ;reinforcement height
(def verw   (JTextField. 5))   ;reinforcement width
(def vel    (JTextField. 5))   ;length

(defn edge-inputs-panel []
  (let [panel (miglayout (JPanel.)
    :column "[right][][right][][right]0[]15[right][][right][]"
;   :layout "debug"
    (JLabel. "d1") "cell 0 0"
    ved1 "cell 1 0"
    (JLabel. "a1") "cell 2 0"
    vea1 "cell 3 0"
    (JLabel. "2x") "cell 4 0"
    veb1x2 "cell 5 0"
    (JLabel. "d2") "cell 6 0"
    ved2 "cell 7 0"
    (JLabel. "a2") "cell 8 0"
    vea2 "cell 9 0"
    (JLabel. "rh") "cell 0 1"
    verh "cell 1 1"
    (JLabel. "rw") "cell 2 1"
    verw "cell 3 1"
    (JLabel. "lg") "cell 6 1"
    vel "cell 7 1")] panel))

(defn edge-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    edge-image
    (edge-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Edge Joint")))))

;; fifth card - Lap Joint
(def lap-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def vld1     (JTextField. 5))   ;v-groove lap joint - depth 1
(def vla1     (JTextField. 5))   ;angle 1
(def vlf1     (JTextField. 5))   ;fillet 1
(def vlb1&fx2 (JCheckBox.))      ;bevel 1 & fillet x2
(def vld2     (JTextField. 5))
(def vla2     (JTextField. 5))
(def vlf2     (JTextField. 5))
(def vll      (JTextField. 5))   ;length

(defn lap-inputs-panel []
  (let [panel (miglayout (JPanel.)
    :column "[right][][right][][right][][right]0[]"
;   :layout "debug"
    (JLabel. "d1") "cell 0 0"
    vld1 "cell 1 0"
    (JLabel. "a1") "cell 2 0"
    vla1 "cell 3 0"
    (JLabel. "f1") "cell 4 0"
    vlf1 "cell 5 0"
    (JLabel. "2x") "cell 6 0"
    vlb1&fx2 "cell 7 0"
    (JLabel. "d2") "cell 0 1"
    vld2 "cell 1 1"
    (JLabel. "a2") "cell 2 1"
    vla2 "cell 3 1"
    (JLabel. "f2") "cell 4 1"
    vlf2 "cell 5 1"
    (JLabel. "lg") "cell 0 2"
    vll "cell 1 2")] panel))

(defn lap-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    lap-image
    (lap-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Lap Joint")))))

;; sixth card - Tee Joint
(def tee-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def vtd1     (JTextField. 5))   ;v-groove tee joint - depth 1
(def vta1     (JTextField. 5))   ;angle 1
(def vtf1     (JTextField. 5))   ;fillet 1
(def vtb1&fx2 (JCheckBox.))      ;bevel 1 & fillet x2
(def vtd2     (JTextField. 5))
(def vta2     (JTextField. 5))
(def vtf2     (JTextField. 5))
(def vtg      (JTextField. 5))   ;gap
(def vtt      (JTextField. 5))   ;thickness
(def vtl      (JTextField. 5))   ;length

(defn tee-inputs-panel []
  (let [panel (miglayout (JPanel.)
    :column "[right][][right][][right][][right]0[]"
;   :layout "debug"
    (JLabel. "d1") "cell 0 0"
    vtd1 "cell 1 0"
    (JLabel. "a1") "cell 2 0"
    vta1 "cell 3 0"
    (JLabel. "f1") "cell 4 0"
    vtf1 "cell 5 0"
    (JLabel. "2x") "cell 6 0"
    vtb1&fx2 "cell 7 0"
    (JLabel. "d2") "cell 0 1"
    vtd2 "cell 1 1"
    (JLabel. "a2") "cell 2 1"
    vta2 "cell 3 1"
    (JLabel. "f2") "cell 4 1"
    vtf2 "cell 5 1"
    (JLabel. "g") "cell 0 2"
    vtg "cell 1 2"
    (JLabel. "t") "cell 2 2"
    vtt "cell 3 2"
    (JLabel. "lg") "cell 4 2"
    vtl "cell 5 2")] panel))

(defn tee-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    tee-image
    (tee-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Tee Joint")))))

;; add cards to deck
(def card-manager (CardLayout.))

(def card-panel (doto (JPanel.)
  (.setLayout card-manager)
  (.add (butt-card) "butt-card")
  (.add (flush-corner-card) "flush-corner-card")
  (.add (open-corner-card) "open-corner-card")
  (.add (edge-card) "edge-card")
  (.add (lap-card) "lap-card")
  (.add (tee-card) "tee-card")))

(declare process-state-changed)

(def process-listener
  (proxy [ItemListener] []
    (itemStateChanged [e]
      (process-state-changed e))))

(def fcaw-radio (doto (JRadioButton. "FCAW" true)
  (.addItemListener process-listener)))
(def gmaw-radio (doto (JRadioButton. "GMAW")
  (.addItemListener process-listener)))
(def gtaw-radio (doto (JRadioButton. "GTAW")
  (.addItemListener process-listener)))
(def saw-radio (doto (JRadioButton. "SAW")
  (.addItemListener process-listener)))
(def smaw-radio (doto (JRadioButton. "SMAW")
  (.addItemListener process-listener)))

(def process-group (doto (ButtonGroup.)
  (.add fcaw-radio)
  (.add gmaw-radio)
  (.add gtaw-radio)
  (.add saw-radio)
  (.add smaw-radio)))

(defn process-panel []
  (let [panel (miglayout (JPanel.) :layout :flowy; "debug"
    fcaw-radio
    gmaw-radio
    gtaw-radio
    saw-radio
    smaw-radio)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Process")))))

(declare combo-state-changed)

(def combo-listener
  (proxy [ItemListener] []
    (itemStateChanged [e]
      (combo-state-changed e))))

; Without resorting to leading spaces, selections are
; displayed tight against left edge of JComboBox.
; @todo Determine proper way to indent JComboBox selections
(def materials-combo (doto (JComboBox.
  (to-array
    [" Steel", " Stainless Steel", " Aluminum"]))
      (.setSelectedIndex 0)
      (.addItemListener combo-listener)))

(def gases-combo (doto (JComboBox.
  (to-array
    [" CO\u2082", " Open Arc"]))
      (.setSelectedIndex 0)
      (.addItemListener combo-listener)))

(def electrodes-combo (doto (JComboBox.
  (to-array
    [" 3/64\" dia", " 1/16\" dia", " 3/32\" dia", " 1/8\" dia"]))
      (.setSelectedIndex 1)
      (.addItemListener combo-listener)))

(def amps-combo (doto (JComboBox.
  (to-array
    [" 200", " 300", " 400", " 500", " 600", " 700"]))
      (.setSelectedIndex 0)
      (.addItemListener combo-listener)))

(declare position-state-changed)

(def position-listener
  (proxy [ItemListener] []
    (itemStateChanged [e]
      (position-state-changed e))))

(def flat-radio (doto (JRadioButton. "Flat")
  (.addItemListener position-listener)))
(def horizontal-radio (doto (JRadioButton. "Horizontal" true)
  (.addItemListener position-listener)))
(def vertical-radio (doto (JRadioButton. "Vertical")
  (.addItemListener position-listener)))
(def overhead-radio (doto (JRadioButton. "Overhead")
  (.addItemListener position-listener)))

(def position-group (doto (ButtonGroup.)
  (.add flat-radio)
  (.add horizontal-radio)
  (.add vertical-radio)
  (.add overhead-radio)))

(defn position-panel []
  (let [panel (miglayout (JPanel.) :layout :flowy; "debug"
    flat-radio
    horizontal-radio
    vertical-radio
    overhead-radio)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Position")))))

(def add-weld-button (doto (JButton. "Add weld")
  (.addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (do
          (println "'Add Weld' button clicked")))))))

(defn footer-panel []
  (let [panel (miglayout (JPanel.) :layout :align :center; "debug"
    add-weld-button)]
    (doto panel
      (.setBorder (BorderFactory/createEtchedBorder)))))

(defn new-weld-panel []
  (let [panel (miglayout (JPanel.) :layout :nogrid :flowy; "debug"
    (footer-panel) :south
    (joint-panel) :aligny :top "growx"
    (groove-panel) "growx"
    (system-panel) "growx" :wrap
    card-panel :aligny :top :wrap
    (process-panel) :aligny :top "growx"
    (JLabel. "Material")
    materials-combo "growx"
    (JLabel. "Shielding Gas")
    gases-combo "growx"
    (JLabel. "Electrode")
    electrodes-combo "growx"
    (JLabel. "Current, Amps")
    amps-combo "growx" :wrap
    (position-panel) :aligny :top)] panel))

;; build 'Current Welds' panel ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def current-welds-panel (doto (JPanel.)
  (.add (JLabel. "current-welds-panel" SwingConstants/CENTER))))

;; build tabbed pane ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def tabbed-pane (doto (JTabbedPane.)
  (.addTab "Define New Weld" (new-weld-panel))
  (.setMnemonicAt 0 KeyEvent/VK_1)
  (.addTab "Current Welds" current-welds-panel)
  (.setMnemonicAt 1 KeyEvent/VK_2)))

(defn joint-state-changed [e]
  (cond
    (= (.getSource e) butt-radio)
      (do
        (dosync (ref-set joint-type "butt"))
        (.show card-manager card-panel "butt-card"))
    (= (.getSource e) flush-corner-radio)
      (do
        (dosync (ref-set joint-type "flush corner"))
        (.show card-manager card-panel "flush-corner-card"))
    (= (.getSource e) open-corner-radio)
      (do
        (dosync (ref-set joint-type "open corner"))
        (.show card-manager card-panel "open-corner-card"))
    (= (.getSource e) edge-radio)
      (do
        (dosync (ref-set joint-type "edge"))
        (.show card-manager card-panel "edge-card"))
    (= (.getSource e) lap-radio)
      (do
        (dosync (ref-set joint-type "lap"))
        (.show card-manager card-panel "lap-card"))
    (= (.getSource e) tee-radio)
      (do
        (dosync (ref-set joint-type "tee"))
        (.show card-manager card-panel "tee-card"))))

(defn groove-state-changed [e]
  (cond
    (= (.getSource e) j-radio)
      (do
        (println "j-groove"))
    (= (.getSource e) u-radio)
      (do
        (println "u-groove"))
    (= (.getSource e) v-radio)
      (do
        (println "v-groove"))))

(defn system-state-changed [e]
  (cond
    (= (.getSource e) imperial-radio)
      (do
        (println "imperial"))
    (= (.getSource e) metric-radio)
      (do
        (println "metric"))))

(defn process-state-changed [e]
  (cond
    (= (.getSource e) fcaw-radio)
      (do
        (println "fcaw"))
    (= (.getSource e) gmaw-radio)
      (do
        (println "gmaw"))
    (= (.getSource e) gtaw-radio)
      (do
        (println "gtaw"))
    (= (.getSource e) saw-radio)
      (do
        (println "saw"))
    (= (.getSource e) smaw-radio)
      (do
        (println "smaw"))))

(defn combo-state-changed [e]
  (cond
    (= (.getSource e) materials-combo)
      (do
        (println (.getSelectedItem materials-combo)))
    (= (.getSource e) gases-combo)
      (do
        (println (.getSelectedItem gases-combo)))
    (= (.getSource e) electrodes-combo)
      (do
        (println (.getSelectedItem electrodes-combo)))
    (= (.getSource e) amps-combo)
      (do
        (println (.getSelectedItem amps-combo)))))

(defn position-state-changed [e]
  (cond
    (= (.getSource e) flat-radio)
      (do
        (println "flat"))
    (= (.getSource e) horizontal-radio)
      (do
        (println "horizontal"))
    (= (.getSource e) vertical-radio)
      (do
        (println "vertical"))
    (= (.getSource e) overhead-radio)
      (do
        (println "overhead"))))

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
      (.pack)
      (.setLocationRelativeTo frame)
      (.setVisible true))))

(defn -main []
  (UIManager/setLookAndFeel (UIManager/getSystemLookAndFeelClassName))
  (init-GUI))
