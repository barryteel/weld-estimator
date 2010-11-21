(ns com.tripotamus.util.db
  (:use (clojure.contrib [sql :only (with-connection with-query-results)])))

(def db {:classname   "org.hsqldb.jdbcDriver"
         :subprotocol "hsqldb"
         :subname     "file:db/welding_estimation_data"})

(defn- db-query [q]
  (with-connection db (with-query-results res [q] (doall res))))

(defn gases [process]
  (let [query (str "SELECT DISTINCT shielding_gas FROM "
                (str process "_deposition_rates"))
        res (db-query query)
        values (into [] (for [rec res] (:shielding_gas rec)))]
    values))

(defn electrodes [process gas]
  (let [query (str "SELECT DISTINCT electrode FROM "
                (str process "_deposition_rates")
                " WHERE shielding_gas = '" gas "'")
        res (db-query query)
        values (into [] (for [rec res] (rationalize (:electrode rec))))]
    values))

(defn amps [process gas electrode]
  (let [query (str "SELECT amps_of_current FROM "
                (str process "_deposition_rates")
                " WHERE shielding_gas = '" gas "'"
                " AND electrode = " (double electrode))
        res (db-query query)
        values (into [] (for [rec res] (:amps_of_current rec)))]
    values))

