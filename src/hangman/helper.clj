(ns hangman.helper
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [hangman.api :as api])
  (:import [javax.sound.sampled AudioSystem Clip]))

(defn fetch-words-from-api [category]
  (cond
    (= category "countries") (api/fetch-countries)
    (= category "capitals") (api/fetch-capitals)
    (= category "animals") (api/fetch-animals)
    (= category "football-teams") (api/fetch-football-teams)
    (= category "basketball-teams") (api/fetch-basketball-teams)
    ))

(defn save-words-to-file [category words]
  (spit (str "resources/" category ".txt") (json/generate-string words)))

(defn load-words-from-file [category]
  (let [file (str "resources/" category ".txt")]
    (if (.exists (io/file file))
      (json/parse-string (slurp file) true)
      (let [words (fetch-words-from-api category)]
        (save-words-to-file category words)
        words))))

(defn play-sound
  [sound]
  (future
    (let [clip (AudioSystem/getClip)
          audio-input-stream (AudioSystem/getAudioInputStream (io/resource sound))]
    (.open clip audio-input-stream)
    (.start clip)
    (Thread/sleep (long (/ (.getMicrosecondLength clip) 1000)))
    (.close clip))))