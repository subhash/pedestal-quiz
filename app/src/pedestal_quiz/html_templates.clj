(ns pedestal-quiz.html-templates
  (:use [io.pedestal.app.templates :only [tfn dtfn tnodes]]))

(defmacro pedestal-quiz-templates
  []
  ;; Extract the 'hello' template from the template file pedestal-quiz.html.
  ;; The 'dtfn' function will create a dynamic template which can be
  ;; updated after it has been attached to the DOM.
  ;;
  ;; To see how this template is used, refer to
  ;;
  ;; app/src/pedestal_quiz/rendering.cljs
  ;;
  ;; The last argument to 'dtfn' is a set of fields that should be
  ;; treated as static fields (may only be set once). Dynamic templates
  ;; use ids to set values so you cannot dynamically set an id.
  {:quiz (dtfn (tnodes "quiz.html" "quiz"))
   :question (dtfn (tnodes "quiz.html" "question"))
   :choice-input (tfn (tnodes "quiz.html" "choice-input"))
   :choice (dtfn (tnodes "quiz.html" "choice"))})

;; Note: this file will not be reloaded automatically when it is changed.
