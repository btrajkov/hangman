
# The Hangman

## Table of Contents
- [Overview](#overview)
- [Project Structure](#project-structure)
- [How to Play](#how-to-play)
- [Functions Overview](#functions-overview)
  - [core.clj](#coreclj)
  - [api.clj](#apiclj)
  - [helper.clj](#helperclj)
  - [core_test.clj](#core_testclj)
  - [api_test.clj](#api_testclj)
  - [helper_test.clj](#helper_testclj)
- [Installation and Running the Game](#installation-and-running-the-game)
- [Running Tests](#running-tests)
- [Contributions](#contributions)
- [License](#license)

## Overview

This project implements a terminal-based Hangman game in Clojure. The game allows players to select from various categories, including countries, capitals, animals, football teams, and basketball teams. The words in these categories are fetched either from local files or external APIs. The project is structured into multiple Clojure namespaces, each with a specific purpose, ranging from the core game logic to API interactions and helper utilities. Additionally, comprehensive tests are provided to ensure the reliability and correctness of the game. Finally, this project is built using Leiningen, a build automation and dependency management tool for the simple configuration of software projects written in the Clojure programming language.

## Project Structure

- **`src/hangman/core.clj`**: Contains the main game logic, including functions for category selection, word masking, and hangman drawing.
- **`src/hangman/api.clj`**: Handles interactions with external APIs to fetch data for the game categories.
- **`src/hangman/helper.clj`**: Contains helper functions for fetching, saving, and loading words, as well as playing sound effects.
- **`test/hangman/core_test.clj`**: Contains tests for the core game functions.
- **`test/hangman/api_test.clj`**: Contains tests for the API-related functions.
- **`test/hangman/helper_test.clj`**: Contains tests for the helper functions.
- **`resources/`**: Contains .txt files where the fetched words for each category are being saved, as well as the sounds used in the game.
- **`project.clj`**: Defines all the dependencies used for development.

## How to Play

These are the elementary steps of the game:

1. **Category Selection**: Players can choose from several categories such as Countries, Capitals, Animals, Football Teams, and Basketball Teams. If an invalid choice is made, a random category is selected.<br />![Screenshot 2024-08-10 144639.png](resources%2Fscreenshots%2FScreenshot%202024-08-10%20144639.png)
2. **Guessing the Word**: The game randomly selects a word from the chosen category. The word is masked, showing only the correctly guessed letters.<br />![Screenshot 2024-08-10 144715.png](resources%2Fscreenshots%2FScreenshot%202024-08-10%20144715.png)
3. **Making Guesses**: Players input their guesses one letter at a time. Incorrect guesses result in parts of a hangman figure being drawn.<br />![Screenshot 2024-08-10 144808.png](resources%2Fscreenshots%2FScreenshot%202024-08-10%20144808.png)
4. **Winning or Losing**: The game continues until the player either guesses the word correctly or the hangman figure is fully drawn.<br />![Screenshot 2024-08-10 144834.png](resources%2Fscreenshots%2FScreenshot%202024-08-10%20144834.png)

## Functions Overview

### core.clj

- **`current-time-millis`**: Returns the current time in milliseconds.
- **`choose-category`**: Prompts the player to select a category, validates the input, and selects a category either based on the player's choice or randomly, if the player's input is invalid.
- **`mask-word`**: Masks the word, revealing only the correctly guessed letters while hiding the others.
- **`draw-hangman`**: Draws the hangman figure based on the number of incorrect guesses and displays correctly and incorrectly guessed letters.
- **`game-loop`**: Represents the central point of the game; prompts the player to guess, validates the input, performs the check and handles the end of the game by displaying the appropriate message and sound.
- **`-main`**: Starts the game by calling the previous functions; when one iteration of the game is finished, prompts the player to play again.

### api.clj

- **`fetch-countries`**: Fetches a list of countries from an external API.
- **`fetch-capitals`**: Fetches a list of capitals from an external API.
- **`fetch-animals`**: Fetches a list of animals from an external API.
- **`fetch-football-teams`**: Fetches a list of football teams from an external API.
- **`fetch-basketball-teams`**: Fetches a list of basketball teams from an external API.

clj-http, a Clojure HTTP library is used for API calls.

### helper.clj

- **`fetch-words-from-api`**: Fetches words from the API based on the selected category.
- **`save-words-to-file`**: Saves the fetched words to a local file for future use.
- **`load-words-from-file`**: Loads words from a file if available, otherwise, fetches from the API and saves them.
- **`play-sound`**: Plays a sound effect, such as a win or a loss notification at the end of the game.

Cheshire library is used for JSON encode and decoding; clojure.java.io namespace is used for writing to and reading from the files; javax.sound.sampled Java package is used for playing the sounds.

### core_test.clj

- **Tests for `choose-category`**: Verifies that the category selection prompts correctly, lists all categories, handles valid input, and deals with invalid or random inputs appropriately.
- **Tests for `mask-word`**: Verifies that all unknown letters are masked, that all correctly guessed letters are revealed, that both uppercase and lowercase letters are handled, that spaces are handled and that the word is revealed when every letter is guessed.
- **Tests for `draw-hangman`**: Verifies that the hangman as well as the correctly and incorrectly guessed words are appropriately displayed at all stages of the game.
- **Tests for `game-loop`**: Verifies that the positive and negative game results are appropriately handled.
- **Tests for `-main`**: Verifies that the game is run correctly once, but also rerun more than once.

### api_test.clj

- **Tests for `fetch-countries`**: Verifies that the country names are successfully fetched and that the errors are handled accordingly.
- **Tests for `fetch-capitals`**: Verifies that the capital names are successfully fetched and that the errors are handled accordingly.
- **Tests for `fetch-animals`**: Verifies that the animal names are successfully fetched and that the errors are handled accordingly.
- **Tests for `fetch-football-teams`**: Verifies that the football team names are successfully fetched and that the errors are handled accordingly.
- **Tests for `fetch-basketball-teams`**: Verifies that the basketball team names are successfully fetched and that the errors are handled accordingly.

### helper_test.clj

- **Tests for `fetch-words-from-api`**: Ensures that words are correctly fetched based on the selected category, and that the function interacts correctly with the mocked API responses.
- **Tests for `save-words-to-file`**: Ensures that the words are appropriately saved to a file.
- **Tests for `load-words-from-file`**: Ensures that the words are correctly loaded from the existing file, or that the function correctly fetches the words from the mocked API response and saves them to a file.

## Installation and Running the Game

1. **Clone the repository**:
   ```sh
   git clone https://github.com/btrajkov/hangman.git
   ```
2. **Navigate to the project directory**:
   ```sh
   cd hangman
   ```
3. **In order to run the game [Leiningen](https://leiningen.org/#install) must be installed. Next, use the following command:**
   ```sh
   lein run
   ```

## Running Tests

To run the tests, use the following command:

```sh
lein midje
```

This will execute all the tests defined in `core_test.clj`, `api_test.clj`, and `helper_test.clj`, ensuring that the game functions correctly.
The tests were written using [Midje](https://github.com/marick/Midje), a test framework for Clojure.

## Contributions

As this is an academic project, contributions are welcome for learning and improving the codebase. Please feel free to fork the repository and submit pull requests with improvements or additional features.

## License

This project is available under the Eclipse Public License v2.
