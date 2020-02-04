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

## 