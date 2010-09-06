(ns com.tripotamus.WeldEstimator
  (:gen-class)
  (:import (java.awt CardLayout)
           (java.awt.event ActionListener ItemEvent ItemListener KeyEvent
                           WindowAdapter)
           (javax.swing BorderFactory ButtonGroup ImageIcon JButton JCheckBox
                        JComboBox JFrame JLabel JPanel JRadioButton JTabbedPane
                        JTextField SwingConstants UIManager))
  (:use    (clojure.contrib [miglayout :only (miglayout)])
           (com.tripotamus.util [core :only
             (add-weld parse-field populate-multiples unpopulate-multiples)])))

(defn save-before-exit []
	(println "function 'save-before-exit' called"))

;; build 'Define new weld' panel ;;;;;;;;;;;;;;;;;;;;;;;;;;;
(declare joint-state-changed)

(def joint-listener
  (proxy [ItemListener] []
    (itemStateChanged [e]
      (joint-state-changed e))))

(def butt-radio (doto (JRadioButton. "butt" true)
  (.addItemListener joint-listener)))
(def joint-type (atom "butt"))
(def flush-corner-radio (doto (JRadioButton. "flush-corner")
  (.addItemListener joint-listener)))
(def edge-radio (doto (JRadioButton. "edge")
  (.addItemListener joint-listener)))
(def lap-radio (doto (JRadioButton. "lap/open-corner")
  (.addItemListener joint-listener)))
(def tee-radio (doto (JRadioButton. "tee")
  (.addItemListener joint-listener)))

(def joint-group (doto (ButtonGroup.)
  (.add butt-radio)
  (.add flush-corner-radio)
  (.add edge-radio)
  (.add lap-radio)
  (.add tee-radio)))

(defn joint-panel []
  (let [panel (miglayout (JPanel.) :layout :flowy; "debug"
    butt-radio
    flush-corner-radio
    edge-radio
    lap-radio
    tee-radio)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "Joint type")))))

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
  (.addItemListener groove-listener)))
(def groove-type (atom "v"))

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
      (.setBorder (BorderFactory/createTitledBorder "Groove type")))))

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

(declare multiples-state-changed)

(def multiples-listener
  (proxy [ItemListener] []
    (itemStateChanged [e]
      (multiples-state-changed e))))

;; first card: j-groove butt joint
(def jb-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def jbd1 (JTextField. 5))   ;j-groove butt joint - depth 1

(def jb-params {:d1 jbd1})

(defn jb-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][]"
    (JLabel. "d1") "cell 0 0"
    jbd1 "cell 1 0")] panel))

(defn jb-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    jb-image
    (jb-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "J-groove butt joint")))))

;; second card: j-groove flush-corner joint
(def jf-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def jfd1 (JTextField. 5))   ;j-groove flush-corner joint - depth 1

(def jf-params {:d1 jfd1})

(defn jf-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][]"
    (JLabel. "d1") "cell 0 0"
    jfd1 "cell 1 0")] panel))

(defn jf-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    jf-image
    (jf-inputs-panel) :align :center)]
    (doto panel
      (.setBorder
        (BorderFactory/createTitledBorder "J-groove flush-corner joint")))))

;; third card: j-groove edge joint
(def je-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def jed1 (JTextField. 5))   ;j-groove edge joint - depth 1

(def je-params {:d1 jed1})

(defn je-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][]"
    (JLabel. "d1") "cell 0 0"
    jed1 "cell 1 0")] panel))

(defn je-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    je-image
    (je-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "J-groove edge joint")))))

;; fourth card: j-groove lap joint
(def jl-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def jld1 (JTextField. 5))   ;j-groove lap joint - depth 1

(def jl-params {:d1 jld1})

(defn jl-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][]"
    (JLabel. "d1") "cell 0 0"
    jld1 "cell 1 0")] panel))

(defn jl-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    jl-image
    (jl-inputs-panel) :align :center)]
    (doto panel
      (.setBorder
        (BorderFactory/createTitledBorder
          "J-groove lap / open-corner joint")))))

;; fifth card: j-groove tee joint
(def jt-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def jtd1 (JTextField. 5))   ;j-groove tee joint - depth 1

(def jt-params {:d1 jtd1})

(defn jt-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][]"
    (JLabel. "d1") "cell 0 0"
    jtd1 "cell 1 0")] panel))

(defn jt-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    jt-image
    (jt-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "J-groove tee joint")))))

;; sixth card: u-groove butt joint
(def ub-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def ubd1 (JTextField. 5))   ;u-groove butt joint - depth 1

(def ub-params {:d1 ubd1})

(defn ub-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][]"
    (JLabel. "d1") "cell 0 0"
    ubd1 "cell 1 0")] panel))

(defn ub-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    ub-image
    (ub-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "U-groove butt joint")))))

;; seventh card: u-groove flush-corner joint
(def uf-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def ufd1 (JTextField. 5))   ;u-groove flush-corner joint - depth 1

(def uf-params {:d1 ufd1})

(defn uf-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][]"
    (JLabel. "d1") "cell 0 0"
    ufd1 "cell 1 0")] panel))

(defn uf-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    uf-image
    (uf-inputs-panel) :align :center)]
    (doto panel
      (.setBorder
        (BorderFactory/createTitledBorder "U-groove flush-corner joint")))))

;; eighth card: u-groove edge joint
(def ue-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def ued1 (JTextField. 5))   ;u-groove edge joint - depth 1

(def ue-params {:d1 ued1})

(defn ue-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][]"
    (JLabel. "d1") "cell 0 0"
    ued1 "cell 1 0")] panel))

(defn ue-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    ue-image
    (ue-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "U-groove edge joint")))))

;; ninth card: u-groove lap joint
(def ul-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def uld1 (JTextField. 5))   ;u-groove lap joint - depth 1

(def ul-params {:d1 uld1})

(defn ul-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][]"
    (JLabel. "d1") "cell 0 0"
    uld1 "cell 1 0")] panel))

(defn ul-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    ul-image
    (ul-inputs-panel) :align :center)]
    (doto panel
      (.setBorder
        (BorderFactory/createTitledBorder
          "U-groove lap / open-corner joint")))))

;; tenth card: u-groove tee joint
(def ut-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def utd1 (JTextField. 5))   ;u-groove tee joint - depth 1

(def ut-params {:d1 utd1})

(defn ut-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][]"
    (JLabel. "d1") "cell 0 0"
    utd1 "cell 1 0")] panel))

(defn ut-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    ut-image
    (ut-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "U-groove tee joint")))))

;; eleventh card: v-groove butt joint
(def vb-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def vbd1     (JTextField. 5))            ;v-groove butt joint - depth 1
(def vba1     (JTextField. 5))            ;angle 1
(def vbb1x2   (doto (JCheckBox.)          ;bevel 1 x2
  (.addItemListener multiples-listener)))
(def vbb1x4   (doto (JCheckBox.)
  (.addItemListener multiples-listener)))
(def vbd2     (JTextField. 5))
(def vba2     (JTextField. 5))
(def vbb1&2x2 (doto (JCheckBox.)          ;bevels 1 & 2 x2
  (.addItemListener multiples-listener)))
(def vbd3     (JTextField. 5))
(def vba3     (JTextField. 5))
(def vbb3x2   (doto (JCheckBox.)
  (.addItemListener multiples-listener)))
(def vbd4     (JTextField. 5))
(def vba4     (JTextField. 5))
(def vbg      (JTextField. 5))            ;gap
(def vbt      (JTextField. 5))            ;thickness
(def vbl      (JTextField. 5))            ;length
(def vbrh1    (JTextField. 5))            ;reinforcement height 1
(def vbrw1    (JTextField. 5))            ;reinforcement width 1
(def vbr1x2   (doto (JCheckBox.)          ;reinforcement 1 x2
  (.addItemListener multiples-listener)))
(def vbrh2    (JTextField. 5))
(def vbrw2    (JTextField. 5))

(def vb-params
  {:d1 vbd1 :a1 vba1 :d2 vbd2 :a2 vba2 :d3 vbd3 :a3 vba3 :d4 vbd4 :a4 vba4
   :gap vbg :thk vbt :lg vbl :rh1 vbrh1 :rw1 vbrw1 :rh2 vbrh2 :rw2 vbrw2})

(def params (atom {}))
(reset! params vb-params)

(defn vb-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][][right][][right]0[]3[right]0[]15
             [right][][right][][right]0[]"
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

(defn vb-card []
  (let [panel (miglayout (JPanel.) :layout :flowy; "debug"
    vb-image :align :center
    (vb-inputs-panel))]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "V-groove butt joint")))))

;; twelveth card: v-groove flush-corner joint
(def vf-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def vfd1   (JTextField. 5))              ;v-groove flush corner joint - depth 1
(def vfa1   (JTextField. 5))              ;angle 1
(def vfb1x2 (doto (JCheckBox.)            ;bevel 1 x2
  (.addItemListener multiples-listener)))
(def vfd2   (JTextField. 5))
(def vfa2   (JTextField. 5))
(def vff    (JTextField. 5))              ;fillet
(def vfg    (JTextField. 5))              ;gap
(def vft    (JTextField. 5))              ;thickness
(def vfl    (JTextField. 5))              ;length
(def vfrh   (JTextField. 5))              ;reinforcement height
(def vfrw   (JTextField. 5))              ;reinforcement width

(def vf-params
  {:d1 vfd1 :a1 vfa1 :d2 vfd2 :a2 vfa2 :f vff
   :gap vfg :thk vft :lg vfl :rh vfrh :rw vfrw})

(defn vf-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][][right][][right]0[]15[right][][right][]"
    (JLabel. "d1") "cell 0 0"
    vfd1 "cell 1 0"
    (JLabel. "a1") "cell 2 0"
    vfa1 "cell 3 0"
    (JLabel. "2x") "cell 4 0"
    vfb1x2 "cell 5 0"
    (JLabel. "d2") "cell 6 0"
    vfd2 "cell 7 0"
    (JLabel. "a2") "cell 8 0"
    vfa2 "cell 9 0"
    (JLabel. "f") "cell 0 1"
    vff "cell 1 1"
    (JLabel. "g") "cell 2 1"
    vfg "cell 3 1"
    (JLabel. "t") "cell 6 1"
    vft "cell 7 1"
    (JLabel. "lg") "cell 8 1"
    vfl "cell 9 1"
    (JLabel. "rh") "cell 0 2"
    vfrh "cell 1 2"
    (JLabel. "rw") "cell 2 2"
    vfrw "cell 3 2")] panel))

(defn vf-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    vf-image
    (vf-inputs-panel) :align :center)]
    (doto panel
      (.setBorder
        (BorderFactory/createTitledBorder "V-groove flush-corner joint")))))

;; thirteenth card: v-groove edge joint
(def ve-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def ved1   (JTextField. 5))              ;v-groove edge joint - depth 1
(def vea1   (JTextField. 5))              ;angle 1
(def veb1x2 (doto (JCheckBox.)            ;bevel 1 x2
  (.addItemListener multiples-listener)))
(def ved2   (JTextField. 5))
(def vea2   (JTextField. 5))
(def verh   (JTextField. 5))              ;reinforcement height
(def verw   (JTextField. 5))              ;reinforcement width
(def vel    (JTextField. 5))              ;length

(def ve-params {:d1 ved1 :a1 vea1 :d2 ved2 :a2 vea2 :rh verh :rw verw :lg vel})

(defn ve-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][][right][][right]0[]15[right][][right][]"
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

(defn ve-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    ve-image
    (ve-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "V-groove edge joint")))))

;; fourteenth card: v-groove lap joint
(def vl-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def vld1     (JTextField. 5))            ;v-groove lap joint - depth 1
(def vla1     (JTextField. 5))            ;angle 1
(def vlf1     (JTextField. 5))            ;fillet 1
(def vlb1&fx2 (doto (JCheckBox.)          ;bevel 1 & fillet x2
  (.addItemListener multiples-listener)))
(def vld2     (JTextField. 5))
(def vla2     (JTextField. 5))
(def vlf2     (JTextField. 5))
(def vll      (JTextField. 5))            ;length

(def vl-params {:d1 vld1 :a1 vla1 :f1 vlf1 :d2 vld2 :a2 vla2 :f2 vlf2 :lg vll})

(defn vl-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][][right][][right][][right]0[]"
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

(defn vl-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    vl-image
    (vl-inputs-panel) :align :center)]
    (doto panel
      (.setBorder
        (BorderFactory/createTitledBorder
          "V-groove lap / open-corner joint")))))

;; fifteenth card: v-groove tee joint
(def vt-image (doto (JLabel. (ImageIcon. "images/275x445.png"))
  (.setBorder (BorderFactory/createEtchedBorder))))

(def vtd1     (JTextField. 5))            ;v-groove tee joint - depth 1
(def vta1     (JTextField. 5))            ;angle 1
(def vtf1     (JTextField. 5))            ;fillet 1
(def vtb1&fx2 (doto (JCheckBox.)          ;bevel 1 & fillet x2
  (.addItemListener multiples-listener)))
(def vtd2     (JTextField. 5))
(def vta2     (JTextField. 5))
(def vtf2     (JTextField. 5))
(def vtg      (JTextField. 5))            ;gap
(def vtt      (JTextField. 5))            ;thickness
(def vtl      (JTextField. 5))            ;length

(def vt-params {:d1 vtd1 :a1 vta1 :f1 vtf1 :d2 vtd2 :a2 vta2 :f2 vtf2
                :gap vtg :thk vtt :lg vtl})

(defn vt-inputs-panel []
  (let [panel (miglayout (JPanel.); :layout "debug"
    :column "[right][][right][][right][][right]0[]"
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

(defn vt-card []
  (let [panel (miglayout (JPanel.) :layout :flowy :align :center; "debug"
    vt-image
    (vt-inputs-panel) :align :center)]
    (doto panel
      (.setBorder (BorderFactory/createTitledBorder "V-groove tee joint")))))

(def card-manager (CardLayout.))

;; add cards to deck
(def card-panel (doto (JPanel.)
  (.setLayout card-manager)
  (.add (vb-card) "vb-card")
  (.add (vf-card) "vf-card")
  (.add (ve-card) "ve-card")
  (.add (vl-card) "vl-card")
  (.add (vt-card) "vt-card")
  (.add (jb-card) "jb-card")
  (.add (jf-card) "jf-card")
  (.add (je-card) "je-card")
  (.add (jl-card) "jl-card")
  (.add (jt-card) "jt-card")
  (.add (ub-card) "ub-card")
  (.add (uf-card) "uf-card")
  (.add (ue-card) "ue-card")
  (.add (ul-card) "ul-card")
  (.add (ut-card) "ut-card")))

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

; @todo Determine proper way to indent JComboBox selections.
; Without resorting to leading spaces, selections are
; displayed tight against left edge of JComboBox.
(def materials-combo (doto (JComboBox.
  (to-array
    [" steel", " stainless steel", " aluminum"]))
      (.setSelectedIndex 0)
      (.addItemListener combo-listener)))

(def gases-combo (doto (JComboBox.
  (to-array
    [" CO\u2082", " open arc"]))
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

(def flat-radio (doto (JRadioButton. "flat")
  (.addItemListener position-listener)))
(def horizontal-radio (doto (JRadioButton. "horizontal" true)
  (.addItemListener position-listener)))
(def vertical-radio (doto (JRadioButton. "vertical")
  (.addItemListener position-listener)))
(def overhead-radio (doto (JRadioButton. "overhead")
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

(def multiples-checkboxes
  [vbb1x2 vbb1x4 vbb1&2x2 vbb3x2 vbr1x2 vfb1x2 veb1x2 vlb1&fx2 vtb1&fx2])

(def add-weld-button (doto (JButton. "Add weld")
  (.addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (do
          (add-weld (conj {:gt @groove-type :jt @joint-type}
            (into {} (for [[k v] @params] [k (parse-field v)]))))
          (dorun (map #(.setSelected % false) multiples-checkboxes))))))))

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
    (JLabel. "Material:")
    materials-combo "growx"
    (JLabel. "Shielding gas:")
    gases-combo "growx"
    (JLabel. "Electrode:")
    electrodes-combo "growx"
    (JLabel. "Amps of current:")
    amps-combo "growx" :wrap
    (position-panel) :aligny :top)] panel))

;; build 'Current welds' panel ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def current-welds-panel (doto (JPanel.)
  (.add (JLabel. "current-welds-panel" SwingConstants/CENTER))))

;; build tabbed pane ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def tabbed-pane (doto (JTabbedPane.)
  (.addTab "Define new weld" (new-weld-panel))
  (.setMnemonicAt 0 KeyEvent/VK_1)
  (.addTab "Current welds" current-welds-panel)
  (.setMnemonicAt 1 KeyEvent/VK_2)))

(defn groove+joint []
  (str (first @groove-type) (first @joint-type)))

(defn update-params []
  (reset! params @(find-var (symbol
    (str "com.tripotamus.WeldEstimator/" (groove+joint) "-params")))))

(defn update-ui []
  (.show card-manager card-panel (str (groove+joint) "-card")))

(defn joint-state-changed [e]
  (cond
    (= (.getSource e) butt-radio)
      (reset! joint-type "butt")
    (= (.getSource e) flush-corner-radio)
      (reset! joint-type "flush corner")
    (= (.getSource e) edge-radio)
      (reset! joint-type "edge")
    (= (.getSource e) lap-radio)
      (reset! joint-type "lap")
    (= (.getSource e) tee-radio)
      (reset! joint-type "tee"))
  (update-params)
  (update-ui))

(defn groove-state-changed [e]
  (cond
    (= (.getSource e) j-radio)
      (reset! groove-type "j")
    (= (.getSource e) u-radio)
      (reset! groove-type "u")
    (= (.getSource e) v-radio)
      (reset! groove-type "v"))
  (update-params)
  (update-ui))

(defn system-state-changed [e]
  (cond
    (= (.getSource e) imperial-radio)
      (println "imperial")
    (= (.getSource e) metric-radio)
      (println "metric")))

; @todo Refactor this function.
; This function works but smells.
(defn multiples-state-changed [e]
  (if (= (.getStateChange e) ItemEvent/SELECTED)
    (cond
      (= (.getSource e) vbb1x2)
        (populate-multiples [[vbd1 vbd2] [vba1 vba2]])
      (= (.getSource e) vbb1x4)
        (populate-multiples [[vbd1 vbd2 vbd3 vbd4] [vba1 vba2 vba3 vba4]])
      (= (.getSource e) vbb1&2x2)
        (populate-multiples [[vbd1 vbd3] [vba1 vba3] [vbd2 vbd4] [vba2 vba4]])
      (= (.getSource e) vbb3x2)
        (populate-multiples [[vbd3 vbd4] [vba3 vba4]])
      (= (.getSource e) vbr1x2)
        (populate-multiples [[vbrh1 vbrh2] [vbrw1 vbrw2]])
      (= (.getSource e) vfb1x2)
        (populate-multiples [[vfd1 vfd2] [vfa1 vfa2]])
      (= (.getSource e) veb1x2)
        (populate-multiples [[ved1 ved2] [vea1 vea2]])
      (= (.getSource e) vlb1&fx2)
        (populate-multiples [[vld1 vld2] [vla1 vla2] [vlf1 vlf2]])
      (= (.getSource e) vtb1&fx2)
        (populate-multiples [[vtd1 vtd2] [vta1 vta2] [vtf1 vtf2]]))
    (cond
      (= (.getSource e) vbb1x2)
        (unpopulate-multiples [[vbd1 vbd2] [vba1 vba2]])
      (= (.getSource e) vbb1x4)
        (unpopulate-multiples [[vbd1 vbd2 vbd3 vbd4] [vba1 vba2 vba3 vba4]])
      (= (.getSource e) vbb1&2x2)
        (unpopulate-multiples [[vbd1 vbd3] [vba1 vba3] [vbd2 vbd4] [vba2 vba4]])
      (= (.getSource e) vbb3x2)
        (unpopulate-multiples [[vbd3 vbd4] [vba3 vba4]])
      (= (.getSource e) vbr1x2)
        (unpopulate-multiples [[vbrh1 vbrh2] [vbrw1 vbrw2]])
      (= (.getSource e) vfb1x2)
        (unpopulate-multiples [[vfd1 vfd2] [vfa1 vfa2]])
      (= (.getSource e) veb1x2)
        (unpopulate-multiples [[ved1 ved2] [vea1 vea2]])
      (= (.getSource e) vlb1&fx2)
        (unpopulate-multiples [[vld1 vld2] [vla1 vla2] [vlf1 vlf2]])
      (= (.getSource e) vtb1&fx2)
        (unpopulate-multiples [[vtd1 vtd2] [vta1 vta2] [vtf1 vtf2]]))))

(defn process-state-changed [e]
  (cond
    (= (.getSource e) fcaw-radio)
      (println "fcaw")
    (= (.getSource e) gmaw-radio)
      (println "gmaw")
    (= (.getSource e) gtaw-radio)
      (println "gtaw")
    (= (.getSource e) saw-radio)
      (println "saw")
    (= (.getSource e) smaw-radio)
      (println "smaw")))

(defn combo-state-changed [e]
  (cond
    (= (.getSource e) materials-combo)
      (println (.getSelectedItem materials-combo))
    (= (.getSource e) gases-combo)
      (println (.getSelectedItem gases-combo))
    (= (.getSource e) electrodes-combo)
      (println (.getSelectedItem electrodes-combo))
    (= (.getSource e) amps-combo)
      (println (.getSelectedItem amps-combo))))

(defn position-state-changed [e]
  (cond
    (= (.getSource e) flat-radio)
      (println "flat")
    (= (.getSource e) horizontal-radio)
      (println "horizontal")
    (= (.getSource e) vertical-radio)
      (println "vertical")
    (= (.getSource e) overhead-radio)
      (println "overhead")))

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

