(ns com.tripotamus.util.core)

(def *forty-five* 45.0)
(def *minutes-per-hour* 60.0)

(defn parse-field [f]
  (let [result (try (Math/abs (Double/parseDouble (.getText f)))
               (catch NumberFormatException _ 0.0))]
    (.setText f "")
    result))

(defn populate-multiples [fields]
  (dorun
    (for [field fields]
      (let [text (.getText (first field))]
        (dorun
          (map #(.setText % text) (rest field)))))))

(defn unpopulate-multiples [fields]
  (dorun
    (for [field fields]
      (dorun
        (map #(.setText % "") (rest field))))))

;; area of triangle
(defn- tri-area [adjacent angle]
  (/ (* adjacent (* adjacent (Math/tan (Math/toRadians angle)))) 2.0))

;; area of rectangle
(defn- rect-area [length width]
  (* length width))

;; area of circular segment - http://en.wikipedia.org/wiki/Circular_segment
(defn- circseg-area [height width]
  (let [radius (+ (/ height 2.0) (/ (Math/pow width 2.0) (* height 8.0)))
        theta  (* (Math/acos (/ (- radius height) radius)) 2.0)
        area   (* (/ (Math/pow radius 2.0) 2.0) (- theta (Math/sin theta)))]
    area))

;; For now, just return density of low carbon steel (lb/inch^3)
(defn- material-density [] 0.2836)

;; For now, just return 5.5 lb/hr of weld wire
(defn- process-deposition-rate [] 5.5)

;; For now, just return 23% of possible arc time
(defn- operator-factor [] 0.23)

;; For now, just return 40% of arc time
(defn- fitting-factor [] 0.4)

(defmulti area-of-joint (fn [p]
  [(p :gt) (p :jt)]))

(defmethod area-of-joint ["j" "butt"] [p]
  (println p))

(defmethod area-of-joint ["j" "flush corner"] [p]
  (println p))

(defmethod area-of-joint ["j" "edge"] [p]
  (println p))

(defmethod area-of-joint ["j" "lap"] [p]
  (println p))

(defmethod area-of-joint ["j" "tee"] [p]
  (println p))

(defmethod area-of-joint ["u" "butt"] [p]
  (println p))

(defmethod area-of-joint ["u" "flush corner"] [p]
  (println p))

(defmethod area-of-joint ["u" "edge"] [p]
  (println p))

(defmethod area-of-joint ["u" "lap"] [p]
  (println p))

(defmethod area-of-joint ["u" "tee"] [p]
  (println p))

(defmethod area-of-joint ["v" "butt"] [p]
  (+ (tri-area     (p :d1)  (p :a1))
     (tri-area     (p :d2)  (p :a2))
     (tri-area     (p :d3)  (p :a3))
     (tri-area     (p :d4)  (p :a4))
     (rect-area    (p :thk) (p :gap))
     (circseg-area (p :rh1) (p :rw1))
     (circseg-area (p :rh2) (p :rw2))))

(defmethod area-of-joint ["v" "flush corner"] [p]
  (+ (tri-area     (p :d1)  (p :a1))
     (tri-area     (p :d2)  (p :a2))
     (tri-area     (p :f)   *forty-five*)
     (rect-area    (p :thk) (p :gap))
     (circseg-area (p :rh)  (p :rw))))

(defmethod area-of-joint ["v" "edge"] [p]
  (+ (tri-area     (p :d1)  (p :a1))
     (tri-area     (p :d2)  (p :a2))
     (circseg-area (p :rh)  (p :rw))))

(defmethod area-of-joint ["v" "lap"] [p]
  (+ (tri-area     (p :d1)  (p :a1))
     (tri-area     (p :f1)  *forty-five*)
     (tri-area     (p :d2)  (p :a2))
     (tri-area     (p :f2)  *forty-five*)))

(defmethod area-of-joint ["v" "tee"] [p]
  (+ (tri-area     (p :d1)  (p :a1))
     (tri-area     (p :f1)  *forty-five*)
     (tri-area     (p :d2)  (p :a2))
     (tri-area     (p :f2)  *forty-five*)
     (rect-area    (p :thk) (p :gap))))

(defn add-weld [p]
  (let [weight-of-weld (* (area-of-joint p) (p :lg) (material-density))
        arc-time       (/ weight-of-weld
                          (* (process-deposition-rate) (operator-factor)))
        fit-time       (* arc-time (fitting-factor))
        total-time     (+ arc-time fit-time)]
    (println (str "Arc time is:   "
      (format "%.2f" arc-time) " hrs / "
      (format "%.0f" (* arc-time *minutes-per-hour*)) " mins"))
    (println (str "Fit time is:   "
      (format "%.2f" fit-time) " hrs / "
      (format "%.0f" (* fit-time *minutes-per-hour*)) " mins"))
    (println (str "Total time is: "
      (format "%.2f" total-time) " hrs / "
      (format "%.0f" (* total-time *minutes-per-hour*)) " mins"))
    (println (str "Will require:  "
      (format "%.1f" weight-of-weld) " lbs of wire\n"))))

