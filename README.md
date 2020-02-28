![CI](https://github.com/jonathanliem94/BG-Manager/workflows/CI/badge.svg?branch=master)

## Use Case

- Allow users to search board games on BGG
- Add games to an in-app list (i.e. as a games to be played list)
- (TO-DO) Share said list to external services (i.e. share to other players in a session)

## Implementation Details

- Main Activity
  - Is a navigation drawer layout
  - Contains navigation host fragment
  - Toolbar only has a search view (for now)

- Search Fragment
  - Contains RecyclerView with ListAdapter
  - Clicking a result will navigate to a new fragment
  - ~~Layout file uses data-binding for modifying visibility~~
  - Uses view-binding to modify UI states
  - ViewModel uses `ObservableField`
    
- GameList Fragment
  - ViewModel uses `LiveData`

- BoardGameDetails Fragment
  - ~~Uses `findViewById` to set UI~~
  - Uses view-binding to modify UI
  - NestedScroll View for games with excessive details

- GameListUseCase
  - Handles gameList operations and emits GameList using `BehaviorSubject`

- NetworkUseCase
  - Handles all network calls (currently only: Search) and emits the results
  and searchStatus through `BehaviorSubject` and `PublishSubject` respectively

## Important Dependencies

- Native Navigation component for navigation
- RxJava/Kotlin/Android for reactive programming
- Dagger2 for Dependency Injection
  - Use of abstraction to handle references to components required by other components
  (see BaseActivity/BaseFragment)
- Picasso for loading images
- Retrofit2 for network calls
- Espresso for UI tests
- Mockito-Kotlin for Unit tests
