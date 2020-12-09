(ns swig.three.state)

(def three-entities (atom {}))

(defn three-listener [{:keys [tx-data tx-data]}]
  (let [entities @three-entities]
    (doseq [d tx-data]
      (case (.-a d)
        :three.object/position
        (when (.-added d)
          (set! (.-position (get entities (.-e d))) (into-array (.-v d))))

        :three.object/rotation
        (when (.-added d)
          (set! (.-rotation (get entities (.-e d))) (into-array (.-v d))))

        :three.object/scale
        (when (.-added d)
          (set! (.-scale (get entities (.-e d))) (.-v d)))

        :three.object/cast-shadow
        (if (.-added d)
          (set! (.-castShadow (get entities (.-e d))) (.-v d))
          (set! (.-castShadow (get entities (.-e d))) (.-v d)))

        :three.object/receive-shadow
        (if (.-added d)
          (set! (.-receiveShadow (get entities (.-e d))) (.-v d))
          (set! (.-recieveShadow (get entities (.-e d))) false))

        :else nil))))