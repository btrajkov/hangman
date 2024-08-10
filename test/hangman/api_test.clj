(ns hangman.api-test
  (:require [midje.sweet :refer :all]
            [hangman.api :refer :all]
            [clj-http.client :as client]))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; fetch-countries tests
(fact "fetch-countries should return a list of country names on successful fetch"
      (with-redefs [client/get (fn [_ _] {:status 200 :body [{:name {:common "Country1"}}
                                                             {:name {:common "Country2"}}
                                                             {:name {:common "Country3"}}]})]
        (fetch-countries) => '("Country1" "Country2" "Country3")))

(fact "fetch-countries should throw an exception on failed fetch"
      (with-redefs [client/get (fn [_ _] {:status 500})]
        (fetch-countries) => (throws Exception "Failed to fetch data, status: 500")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; fetch-capitals tests
(fact "fetch-capitals should return a list of capital names on successful fetch"
      (with-redefs [client/get (fn [url _]
                                 (case url
                                   "https://restcountries.com/v3.1/region/europe" {:status 200 :body [{:capital ["Capital1"]}]}
                                   "https://restcountries.com/v3.1/region/africa" {:status 200 :body [{:capital ["Capital2"]}]}
                                   "https://restcountries.com/v3.1/region/americas" {:status 200 :body [{:capital ["Capital3"]}]}
                                   "https://restcountries.com/v3.1/region/asia" {:status 200 :body [{:capital ["Capital4"]}]}
                                   "https://restcountries.com/v3.1/region/oceania" {:status 200 :body [{:capital ["Capital5"]}]}
                                   {:status 404 :body "Not Found"}))]
        (fetch-capitals) => '("Capital1" "Capital2" "Capital3" "Capital4" "Capital5")))

(fact "fetch-capitals should throw an exception on failed fetch"
      (with-redefs [client/get (fn [url _]
                                 (case url
                                   "https://restcountries.com/v3.1/region/europe" {:status 200 :body [{:capital ["Capital1"]}]}
                                   "https://restcountries.com/v3.1/region/africa" {:status 200 :body [{:capital ["Capital2"]}]}
                                   "https://restcountries.com/v3.1/region/americas" {:status 500 :body "Internal Server Error"}
                                   "https://restcountries.com/v3.1/region/asia" {:status 200 :body [{:capital ["Capital4"]}]}
                                   "https://restcountries.com/v3.1/region/oceania" {:status 200 :body [{:capital ["Capital5"]}]}
                                   {:status 404 :body "Not Found"}))]
        (fetch-capitals) => (throws Exception "Failed to fetch data, status: 500")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; fetch-animals tests
(fact "fetch-animals should return a list of animal names on successful fetch"
      (with-redefs [client/get (fn [_ _] {:status 200 :body [{:name "Animal1"}
                                                             {:name "Animal2"}
                                                             {:name "Animal3"}]})]
        (fetch-animals) => '("Animal1" "Animal2" "Animal3")))

(fact "fetch-animals should throw an exception on failed fetch"
      (with-redefs [client/get (fn [_ _] {:status 500})]
        (fetch-animals) => (throws Exception "Failed to fetch data, status: 500")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; fetch-football-teams tests
(fact "fetch-football-teams should return a list of football team names on successful fetch"
      (with-redefs [client/get (fn [url _]
                                 (case url
                                   "https://v3.football.api-sports.io/teams?league=39&season=2024" {:status 200 :body {:response [{:team {:name "Team1"}}]}}
                                   "https://v3.football.api-sports.io/teams?league=140&season=2024" {:status 200 :body {:response [{:team {:name "Team2"}}]}}
                                   "https://v3.football.api-sports.io/teams?league=61&season=2024" {:status 200 :body {:response [{:team {:name "Team3"}}]}}
                                   "https://v3.football.api-sports.io/teams?league=78&season=2024" {:status 200 :body {:response [{:team {:name "Team4"}}]}}
                                   "https://v3.football.api-sports.io/teams?league=135&season=2024" {:status 200 :body {:response [{:team {:name "Team5"}}]}}
                                   {:status 404 :body "Not Found"}) )]
        (fetch-football-teams) => '("Team1" "Team2" "Team3" "Team4" "Team5")))

(fact "fetch-football-teams should throw an exception on failed fetch"
      (with-redefs [client/get (fn [url _]
                                 (case url
                                   "https://v3.football.api-sports.io/teams?league=39&season=2024" {:status 200 :body {:response [{:team {:name "Team1"}}]}}
                                   "https://v3.football.api-sports.io/teams?league=140&season=2024" {:status 200 :body {:response [{:team {:name "Team2"}}]}}
                                   "https://v3.football.api-sports.io/teams?league=61&season=2024" {:status 500 :body "Internal Server Error"}
                                   "https://v3.football.api-sports.io/teams?league=78&season=2024" {:status 200 :body {:response [{:team {:name "Team4"}}]}}
                                   "https://v3.football.api-sports.io/teams?league=135&season=2024" {:status 200 :body {:response [{:team {:name "Team5"}}]}}
                                   {:status 404 :body "Not Found"}) )]
        (fetch-football-teams) => (throws Exception "Failed to fetch data, status: 500")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; fetch-basketball-teams tests
(fact "fetch-basketball-teams should return a list of basketball team names excluding 'West' and 'East' on successful fetch"
      (with-redefs [client/get (fn [url _]
                                 (case url
                                   "https://api-basketball.p.rapidapi.com/teams?league=12&season=2023-2024" {:status 200 :body {:response [{:name "Team1"}]}}
                                   "https://api-basketball.p.rapidapi.com/teams?league=120&season=2024" {:status 200 :body {:response [{:name "Team2"}]}}
                                   {:status 404 :body "Not Found"}) )]
        (fetch-basketball-teams) => '("Team1" "Team2")))

(fact "fetch-basketball-teams should throw an exception on failed fetch"
      (with-redefs [client/get (fn [url _]
                                 (case url
                                   "https://api-basketball.p.rapidapi.com/teams?league=12&season=2023-2024" {:status 200 :body {:response [{:name "Team1"}]}}
                                   "https://api-basketball.p.rapidapi.com/teams?league=120&season=2024" {:status 500 :body "Internal Server Error"}
                                   {:status 404 :body "Not Found"}) )]
        (fetch-basketball-teams) => (throws Exception "Failed to fetch data, status: 500")))