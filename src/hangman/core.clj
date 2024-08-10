(ns hangman.core
  (:require [hangman.helper :refer :all]))

(def categories ["countries" "capitals" "animals" "football-teams" "basketball-teams"])
(def categories-names ["Countries" "Capitals" "Animals" "Football Teams" "Basketball Teams"])

(defn current-time-millis
  []
  (System/currentTimeMillis))

(defn choose-category
  []
  (println "Choose a category:")
  (doseq [index (range (count categories))]
    (println (str (inc index) ". " (categories-names index))))
  (let [choice (read-line)]
    (if (contains? #{"1" "2" "3" "4" "5"} choice)
      (categories (dec (Integer/parseInt choice)))
      (do
        (println "The choice must be a integer and one of the categories, so the category will be random!")
        (categories (rand-int 5))))))

(defn mask-word
  [word guesses]
  (apply str (map #(cond
                     (= % \space) \space
                     (contains? guesses (clojure.string/lower-case %)) %
                     :else \_) word)))

(defn draw-hangman
  [wrong-guesses guesses wrong-letters]
  (let [stages [""
                " | \n | \n | \n | "
                " | \n | \n | \n | \n | \n | \n | \n | "
                " ====== \n | \n | \n | \n | \n | \n | \n | \n | "
                " ====== \n |    | \n |    O \n | \n | \n | \n | \n | \n | "
                " ====== \n |    | \n |    O \n |   /|\\ \n |  / | \\ \n |    |\n | \n | \n | "
                " ====== \n |    | \n |    O \n |   /|\\ \n |  / | \\ \n |    |\n |   / \\ \n |  /   \\ \n | "]]
    (println "------------------------------------")
    (println "            The Hangman")
    (println "------------------------------------")
    (println (stages wrong-guesses))
    (println "------------------------------------")
    (println (str "Correct letters: " (clojure.string/join ", " guesses)))
    (println (str "Wrong letters: " (clojure.string/join ", " wrong-letters)))
    (println "------------------------------------")))

(defn game-loop
  [word]
  (let [word-set (set (map #(clojure.string/lower-case %) (set word)))
        guesses (atom #{})
        wrong-guesses (atom 0)
        wrong-letters (atom #{})
        start-time (current-time-millis)]
    (loop []
      (println (mask-word word @guesses))
      (println "Enter a letter:")
      (let [guess (read-line)]
        (if-not (or (= guess nil) (= guess "") (= guess " "))
         (do
           (if (contains? word-set (clojure.string/lower-case (first guess)))
             (swap! guesses conj (clojure.string/lower-case (first guess)))
             (do
               (swap! wrong-guesses inc)
               (swap! wrong-letters conj (clojure.string/lower-case (first guess))))))
         (println "Try again!"))
        (draw-hangman @wrong-guesses @guesses @wrong-letters)
        (if (or (>= @wrong-guesses 6) (= word (mask-word word @guesses)))
          (do
            (println "Game over!")
            (println (str "The word was: " word))
            (if (= word (mask-word word @guesses))
              (do
                (println "Congratulations, you won!")
                (play-sound "win.wav"))
              (do
                (println "Better luck next time!")
                (play-sound "lose.wav")))
            (println (str "Time taken: " (int (/ (- (current-time-millis) start-time) 1000.0)) " seconds"))
            (println (str "Guesses: " (+ (count @guesses) (count @wrong-letters)))))
          (recur))))))

(defn exit
  [status]
  (System/exit status))

(defn -main
  []
  (let [category (choose-category)
        words (load-words-from-file category)
        word (rand-nth words)]
    (game-loop word)
    (println "Play again? y/n")
    (let [res (read-line)]
      (if (= (clojure.string/lower-case res) "y")
        (recur)
        (exit 0)))))