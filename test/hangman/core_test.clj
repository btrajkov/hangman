(ns hangman.core-test
  (:require [midje.sweet :refer :all]
            [hangman.core :refer :all]
            [hangman.helper :refer :all]))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; choose-category tests
(fact "choose-category should prompt for category selection"
      (with-redefs [read-line (fn [] "1")]
        (with-out-str (choose-category)) => (contains "Choose a category:")))

(fact "choose-category should list all categories"
      (with-redefs [read-line (fn [] "1")]
        (let [output (with-out-str (choose-category))]
          output => (contains "1. Countries")
          output => (contains "2. Capitals")
          output => (contains "3. Animals")
          output => (contains "4. Football Teams")
          output => (contains "5. Basketball Teams"))))

(fact "choose-category should handle a valid input"
      (let [choice (str (inc (rand-int 5)))]
       (with-redefs [read-line (fn [] choice)]
        (choose-category) => (categories (dec (Integer/parseInt choice))))))

(fact "choose-category should return a random category for invalid input"
      (with-redefs [read-line (fn [] "invalid")
                    rand-int (fn [_] 2)]
        (let [output (with-out-str (choose-category))]
          output => (contains "The choice must be a integer and one of the categories, so the category will be random!")
          (choose-category) => "animals")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; mask-word tests
(fact "mask-word should mask all letters that have not been guessed"
      (mask-word "hello" #{}) => "_____")

(fact "mask-word should reveal guessed letters"
      (mask-word "hello" #{"h" "e" "o"}) => "he__o")

(fact "mask-word should handle uppercase and lowercase letters correctly"
      (mask-word "Hello" #{"h" "e" "o"}) => "He__o")

(fact "mask-word should handle spaces correctly"
      (mask-word "hello world" #{"h" "e" "o"}) => "he__o _o___")

(fact "mask-word should reveal all letters if all have been guessed"
      (mask-word "hello" #{"h" "e" "l" "o"}) => "hello")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; draw-hangman tests
(fact "draw-hangman should display the initial stage correctly"
      (with-out-str
        (draw-hangman 0 #{} #{})) => (contains "------------------------------------\r\n            The Hangman\r\n------------------------------------\r\n\r\n------------------------------------\r\nCorrect letters: \r\nWrong letters: \r\n------------------------------------\r\n"))

(fact "draw-hangman should display the second stage correctly"
      (with-out-str
        (draw-hangman 1 #{} #{})) => (contains "------------------------------------\r\n            The Hangman\r\n------------------------------------\r\n | \n | \n | \n | \r\n------------------------------------\r\nCorrect letters: \r\nWrong letters: \r\n------------------------------------\r\n"))

(fact "draw-hangman should display the middle stage correctly"
      (with-out-str
        (draw-hangman 3 #{} #{})) => (contains "------------------------------------\r\n            The Hangman\r\n------------------------------------\r\n ====== \n | \n | \n | \n | \n | \n | \n | \n | \r\n------------------------------------\r\nCorrect letters: \r\nWrong letters: \r\n------------------------------------\r\n"))

(fact "draw-hangman should display the final stage correctly"
      (with-out-str
        (draw-hangman 6 #{} #{})) => (contains "------------------------------------\r\n            The Hangman\r\n------------------------------------\r\n ====== \n |    | \n |    O \n |   /|\\ \n |  / | \\ \n |    |\n |   / \\ \n |  /   \\ \n | \r\n------------------------------------\r\nCorrect letters: \r\nWrong letters: \r\n------------------------------------\r\n"))

(fact "draw-hangman should display correct and wrong letters correctly"
      (with-out-str
        (draw-hangman 2 #{"a" "b"} #{"c" "d"})) => (contains "------------------------------------\r\n            The Hangman\r\n------------------------------------\r\n | \n | \n | \n | \n | \n | \n | \n | \r\n------------------------------------\r\nCorrect letters: a, b\r\nWrong letters: d, c\r\n------------------------------------\r\n"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; game-loop tests
(fact "game-loop should handle winning the game"
      (let [inputs (atom ["h" "e" "l" "o"])]
        (with-redefs [read-line (fn [] (let [input (first @inputs)]
                                         (swap! inputs rest)
                                         input))
                      draw-hangman (fn [_ _ _] (println "Drawing hangman"))
                      current-time-millis (fn [] 1000)
                      play-sound (fn [sound] (println (str "Playing " sound " sound")))]
          (let [output (with-out-str (game-loop "hello"))]
            output => (contains "_____")
            output => (contains "he___")
            output => (contains "hell_")
            output => (contains "hello")
            output => (contains "Game over!")
            output => (contains "The word was: hello")
            output => (contains "Congratulations, you won!")
            output => (contains "Playing win.wav sound")
            output => (contains "Time taken: 0 seconds")
            output => (contains "Guesses: 4")))))

(fact "game-loop should handle losing the game"
      (let [inputs (atom ["x" "y" "z" "q" "w" "r"])]
        (with-redefs [read-line (fn [] (let [input (first @inputs)]
                                         (swap! inputs rest)
                                         input))
                      draw-hangman (fn [_ _ _] (println "Drawing hangman"))
                      current-time-millis (fn [] 1000)
                      play-sound (fn [sound] (println (str "Playing " sound " sound")))]
          (let [output (with-out-str (game-loop "hello"))]
            output => (contains "_____")
            output => (contains "Drawing hangman")
            output => (contains "Game over!")
            output => (contains "The word was: hello")
            output => (contains "Better luck next time!")
            output => (contains "Playing lose.wav sound")
            output => (contains "Time taken: 0 seconds")
            output => (contains "Guesses: 6")))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; -main tests
(fact "main function should run the game loop and handle replay"
      (let [inputs (atom ["y" "n"])
            game-inputs (atom ["w" "o" "r" "d"])]
        (with-redefs [choose-category (fn [] "category")
                      load-words-from-file (fn [_] ["word"])
                      read-line (fn []
                                  (if (empty? @game-inputs)
                                    (let [input (first @inputs)]
                                      (swap! inputs rest)
                                      (swap! game-inputs into ["w" "o" "r" "d"])
                                      input)
                                    (let [input (first @game-inputs)]
                                      (swap! game-inputs rest)
                                      input)))
                      exit (fn [_] (println "Exiting..."))
                      draw-hangman (fn [_ _ _] (println "Drawing hangman"))
                      play-sound (fn [sound] (println (str "Playing " sound " sound")))]
          (let [output (with-out-str (-main))]
            output => (contains "Enter a letter:")
            output => (contains "Drawing hangman")
            output => (contains "word")
            output => (contains "Congratulations, you won!")
            output => (contains "Playing win.wav sound")
            output => (contains "Exiting...")))))

(fact "main function should run and handle the game loop only once"
      (let [inputs (atom ["n"])
            game-inputs (atom ["w" "o" "r" "d"])]
        (with-redefs [choose-category (fn [] "category")
                      load-words-from-file (fn [_] ["word"])
                      read-line (fn []
                                  (if (empty? @game-inputs)
                                    (let [input (first @inputs)]
                                      (swap! inputs rest)
                                      (swap! game-inputs into ["w" "o" "r" "d"])
                                      input)
                                    (let [input (first @game-inputs)]
                                      (swap! game-inputs rest)
                                      input)))
                      exit (fn [_] (println "Exiting..."))
                      draw-hangman (fn [_ _ _] (println "Drawing hangman"))
                      play-sound (fn [sound] (println (str "Playing " sound " sound")))]
          (let [output (with-out-str (-main))]
            output => (contains "Enter a letter:")
            output => (contains "Drawing hangman")
            output => (contains "word")
            output => (contains "Congratulations, you won!")
            output => (contains "Playing win.wav sound")
            output => (contains "Exiting...")))))