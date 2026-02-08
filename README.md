This is a capstone project for a design pattern tutorial app.

## Refactoring to MVI and Jetpack Compose

This project is currently being refactored to a modern Android architecture using MVI (Model-View-Intent) and Jetpack Compose.

### Progress:

*   **Gradle Modernization:**
    *   Updated project-level `build.gradle` to use the latest plugin syntax.
    *   Updated app-level `build.gradle` with modern dependencies for Jetpack Compose, ViewModel, and Dagger.
    *   Configured `settings.gradle` to include necessary plugin repositories.
*   **MVI Architecture Setup:**
    *   Created `HomeViewModel` to manage UI state and handle user events.
    *   Defined `HomeState` and `HomeEvent` sealed classes for MVI.
*   **Dependency Injection:**
    *   Created `AppModule` to provide `Application` and `ContentResolver` dependencies.
    *   Created `AppComponent` to provide a `ViewModelFactory`.
    *   Created `ViewModelModule` and `ViewModelKey` to provide ViewModels.
    *   Updated `MyApplication` to use the new `AppComponent`.
*   **UI (Jetpack Compose):**
    *   Converted `HomeActivity` to a `ComponentActivity` and set up a basic Compose UI.
    *   Created `HomeScreen`, `NavigationDrawer`, `CategoryList`, and `PatternScreen` composables.
    *   Implemented navigation between screens using `NavHost`.
*   **Cleanup:**
    *   Removed old fragments, presenters, and other unnecessary files.
