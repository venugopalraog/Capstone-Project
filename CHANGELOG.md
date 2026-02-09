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
    - `HtmlText.kt`: A composable for rendering HTML text.
    - `CategoryScreen.kt`: A composable for displaying the patterns for a single category.
    - `PatternListItem.kt`: A composable for displaying a single pattern in a list.
    - `SearchScreen.kt`: A composable for displaying search results.
    - `AboutScreen.kt`: A composable for displaying information about the app.
- **MVI Architecture:**
    - `HomeViewModel.kt`: A new `ViewModel` that manages the UI state for the `HomeScreen` and handles user events.
    - `HomeState.kt` & `HomeEvent.kt`: Sealed classes that define the possible states and events for the MVI pattern.
    - `FavoritesState.kt`: A sealed class that defines the possible states for the favorites screen.
    - `RecentsState.kt`: A sealed class that defines the possible states for the recents screen.
    - `SearchState.kt`: A sealed class that defines the possible states for the search screen.
- **Dependency Injection (Dagger):**
    - `AppComponent.kt`: A new Dagger component for providing dependencies to the application.
    - `AppModule.kt`: A Dagger module for providing application-level dependencies like `ContentResolver`.
    - `ViewModelModule.kt`, `ViewModelFactory.kt`, `ViewModelKey.kt`: A Dagger setup for injecting ViewModels.
- **Kotlin Models:**
    - Converted `Pattern`, `Category`, `MainScreenData`, and `FavoriteScreenData` from Java classes to Kotlin data classes, and made them `Serializable`.
- **Kotlin Services & Converters:**
    - Converted `FavoriteDbService`, `RecentDbService` and `MainScreenConverter` to Kotlin.
- **Compatibility:**
    - `IntentCompat.kt`: Added a compatibility function to safely retrieve `Serializable` objects from `Intents` on all Android versions.
- **Database:**
    - Converted `DesignPatternContract.java` to `DesignPatternContract.kt`
- **Widgets:**
    - Converted `PatternWidgetViewsFactory.java` to `PatternWidgetViewsFactory.kt`
- **Resources:**
    - Added `font_certs.xml` to provide the necessary certificates for Google Fonts.

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
    - Updated navigation to pass only the pattern ID, which is a more robust solution.
- **Favorites:**
    - Added a "favorite" icon to the `PatternScreen`.
    - Updated the `HomeViewModel` to handle adding and removing favorites.
    - Implemented the "Favorites" screen to display the list of favorite patterns.
    - Added a message to the favorites screen when no favorites have been added.
- **Recents:**
    - Added a "Recents" item to the navigation drawer.
    - Updated the `HomeViewModel` to handle adding and loading recent patterns.
    - Implemented the "Recents" screen to display the list of recently viewed patterns.
- **Search:**
    - Added a search icon to the top app bar.
    - Implemented a search bar that appears when the search icon is clicked.
    - Updated the `HomeViewModel` to handle searching for patterns.
    - Implemented a `SearchScreen` to display search results.
    - Refined the search to highlight the search query in the results.
- **Pattern Screen:**
    - The `PatternScreen` now parses and displays HTML content from the pattern summary.
- **Database:**
    - Converted `DesignPatternProvider.java` to Kotlin.
- **UI:**
    - Replaced the single list of categories with a bottom navigation bar.
    - Added icons to the bottom navigation bar.
    - Wrapped pattern list items in `Card`s for a more defined look.
    - Added a fade-in animation when switching categories.
    - Updated the favorites screen to use `Card`s for a consistent look.
    - Refactored the `HomeScreen` to use a single, persistent bottom navigation bar for top-level navigation.
- **Theme:**
    - Added a custom theme with the Montserrat font.

### Fixed

- **Fixed crash on favorite:** Resolved a crash that occurred when marking a pattern as a favorite by adding missing fields (`categoryId` and `imageName`) to the `Pattern` data class and ensuring they are correctly read from and written to the database.
- **Fixed race condition on favorite:** Resolved a race condition that occurred when quickly tapping the favorite icon by implementing an optimistic update strategy in the `HomeViewModel`.
- **Fixed database conflict on favorite:** Resolved a database conflict that occurred when inserting a favorite that already existed by using `insertWithOnConflict` in the `DesignPatternProvider`.
- **Fixed Unresolved reference for icons:** Corrected the icon references in `HomeScreen.kt` by adding the `androidx.compose.material:material-icons-extended` dependency.
- **Fixed "No such table" error:** Implemented a robust, transaction-based database creation process in `DesignPatternDbHelper` to ensure the database is always created correctly.
- **Fixed search highlight crash:** Passed an empty query to `PatternListItem` from `CategoryScreen` to prevent a crash when not in search mode.
- **Fixed empty recents screen:** Created a dedicated `toRecentPatternContentValues` function to correctly save recent patterns to the database. Incremented `DATABASE_VERSION` to force an upgrade and ensure the `recents` table is properly created.
- **Fixed private access error in widget:** Added `@JvmStatic` annotations to the properties in `DesignPatternContract.kt` to expose them to Java code.
- **Fixed Google Fonts certificate error:** Added the necessary `font_certs.xml` resource file and corrected the `GoogleFont.Provider` constructor in `Type.kt`.
- **Fixed double bottom navigation bar:** Removed the nested `Scaffold` in `HomeScreen.kt` to prevent two bottom navigation bars from being displayed.
- **Fixed inconsistent title bar:** Refactored `HomeScreen` to only show the main `TopAppBar` on top-level screens.
- **Fixed broken search and empty screens:** Corrected the UI state logic to ensure the search screen appears when active and that data for Favorites and Recents is loaded correctly.

### Removed

- **Legacy Architecture:**
    - Deleted the old fragment-based UI, including `CategoryFragment`, `FavoriteListFragment`, `PatternFragment`, and others.
    - Removed the `HomePresenter` and other presenters.
- **Legacy UI Resources:**
    - Removed obsolete XML layouts, menus, animations, and widget-related resources to complete the migration to Jetpack Compose.
- **App Widget:**
    - Removed the legacy home screen widget and its related Java code for a more focused, modern codebase.
- **Legacy Dependencies:**
    - Removed the `EventBus` dependency.
    - Removed data binding from the build configuration.
- **Old Java Files:**
    - Deleted numerous old Java files that were replaced by their Kotlin counterparts or were no longer needed.

