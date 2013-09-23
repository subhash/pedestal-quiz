(ns ^:shared pedestal-quiz.behavior
    (:require [clojure.string :as string]
              [io.pedestal.app.messages :as msg]
              [io.pedestal.app :as app]
              [io.pedestal.app.dataflow :as dataflow]))

(defn create-quiz [state message]
  (.log js/console state)
    nil)

(defn add-question [state message]
    (let [id (gensym "question")
          q {:text (:text message) :id id}] 
      (assoc state :questions (assoc (:questions state) id q))))

(defn edit-question [question {:keys [text]}]
  (assoc question :text text))

(defn edit-choice [choice {:keys [text]}]
  (assoc choice :text text))

(defn add-choice [question {:keys [text]}]
  (let [id (gensym "choice")
        c {:id id :text text}]
    (assoc question :choices (assoc (:choices question) id c))))

(defn init-main [_]
  [[:transform-enable [:quiz] 
    :add-question [{msg/topic [:quiz] (msg/param :text) {}}]]])

(defn new-question-enabler [inputs]
  (let [[ [[_ _ q _] text :as input ]]  (vec (dataflow/added-inputs inputs))]
  (if (not (nil? q)) 
    [[:transform-enable [:quiz :questions q] 
      :edit-question [{msg/topic [:quiz :questions q] :id q (msg/param :text) {}}]]
     [:transform-enable [:quiz :questions q] 
      :add-choice [{msg/topic [:quiz :questions q] :id q (msg/param :text) {}}]]])))

(defn new-choice-enabler [inputs]
  (let [[ [[_ _ q _ c _] text :as input ]]  (vec (dataflow/added-inputs inputs))]
  (if (not (nil? q)) 
    [[:transform-enable [:quiz :questions q :choices c] 
      :edit-choice [{msg/topic [:quiz :questions q :choices c] :id c (msg/param :text) {}}]]])))
  
(def quiz-app
  {:version 2
   :transform [[:create-quiz [:quiz] create-quiz]
               [:add-question [:quiz] add-question]
               [:edit-question [:quiz :questions :*] edit-question]
               [:add-choice [:quiz :questions :*] add-choice]
               [:edit-choice [:quiz :questions :* :choices :*] edit-choice]]
   :emit [{:init init-main} 
          [#{ [:quiz :questions :* :text]
              [:quiz :questions :* :choices :* :text]} (app/default-emitter [])]
          [#{ [:quiz :questions :* :text]}  new-question-enabler]
          [#{ [:quiz :questions :* :choices :* :text]}  new-choice-enabler]
          ]})
