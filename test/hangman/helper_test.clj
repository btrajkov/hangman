(ns hangman.helper-test
  (:require [midje.sweet :refer :all]
            [hangman.helper :refer :all]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [hangman.api :as api]))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; fetch-words-from-api tests
(fact "fetch-words-from-api should fetch words based on a category"
      (with-redefs [api/fetch-countries (fn [] ["Country1" "Country2" "Country3"])
                    api/fetch-capitals (fn [] ["Capital1" "Capital2" "Capital3"])
                    api/fetch-animals (fn [] ["Animal1" "Animal2" "Animal3"])
                    api/fetch-football-teams (fn [] ["Football-Team1" "Football-Team2" "Football-Team3"])
                    api/fetch-basketball-teams (fn [] ["Basketball-Team1" "Basketball-Team2" "Basketball-Team3"])]
        (fetch-words-from-api "countries") => ["Country1" "Country2" "Country3"]
        (fetch-words-from-api "capitals") => ["Capital1" "Capital2" "Capital3"]
        (fetch-words-from-api "animals") => ["Animal1" "Animal2" "Animal3"]
        (fetch-words-from-api "football-teams") => ["Football-Team1" "Football-Team2" "Football-Team3"]
        (fetch-words-from-api "basketball-teams") => ["Basketball-Team1" "Basketball-Team2" "Basketball-Team3"]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; save-words-to-file tests
(fact "save-words-to-file should save words to a file"
      (let [file "resources/test.txt"
            words ["Word1" "Word2" "Word3"]]
        (save-words-to-file "test" words)
        (slurp file) => (json/generate-string words)
        (io/delete-file file)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; load-words-from-file tests
(fact "load-words-from-file should load words from an existing file"
      (let [file "resources/test.txt"
            words ["Word1" "Word2" "Word3"]]
        (spit file (json/generate-string words))
        (load-words-from-file "test") => words
        (io/delete-file file)))

(fact "load-words-from-file should fetch words from API and save them if file does not exist"
      (with-redefs [fetch-words-from-api (fn [_] ["Word1" "Word2" "Word3"])]
        (let [file "resources/test.txt"]
          (load-words-from-file "test") => ["Word1" "Word2" "Word3"]
          (.exists (io/file file)) => true
          (io/delete-file file))))