(ns com.tripotamus.util.db
  (:use (clojure.contrib [sql :only (with-connection with-query-results)])))

(def db {:classname   "org.hsqldb.jdbcDriver"
         :subprotocol "hsqldb"
         :subname     "file:db/welding_estimation_data"})

(defn- db-query [q]
  (with-connection db (with-query-results res [q] (doall res))))

(defn gases [process]
  (let [query (str "SELECT DISTINCT gas_or_stickout FROM "
                (str process "_deposition_rates"))
        res (db-query query)
        values (into [] (for [rec res] (:gas_or_stickout rec)))]
    values))

(defn electrodes [process gas]
  (let [query (str "SELECT DISTINCT electrode FROM "
                (str process "_deposition_rates")
                " WHERE gas_or_stickout = '" gas "'")
        res (db-query query)
        values (into [] (for [rec res]
	                  (if (= String (class (:electrode rec)))
                            (:electrode rec)
                            (rationalize (:electrode rec)))))]
    values))

(defn amps [process gas electrode]
  (let [query (str "SELECT DISTINCT amps FROM "
                (str process "_deposition_rates")
                " WHERE gas_or_stickout = '" gas "'"
                " AND electrode = "
                  (if (= String (class electrode))
                    (str "'" electrode "'")
                    (double electrode)))
        res (db-query query)
        values (into [] (for [rec res] (:amps rec)))]
    values))

(defn depositions [process gas electrode amps]
  (let [query (str "SELECT deposition_rate FROM "
                (str process "_deposition_rates")
                " WHERE gas_or_stickout = '" gas "'"
                " AND electrode = "
                  (if (= String (class electrode))
                    (str "'" electrode "'")
                    (double electrode))
                " AND amps = "
                  (if (= String (class amps))
                    (str "'" amps "'")
                    amps))
        res (db-query query)
        values (into [] (for [rec res] (:deposition_rate rec)))]
    values))
