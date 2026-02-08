# Change Log

All notable changes to this project will be documented in this file.

## [Unreleased] - 2024-05-16

### Refactoring to MVI and Jetpack Compose

This release marks a significant architectural shift from a traditional Android architecture to a modern one utilizing MVI (Model-View-Intent) with Jetpack Compose.

### Added

- **Jetpack Compose UI:**
    - `HomeActivity.kt`: Converted to a `ComponentActivity` to host Jetpack Compose UI.
    - `HomeScreen.kt`: A new composable that serves as the main screen, managing navigation and observing state from the `HomeViewModel`.
    - `CategoryList.kt`: A composable for displaying the list of design pattern categories and patterns.
    - `PatternScreen.kt`: A composable for displaying the details of a selected pattern.
    - `NavigationDrawer.kt`: A composable for the app's main navigation drawer.
    - `FavoriteList.kt`: A composable for displaying the list of favorite patterns.
- **MVI Architecture:**
    - `HomeViewModel.kt`: A new `ViewModel` that manages the UI state for the `HomeScreen` and handles user events.
    - `HomeState.kt` & `HomeEvent.kt`: Sealed classes that define the possible states and events for the MVI pattern.
    - `FavoritesState.kt`: A sealed class that defines the possible states for the favorites screen.
- **Dependency Injection (Dagger):**
    - `AppComponent.kt`: A new Dagger component for providing dependencies to the application.
    - `AppModule.kt`: A Dagger module for providing application-level dependencies like `ContentResolver`.
    - `ViewModelModule.kt`, `ViewModelFactory.kt`, `ViewModelKey.kt`: A Dagger setup for injecting ViewModels.
- **Kotlin Models:**
    - Converted `Pattern`, `Category`, `MainScreenData`, and `FavoriteScreenData` from Java classes to Kotlin data classes, and made them `Serializable`.
- **Kotlin Services & Converters:**
    - Converted `FavoriteDbService` and `MainScreenConverter` to Kotlin.

### Changed

- **Gradle Build System:**
    - Updated the project-level `build.gradle` to use the modern `plugins` block.
    - Updated the app-level `build.gradle` with dependencies for Jetpack Compose, Navigation, ViewModel, and Dagger.
    - Modernized `settings.gradle` by adding `pluginManagement` and `dependencyResolutionManagement` blocks.
- **Application Class:**
    - Converted `MyApplication.java` to `MyApplication.kt` and updated it to initialize the new Dagger `AppComponent`.
- **Navigation:**
    - Replaced Fragment-based navigation with a `NavHost` in Jetpack Compose.
    - Implemented a custom `NavType` to pass `Pattern` objects between composables.
    - URL-encoded the `Pattern` JSON to prevent crashes during navigation.
- **Favorites:**
    - Added a "favorite" icon to the `PatternScreen`.
    - Updated the `HomeViewModel` to handle adding and removing favorites.
    - Implemented the "Favorites" screen to display the list of favorite patterns.

### Removed

- **Legacy Architecture:**
    - Deleted the old fragment-based UI, including `CategoryFragment`, `FavoriteListFragment`, `PatternFragment`, and others.
    - Removed the `HomePresenter` and other presenters.
- **Legacy Dependencies:**
    - Removed the `EventBus` dependency.
    - Removed data binding from the build configuration.
- **Old Java Files:**
    - Deleted numerous old Java files that were replaced by their Kotlin counterparts or were no longer needed.

