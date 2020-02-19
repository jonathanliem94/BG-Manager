![CI](https://github.com/jonathanliem94/BG-Manager/workflows/CI/badge.svg?branch=master)

## Use Case

- Allow users to search board games on BGG
- (TO-DO) Add games to an in-app list (i.e. as a games to be played list)
- (TO-DO) Share said list to external services (i.e. share to other players in a session)

## Implementation Details

- Main Activity
    - Is a navigation drawer layout
    - Contains navigation host fragment
    - Toolbar only has a search view (for now)

- Search Fragment
    - Contains RecyclerView with ListAdapter
    - Clicking a result will navigate to a new fragment
    - Layout file uses data-binding for modifying visibility
    - ViewModel uses `ObservableField`
    
- GameList Fragment
    - ViewModel uses `LiveData`

- Shared View Model
    - Handles searchResults and game list through `BehaviourSubject` and `PublishSubject`

## Important Dependencies

- Native Navigation component for navigation
- RxJava/Kotlin/Android for reactive programming
- Dagger2 for Dependency Injection
- Retrofit2 for network calls
- Espresso for UI tests
- Mockito-Kotlin for Unit tests
