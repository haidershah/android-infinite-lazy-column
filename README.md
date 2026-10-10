# Android Infinite Lazy Column

A Kotlin / Jetpack Compose app that displays Pokémon from [PokéAPI](https://pokeapi.co/) using a reusable, generic `InfiniteLazyColumn` component.

## Progress

### 0. Project setup
- [x] Create Compose project (`com.haidershah.pokedex`, minSdk 24)
- [x] Initialize git and push to GitHub
- [ ] Add dependencies (networking, serialization, lifecycle/ViewModel, testing)

### 1. Networking
- [x] Model the paginated API response (`count`, `next`, `previous`, `results`)
- [x] Create API service for `https://pokeapi.co/api/v2/pokemon?limit=10&offset=0`
- [x] Page size back to `limit=10` per the prompt
- [x] Load subsequent pages using the `next` URL (Retrofit `@Url`)
- [ ] Handle failures gracefully (no connectivity, HTTP errors, malformed responses) — catch in ViewModel, store in UI state

### 2. Repository & caching
- [x] Repository layer between network and ViewModel
- [ ] Implement caching strategy
- [ ] Document what is cached, why, and tradeoffs

### 3. State management
- [x] Define UI state: items + sealed `LoadState` (`Idle`, `Loading`, `Error`, `EndReached`)
- [x] Derive loaded count from `items.size`
- [x] ViewModel exposes state and a load action that appends to the list
- [x] Prevent duplicate requests while a load is in progress (`LoadState.Loading` guard)
- [x] Handle end of list (`next == null`) — stop requesting
- [ ] Initial load fills the screen (example video starts at 20 = two pages)
- [ ] Retry after error

### 4. `InfiniteLazyColumn` component
- [x] Generic over item type (no Pokémon knowledge)
- [x] Triggers load-more when the user nears the end of the list (prefetch threshold, so the user never hits the end)
- [ ] Loading, success, and error states
- [ ] Customizable item, loading, and error content

### 5. Pokémon list screen
- [x] Display Pokémon names
- [x] Loaded-count header stays pinned (`stickyHeader`) while the title scrolls away
- [x] Display total number of Pokémon currently loaded
- [ ] Seamless loading while scrolling
- [ ] `@Preview` for the stateless `PokemonContent` (loading, loaded, error, end-of-list states)

### 6. Animations
- [ ] Animate newly loaded rows as they appear (fade in, as in the example video)
- [ ] Animate the loaded count when it changes (as in the example video)
- [ ] Animated loading indicator / footer (e.g. shimmer or skeleton) — required by the prompt, not clearly visible in the video
- [ ] Keep animations smooth as the list grows
- [ ] Respect reduced-motion settings

### 7. Accessibility
- [ ] TalkBack support (semantics, content descriptions)
- [ ] Announce newly loaded content / loading and error states
- [ ] Support dynamic font sizes and layouts
- [ ] Reduced motion considerations

### 8. Unit tests
- [ ] Repository tests (pagination, caching, error handling)
- [ ] ViewModel tests (state transitions, duplicate request prevention, retry)
- [ ] `InfiniteLazyColumn` load-more trigger logic

### 9. Cleanup
- [x] Remove unused imports (`colorResource`, `Log`)
- [ ] Rename `UiState` → `PokemonUiState`
- [ ] Inject repository via ViewModel constructor (needed for tests)
- [ ] Declare `lifecycle-runtime-compose` explicitly (currently transitive)
- [ ] Add test dependencies (`kotlinx-coroutines-test`)

### 10. Documentation
- [ ] Build instructions
- [ ] Architecture overview
- [ ] Key design decisions
- [ ] Tradeoffs made
- [ ] Future improvements

---

## Build Instructions

_TODO_

## Architecture Overview

_TODO_

## Key Design Decisions

- **`InfiniteLazyColumn` instead of `InfiniteScrollView`:** the prompt names the component `InfiniteScrollView`. It's named `InfiniteLazyColumn` here because it wraps a `LazyColumn` and follows Compose naming (`LazyColumn`, `LazyRow`), whereas "ScrollView" is a View-system term.

## Caching Strategy

_TODO_

## Accessibility

- **Decorative row icon:** the bolt icon on each Pokémon row uses `contentDescription = null`. It's identical on every row and carries no information — the Pokémon name is the row's meaning. With a description, TalkBack would announce it on every row ("bolt, Bulbasaur", "bolt, Ivysaur", …), adding noise without value; with `null`, TalkBack skips it and reads just the name.

## Tradeoffs

### Initial load in `init` vs. lazy `Flow.onStart`

The first page is loaded from the ViewModel's `init` block.

The alternative is to start loading lazily when the UI first collects the state: `Flow.onStart { … }` combined with `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), …)`, as in Google's Now in Android sample.

**What lazy loading would give:**
- No work until something is observing the state.
- Tests control when loading starts; constructing the ViewModel has no side effects.

**Why `init` was chosen instead:**
- In a single-screen app the ViewModel is only created because the screen is shown and immediately collected, so the two approaches start loading at effectively the same moment.
- `init` is simpler: a single exposed `StateFlow`, no restart behavior to reason about.
- With `WhileSubscribed`, the upstream restarts after the app is backgrounded for more than 5 seconds, re-running `onStart`. That requires an extra "already have data" guard to avoid reloading, which is easy to get subtly wrong.

**Cost of this choice:** loading starts as soon as the ViewModel is constructed, so unit tests must install a test Main dispatcher and inject a fake repository *before* creating the ViewModel. With more screens or ViewModels created ahead of display (e.g. pre-composed navigation destinations), the lazy approach would be worth revisiting.

## Future Improvements

- Dependency injection framework (e.g. Hilt): the app currently uses manual DI — constructor injection (`PokemonRepository(api)`, `PokemonViewModel(repository)`), a single `AppContainer` that builds Retrofit and the repository, and a `viewModelFactory` to construct the ViewModel. A framework is overkill for one screen, but would replace the container and factory as the app grows
- Replace Gson with kotlinx.serialization: Gson ignores Kotlin nullability, so a missing field can land as `null` in a non-null property and crash later; kotlinx.serialization fails at parse time instead
