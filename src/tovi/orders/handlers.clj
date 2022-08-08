(ns tovi.orders.handlers
  (:require [tovi.exceptions :as exc]
            [ring.util.response :as rr]
            [tovi.orders.db :as database]))

(defn create-product [{:keys [parameters db]}]
  (try
    (if-let [product (database/insert-product db (:body parameters))]
      (rr/created "" product)
      (rr/status {:body [{:error-key :412
                          :msg "Product could not be added"}]} 412))
    (catch Exception e
      (exc/handle-exception e))))

(defn get-products [{:keys [parameters db]}]
  (try
    (when-let [products (database/get-products db)]
      (rr/response products))
    (catch Exception e
      (exc/handle-exception e))))

(defn get-product-by-id [{:keys [parameters db]}]
  (try
    (let [id (-> parameters :path :id)]
      (if-let [product (database/get-product-by-id db id)]
        (rr/response product)
        (rr/not-found [{:error-key :404
                        :msg (format "Product with id %s does not exist" id)}])))
    (catch Exception e
      (exc/handle-exception e))))

(defn update-product [{:keys [parameters db]}]
  (try
    (let [id (-> parameters :path :id) body (:body parameters)
          result (database/update-product db id body)]
      (if (not= 0 (:next.jdbc/update-count result))
        (rr/response {:success (format "Product with id %s successfully updated" id)})
        (rr/status {:body [{:error-key :412
                            :msg (format "Product with id %s could not be updated" id)}]} 412)))
    (catch Exception e
      (exc/handle-exception e))))

(defn delete-product [{:keys [parameters db]}]
  (try
    (let [id (-> parameters :path :id)
          result (database/delete-product db id)]
      (if (not= 0 (:next.jdbc/update-count result))
        (rr/response {:success (format "Product with id %s has been successfully deleted" id)})
        (rr/status {:body [{:error-key :412
                            :msg (format "Product with id %s could not be deleted" id)}]} 412)))
    (catch Exception e
      (exc/handle-exception e))))

(defn create-order [{:keys [parameters db]}]
  {:status 200 :body {:result :ok}})

