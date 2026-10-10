# Android Infinite Scroll

A Kotlin / Jetpack Compose app that displays Pokémon from [PokéAPI](https://pokeapi.co/) using a reusable, generic `InfiniteScrollView` component.

## Progress

### 0. Project setup
- [x] Create Compose project (`com.haidershah.infinitescroll`, minSdk 24)
- [x] Initialize git and push to GitHub
- [ ] Add dependencies (networking, serialization, lifecycle/ViewModel, testing)

### 1. Networking
- [ ] Model the paginated API response (`count`, `next`, `previous`, `results`)
- [ ] Create API service for `https://pokeapi.co/api/v2/pokemon?limit=10&offset=0`
- [ ] Load subsequent pages using the `next` URL
- [ ] Handle failures gracefully (no connectivity, HTTP errors, malformed responses)

### 2. Repository & caching
- [ ] Repository layer between network and ViewModel
- [ ] Implement caching strategy
- [ ] Document what is cached, why, and tradeoffs

### 3. State management
- [ ] Define UI state (items, load state, total loaded count)
- [ ] ViewModel exposes state and a "load more" action
- [ ] Prevent duplicate requests while a load is in progress
- [ ] Handle end of list (`next == null`)
- [ ] Retry after error

### 4. `InfiniteScrollView` component
- [ ] Generic over item type (no Pokémon knowledge)
- [ ] Triggers load-more when the user nears the end of the list
- [ ] Loading, success, and error states
- [ ] Customizable item, loading, and error content

### 5. Pokémon list screen
- [ ] Display Pokémon names
- [ ] Display total number of Pokémon currently loaded
- [ ] Seamless loading while scrolling
- [ ] `@Preview` for the stateless `PokemonContent` (loading, loaded, error, end-of-list states)

### 6. Animations
- [ ] Animate newly loaded rows as they appear
- [ ] Animated loading indicator / footer (e.g. shimmer or skeleton)
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
- [ ] `InfiniteScrollView` load-more trigger logic

### 9. Documentation
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

_TODO_

## Caching Strategy

_TODO_

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

- Dependency injection framework (e.g. Hilt)
