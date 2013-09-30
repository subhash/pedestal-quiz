(ns pedestal-quiz.start
  (:require [io.pedestal.app.protocols :as p]
            [io.pedestal.app :as app]
            [io.pedestal.app.render.push :as push-render]
            [io.pedestal.app.render :as render]
            [io.pedestal.app.messages :as msg]
            [pedestal-quiz.behavior :as behavior]
            [pedestal-quiz.rendering :as rendering]))

(defn create-app [render-config]
  (let [app (app/build behavior/quiz-app)
        render-fn (push-render/renderer "content" render-config render/log-fn)
        app-model (render/consume-app-model app render-fn)]
    (app/begin app)
    (p/put-message (:input app) {msg/type :create-quiz msg/topic [:quiz]})
    {:app app :app-model app-model}))

(defn ^:export main []
  (create-app (rendering/render-config)))