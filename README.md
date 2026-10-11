# Android Infinite Lazy Column

A Kotlin / Jetpack Compose app that displays Pokémon from [PokéAPI](https://pokeapi.co/) using a reusable, generic `InfiniteLazyColumn` component.

## Progress

### 0. Project setup
- [x] Create Compose project (`com.haidershah.pokedex`, minSdk 24)
- [x] Initialize git and push to GitHub
- [x] Add dependencies (networking, serialization, lifecycle/ViewModel, testing)

### 1. Networking
- [x] Model the paginated API response (`count`, `next`, `previous`, `results`)
- [x] Create API service for `https://pokeapi.co/api/v2/pokemon?limit=10&offset=0`
- [x] Page size back to `limit=10` per the prompt
- [x] Load subsequent pages using the `next` URL (Retrofit `@Url`)
- [x] Handle failures gracefully (no connectivity, HTTP errors, malformed responses) — catch in ViewModel, store in UI state

### 2. Repository & caching
- [x] Repository layer between network and ViewModel
- [x] Implement caching strategy
- [x] Document what is cached, why, and tradeoffs

### 3. State management
- [x] Define UI state: items + sealed `LoadState` (`Idle`, `Loading`, `Error`, `EndReached`)
- [x] Derive loaded count from `items.size`
- [x] ViewModel exposes state and a load action that appends to the list
- [x] Prevent duplicate requests while a load is in progress (`LoadState.Loading` guard)
- [x] Handle end of list (`next == null`) — stop requesting
- [x] Initial load fills the screen (example video starts at 20 = two pages)
- [x] Retry after error

### 4. `InfiniteLazyColumn` component
- [x] Generic over item type (no Pokémon knowledge)
- [x] Triggers load-more when the user nears the end of the list (prefetch threshold, so the user never hits the end)
- [x] Loading, success, and error states
- [x] Customizable item, loading, and error content

### 5. Pokémon list screen
- [x] Display Pokémon names
- [x] Loaded-count header stays pinned (`stickyHeader`) while the title scrolls away
- [x] Display total number of Pokémon currently loaded
- [x] Seamless loading while scrolling
- [ ] `@Preview` for the stateless `PokemonContent` (loading, loaded, error, end-of-list states)

### 6. Animations
- [x] Animate newly loaded rows as they appear (fade in, as in the example video)
- [x] Animate the loaded count when it changes (as in the example video)
- [x] Animated loading indicator / footer (e.g. shimmer or skeleton) — required by the prompt, not clearly visible in the video
- [x] Keep animations smooth as the list grows
- [ ] Respect reduced-motion settings

### 7. Accessibility
- [ ] TalkBack support (semantics, content descriptions)
- [ ] Announce newly loaded content / loading and error states
- [x] Support dynamic font sizes and layouts
- [ ] Reduced motion considerations

### 8. Unit tests
- [x] Repository tests (first-page URL, next-page URL)
- [x] ViewModel tests (state transitions, duplicate request prevention, retry)
- [ ] `InfiniteLazyColumn` load-more trigger logic

### 9. Cleanup
- [x] Remove unused imports (`colorResource`, `Log`)
- [x] Remove `// todo remove` test code in `PokemonViewModel` (artificial `delay`)
- [x] Rename `UiState` → `PokemonUiState`
- [x] Inject repository via ViewModel constructor (needed for tests)
- [ ] Declare `lifecycle-runtime-compose` explicitly (currently transitive)
- [x] Add test dependencies (`kotlinx-coroutines-test`)
- [ ] Remove all remaining `todo` comments before submitting

### 10. Documentation
- [x] Build instructions
- [x] Architecture overview
- [x] Key design decisions
- [ ] Tradeoffs made
- [ ] Future improvements

---

## Build Instructions

1. Clone the repo:
   ```sh
   git clone https://github.com/haidershah/android-infinite-lazy-column.git
   ```
2. Open the project in the latest stable Android Studio and let Gradle sync.
3. Run the `app` configuration on an emulator or device (Android 7.0 / API 24 or newer). The first load needs an internet connection.

To run the unit tests:

```sh
./gradlew testDebugUnitTest
```

## Architecture Overview

The app follows the MVVM pattern: the UI shows state and sends events, the ViewModel owns the state, and the repository fetches data.

```
PokemonActivity
  └─ PokemonScreen              (Compose UI)
       └─ InfiniteLazyColumn    (generic list component)
  PokemonViewModel              (holds PokemonUiState)
  └─ PokemonRepository          (decides which URL to load)
       └─ PokemonApi            (Retrofit)
            └─ OkHttp + cache   (network and response caching)
```

- **`ui/components`:** `InfiniteLazyColumn` and `LoadState`. Nothing here knows about Pokémon.
- **`ui/pokemon`:** `PokemonScreen`, `PokemonViewModel` and `PokemonUiState`.
- **`data`:** `PokemonApi`, the response models and `PokemonRepository`.
- **`PokedexApplication`:** builds OkHttp, Retrofit and the repository once, and the ViewModel gets the repository through its constructor.

## Key Design Decisions

- **`InfiniteLazyColumn` instead of `InfiniteScrollView`:** the prompt names the component `InfiniteScrollView`. It's named `InfiniteLazyColumn` here because it wraps a `LazyColumn` and follows Compose naming (`LazyColumn`, `LazyRow`), whereas "ScrollView" is a View-system term.
- **The component is generic:** `InfiniteLazyColumn<T>` takes the items, the current `LoadState` and a `loadData` function. The screen passes in how to draw each item, the error UI and the end-of-list message, so the component has no Pokémon code or text of its own. Loading falls back to a spinner if the caller doesn't provide one.
- **Load before the user reaches the end:** the next page is requested when the user is within 3 items of the end (`loadMoreThreshold`), so scrolling doesn't stall.
- **One `LoadState` for the list:** `Idle`, `Loading`, `Error` or `EndReached`. The component uses it to decide what to show at the bottom, and the ViewModel uses it to ignore a new request while one is already loading.
- **Follow the server's `next` URL:** each response says where the next page is, so the app uses that instead of calculating offsets itself. When `next` is `null`, the list is finished.
- **Fakes instead of mocks in tests:** `PokemonApi` is a small interface, so tests use a `FakePokemonApi` that runs whatever the test gives it. No mocking library is needed.

## Caching Strategy

- **What is cached:** each page of Pokémon returned by the server, saved on the phone in the app's private cache folder (up to 10 MB).
- **How:** OkHttp's built-in response cache, set up in `PokedexApplication.kt` and passed to Retrofit. The repository and ViewModel don't know about it.
- **How long:** the server says each response stays fresh for 24 hours. Within that time, pages load from the phone with no network call, even offline. After that, the app checks with the server whether the data changed.

For why this approach was chosen over the alternatives, see [Tradeoffs](#http-response-caching-vs-in-memory-vs-persistent-storage).

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

### HTTP response caching vs. in-memory vs. persistent storage

Responses are cached at the HTTP layer with OkHttp's disk `Cache`.

**What the server sends.** Before picking an approach, I looked at PokéAPI's response headers with curl:

```sh
curl -i "https://pokeapi.co/api/v2/pokemon?offset=20&limit=10"
```

The relevant headers:

```
cache-control: public, max-age=86400
age: 57283
etag: W/"325-6RNO9puAnP/eS+oLiU6f+AiK94o"
```

| Header | Meaning |
|---|---|
| `max-age=86400` | The response is fresh for 86,400 seconds (24 hours). |
| `public` | Any cache may store it, including the app's. |
| `age` | How long, in seconds, the response has already sat in the CDN cache (Cloudflare). OkHttp subtracts it, so here the response stays fresh for about 8 more hours, not a full 24. |
| `etag` | A version tag for the data. After it expires, the app sends this tag to ask the server "has this changed?" If not, the server says so without resending the data, and the app keeps using its saved copy. |

The server already says how long the data stays fresh and how to check whether it has changed, and that shaped the decision below.

**Why response caching was chosen:**
- The data is read-only and rarely changes (Pokémon names don't change).
- PokéAPI already sends caching headers (`Cache-Control: public, max-age=86400` and an `ETag`), so the server decides how long a response is fresh.
- Pages are fetched by URL, which is exactly how an HTTP cache stores them. Little code is needed and the repository and ViewModel stay unchanged.
- It survives app restarts, since it's stored in the app's private cache directory.
- **Knowing when data has changed is handled for us.** After the saved copy expires, the app uses the `etag` to ask the server whether the data changed. If it didn't, the app keeps using its saved copy.

**Why not the alternatives:**
- **In-memory cache:** the ViewModel already holds the loaded list in memory, so a separate in-memory cache adds little, and it's lost when the process dies.
- **Persistent storage (Room):** we would have to decide ourselves when the stored data is out of date (timestamps, expiry, refetching, updating rows). Too much for read-only data; worth it for offline-first behavior, search, or editing.
- **DataStore:** built for small key-value data like settings. Every write rewrites the whole file, which is a poor fit for a list that grows with each page.

**Cost of this choice:** once the saved copy expires, the app needs a connection to check with the server, so expired data isn't shown offline. Showing it offline would take extra code. The caching also depends on the server's headers; if the server changed them, the caching would change too.

## Future Improvements

- Dependency injection framework (e.g. Hilt): the app currently uses manual DI — constructor injection (`PokemonRepository(api)`, `PokemonViewModel(repository)`), `PokedexApplication`, which builds OkHttp, Retrofit and the repository once, and a `viewModelFactory` to construct the ViewModel. A framework is overkill for one screen, but would replace this manual setup and the factory as the app grows
- Replace Gson with kotlinx.serialization: Gson ignores Kotlin nullability, so a missing field can land as `null` in a non-null property and crash later; kotlinx.serialization fails at parse time instead
- UI test for `InfiniteLazyColumn`: a Compose UI test that scrolls near the end of the list and checks that `loadData` is called. Skipped because it needs an emulator and more setup than the unit tests
- Fade in every new row as it first scrolls onto the screen: `animateItem()` only animates rows that are visible when they're added. Because the next page loads before the user reaches the end, most new rows are added below the screen and simply appear. Tracking which keys have already been shown and fading in new ones on first appearance would animate every new row
