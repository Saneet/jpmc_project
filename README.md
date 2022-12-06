# Coding Challenge: NYC Schools

# Requirements
[See full requirements here.](REQUIREMENTS.md)

# Extra Features
## Paged querying of data
https://raw.githubusercontent.com/Saneet/jpmc_project/main/paging.mp4

## Search
NOTE: Search results are also paged like above
https://raw.githubusercontent.com/Saneet/jpmc_project/main/Search.mp4

# Basic Demo
NOTE: Can't demo screen rotation due to video recording limitations
https://raw.githubusercontent.com/Saneet/jpmc_project/main/walkthrough.mp4

# Tech Stack
1. MVVM
2. Dagger
3. Retrofit
4. RxJava
5. Leakcanary

# Testing
Wrote some tests for the List ViewModel, Repository and the Network API interface.

# Features I could have implemented with more time
1. Caching of previous results for faster warm launch using Room SQLite DB.
2. Storing last search term and UI state in SharedPreferences.
3. Launching maps, phone calling, browser and email on the details page.
4. More test coverage for edge cases.