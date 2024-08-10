(ns hangman.api
  (:require [clj-http.client :as client]))

(def apis ["https://restcountries.com/v3.1/all"
           "https://restcountries.com/v3.1/region/europe"
           "https://restcountries.com/v3.1/region/africa"
           "https://restcountries.com/v3.1/region/americas"
           "https://restcountries.com/v3.1/region/asia"
           "https://restcountries.com/v3.1/region/oceania"
           "https://freetestapi.com/api/v1/animals"
           "https://v3.football.api-sports.io/teams?league=39&season=2024"
           "https://v3.football.api-sports.io/teams?league=140&season=2024"
           "https://v3.football.api-sports.io/teams?league=61&season=2024"
           "https://v3.football.api-sports.io/teams?league=78&season=2024"
           "https://v3.football.api-sports.io/teams?league=135&season=2024"
           "https://api-basketball.p.rapidapi.com/teams?league=12&season=2023-2024"
           "https://api-basketball.p.rapidapi.com/teams?league=120&season=2024"])

(def api-key-football "bb3ff59c368a2d33d612bb0e809c1cc2")
(def api-key-basketball "5269bce1eemsh93b130aadb62990p16c61ejsne3526a5011fe")

(defn fetch-countries
  []
  (let [url (get apis 0)
        response (client/get url {:as :json})]
    (if (= (:status response) 200)
      (map #(:common (:name %)) (:body response))
      (throw (Exception. (str "Failed to fetch data, status: " (:status response)))))))

(defn fetch-capitals []
  (let [urls [(get apis 1) (get apis 2) (get apis 3) (get apis 4) (get apis 5)]
        responses (reduce (fn [res url]
                            (let [response (client/get url {:as :json})]
                              (if (= (:status response) 200)
                                (conj res (:body response))
                                (throw (Exception. (str "Failed to fetch data, status: " (:status response)))))))
                          []
                          urls)]
    (map #(get (:capital %) 0) (flatten responses))))

(defn fetch-animals []
  (let [url (get apis 6)
        response (client/get url {:as :json})]
    (if (= (:status response) 200)
      (map #(:name %) (:body response))
      (throw (Exception. (str "Failed to fetch data, status: " (:status response)))))))

(defn fetch-football-teams []
  (let [urls [(get apis 7) (get apis 8) (get apis 9) (get apis 10) (get apis 11)]
        responses (reduce (fn [res url]
                            (let [response (client/get url {:headers {"x-rapidapi-key" api-key-football} :as :json})]
                              (if (= (:status response) 200)
                                (conj res (:response (:body response)))
                                (throw (Exception. (str "Failed to fetch data, status: " (:status response)))))))
                          []
                          urls)]
    (map #(:name (:team %)) (flatten responses))))

(defn fetch-basketball-teams []
  (let [urls [(get apis 12) (get apis 13)]
        responses (reduce (fn [res url]
                            (let [response (client/get url {:headers {"x-rapidapi-key" api-key-basketball} :as :json})]
                              (if (= (:status response) 200)
                                (conj res (:response (:body response)))
                                (throw (Exception. (str "Failed to fetch data, status: " (:status response)))))))
                          []
                          urls)]
    (filter #(not (or (= % "West") (= % "East")))
            (map #(:name %) (flatten responses)))))