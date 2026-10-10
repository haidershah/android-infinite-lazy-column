# Android Infinite Scroll

A Kotlin / Jetpack Compose app that displays Pokémon from [PokéAPI](https://pokeapi.co/) using a reusable, generic `InfiniteScrollView` component.

## Progress

### 0. Project setup
- [x] Create Compose project (`com.haidershah.infinitescroll`, minSdk 24)
- [x] Initialize git and push to GitHub
- [ ] Add dependencies (networking, serialization, lifecycle/ViewModel, testing)
- [ ] Decide on dependency injection approach (Hilt vs. manual)

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

_TODO_

## Future Improvements

_TODO_
