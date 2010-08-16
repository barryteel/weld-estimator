(ns com.tripotamus.util.core)

(defn parse-field [f]
  (let [result (try (Math/abs (Double/parseDouble (.getText f)))
               (catch NumberFormatException _ 0.0))]
  (.setText f "")
  result))

(defmulti add-weld (fn [p]
  [(p :jt) (p :gt)]))

(defmethod add-weld ["butt" "j"] [p]
  (println p))

(defmethod add-weld ["flush corner" "j"] [p]
  (println p))

(defmethod add-weld ["edge" "j"] [p]
  (println p))

(defmethod add-weld ["lap" "j"] [p]
  (println p))

(defmethod add-weld ["tee" "j"] [p]
  (println p))

(defmethod add-weld ["butt" "u"] [p]
  (println p))

(defmethod add-weld ["flush corner" "u"] [p]
  (println p))

(defmethod add-weld ["edge" "u"] [p]
  (println p))

(defmethod add-weld ["lap" "u"] [p]
  (println p))

(defmethod add-weld ["tee" "u"] [p]
  (println p))

(defmethod add-weld ["butt" "v"] [p]
  (println p))

(defmethod add-weld ["flush corner" "v"] [p]
  (println p))

(defmethod add-weld ["edge" "v"] [p]
  (println p))

(defmethod add-weld ["lap" "v"] [p]
  (println p))

(defmethod add-weld ["tee" "v"] [p]
  (println p))

