package com.capstone.designpatterntutorial.viewmodels

import android.app.Application
import android.content.ContentResolver
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.designpatterntutorial.BuildConfig
import com.capstone.designpatterntutorial.database.DesignPatternContract
import com.capstone.designpatterntutorial.model.converter.MainScreenConverter
import com.capstone.designpatterntutorial.model.mainscreen.MainScreenData
import com.capstone.designpatterntutorial.model.mainscreen.Pattern
import com.capstone.designpatterntutorial.services.FavoriteDbService
import com.capstone.designpatterntutorial.services.RecentDbService
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val data: MainScreenData) : HomeState()
    data class Error(val message: String) : HomeState()
}

sealed class FavoritesState {
    object Loading : FavoritesState()
    data class Success(val patterns: List<Pattern>) : FavoritesState()
    data class Error(val message: String) : FavoritesState()
}

sealed class RecentsState {
    object Loading : RecentsState()
    data class Success(val patterns: List<Pattern>) : RecentsState()
    data class Error(val message: String) : RecentsState()
}

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    data class Success(val patterns: List<Pattern>) : SearchState()
    data class Error(val message: String) : SearchState()
}

enum class SearchMode {
    DATABASE,
    AI
}

sealed class HomeEvent {
    object LoadPatterns : HomeEvent()
    object LoadFavorites : HomeEvent()
    object LoadRecents : HomeEvent()
    data class ToggleFavorite(val pattern: Pattern) : HomeEvent()
    data class AddRecent(val pattern: Pattern) : HomeEvent()
    data class Search(val query: String) : HomeEvent()
    data class SetSearchMode(val mode: SearchMode) : HomeEvent()
}

class HomeViewModel @Inject constructor(
    private val application: Application,
    private val contentResolver: ContentResolver
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state

    private val _favoritesState = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val favoritesState: StateFlow<FavoritesState> = _favoritesState

    private val _recentsState = MutableStateFlow<RecentsState>(RecentsState.Loading)
    val recentsState: StateFlow<RecentsState> = _recentsState

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState

    private val _searchMode = MutableStateFlow(SearchMode.DATABASE)
    val searchMode: StateFlow<SearchMode> = _searchMode

    private val generativeModel: GenerativeModel by lazy {
        try {
            GenerativeModel(
                modelName = "gemini-1.5-pro-latest",
                apiKey = BuildConfig.GEMINI_API_KEY
            )
        } catch (e: Exception) {
            // Handle model initialization failure
            throw IllegalStateException("Failed to initialize GenerativeModel", e)
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadPatterns -> {
                loadPatterns()
            }
            is HomeEvent.ToggleFavorite -> {
                toggleFavorite(event.pattern)
            }
            is HomeEvent.LoadFavorites -> {
                loadFavorites()
            }
            is HomeEvent.AddRecent -> {
                addRecent(event.pattern)
            }
            is HomeEvent.LoadRecents -> {
                loadRecents()
            }
            is HomeEvent.Search -> {
                search(event.query)
            }
            is HomeEvent.SetSearchMode -> {
                _searchMode.value = event.mode
            }
        }
    }

    private fun toggleFavorite(patternToToggle: Pattern) {
        val isCurrentlyFavorite = patternToToggle.isFavorite
        val action = if (isCurrentlyFavorite) FavoriteDbService.ACTION_DELETE else FavoriteDbService.ACTION_INSERT

        // Optimistically update the main patterns list state
        val currentState = _state.value
        if (currentState is HomeState.Success) {
            val updatedCategories = currentState.data.categoryList.map { category ->
                val updatedPatterns = category.patternList.map { pattern ->
                    if (pattern.id == patternToToggle.id) {
                        pattern.copy(isFavorite = !isCurrentlyFavorite)
                    } else {
                        pattern
                    }
                }
                category.copy(patternList = ArrayList(updatedPatterns))
            }
            _state.value = HomeState.Success(currentState.data.copy(categoryList = ArrayList(updatedCategories)))
        }

        // Optimistically update the favorites list state
        val currentFavoritesState = _favoritesState.value
        if (currentFavoritesState is FavoritesState.Success) {
            if (isCurrentlyFavorite) {
                _favoritesState.value = FavoritesState.Success(currentFavoritesState.patterns.filterNot { it.id == patternToToggle.id })
            } else {
                _favoritesState.value = FavoritesState.Success(currentFavoritesState.patterns + patternToToggle.copy(isFavorite = true))
            }
        }

        // Dispatch the database operation to the background service
        val intent = Intent(application, FavoriteDbService::class.java).apply {
            this.action = action
            putExtra(FavoriteDbService.PATTERN, patternToToggle)
        }
        application.startService(intent)
    }
    
    private fun addRecent(pattern: Pattern) {
        val intent = Intent(application, RecentDbService::class.java).apply {
            action = RecentDbService.ACTION_INSERT
            putExtra(RecentDbService.PATTERN, pattern)
        }
        application.startService(intent)
    }

    private fun search(query: String) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            try {
                val patterns = when (_searchMode.value) {
                    SearchMode.DATABASE -> searchDatabase(query)
                    SearchMode.AI -> searchWithAi(query)
                }
                _searchState.value = SearchState.Success(patterns)
            } catch (e: Exception) {
                _searchState.value = SearchState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun searchDatabase(query: String): List<Pattern> {
        val selection = "${DesignPatternContract.PatternEntry.COLUMN_NAME} LIKE ?"
        val selectionArgs = arrayOf("%$query%")
        val cursor = contentResolver.query(
            DesignPatternContract.PatternEntry.CONTENT_URI,
            null,
            selection,
            selectionArgs,
            null
        )
        val patterns = mutableListOf<Pattern>()
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_NAME))
                patterns.add(
                    Pattern(
                        id = it.getInt(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_ID)),
                        categoryId = it.getInt(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_CATEGORY_ID)),
                        name = name,
                        summary = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_DESCRIPTION)),
                        url = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_INTENT)),
                        imageName = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_IMAGE_NAME)),
                        isFavorite = false // We can improve this later
                    )
                )
            }
        }
        return patterns
    }

    private suspend fun searchWithAi(query: String): List<Pattern> {
        val allPatterns = getAllPatternsFromDatabase() // You need to implement this function
        val prompt = "Given the following list of design patterns, which ones are most relevant to the query \"$query\"? List the most relevant pattern names, separated by commas.\n\n" +
                allPatterns.joinToString("\n") { "- ${it.name}: ${it.summary}" }

        val response = generativeModel.generateContent(prompt)
        val patternNames = response.text?.split(",")?.map { it.trim() } ?: emptyList()

        return allPatterns.filter { it.name in patternNames }
    }

    private fun getAllPatternsFromDatabase(): List<Pattern> {
        val cursor = contentResolver.query(
            DesignPatternContract.PatternEntry.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val patterns = mutableListOf<Pattern>()
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_NAME))
                patterns.add(
                    Pattern(
                        id = it.getInt(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_ID)),
                        categoryId = it.getInt(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_CATEGORY_ID)),
                        name = name,
                        summary = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_DESCRIPTION)),
                        url = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_INTENT)),
                        imageName = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_IMAGE_NAME)),
                        isFavorite = false // We can improve this later
                    )
                )
            }
        }
        return patterns
    }

    private fun loadPatterns() {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            try {
                val categoryListCursor = contentResolver.query(
                    DesignPatternContract.CategoryEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )

                val mainScreenData = MainScreenConverter.convertCategoryListEntry(categoryListCursor)

                if (mainScreenData != null) {
                    val categoryList = mainScreenData.categoryList
                    for (category in categoryList) {
                        val selection = String.format("%s=?", DesignPatternContract.PatternEntry.COLUMN_CATEGORY_ID)
                        val selectionArgs = arrayOf(category.id.toString())
                        val patternCursor = contentResolver.query(
                            DesignPatternContract.PatternEntry.CONTENT_URI,
                            DesignPatternContract.PatternEntry.PATTERN_COLUMNS,
                            selection,
                            selectionArgs,
                            null
                        )
                        MainScreenConverter.convertCategoryDetailsEntry(contentResolver, patternCursor, category.patternList)
                    }
                }

                if (mainScreenData != null) {
                    _state.value = HomeState.Success(mainScreenData)
                } else {
                    _state.value = HomeState.Error("No data found")
                }
            } catch (e: Exception) {
                _state.value = HomeState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _favoritesState.value = FavoritesState.Loading
            try {
                val favoriteCursor = contentResolver.query(
                    DesignPatternContract.FavoritePatternEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
                val favorites = mutableListOf<Pattern>()
                favoriteCursor?.use {
                    while (it.moveToNext()) {
                        val name = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.FavoritePatternEntry.COLUMN_NAME))
                        favorites.add(
                            Pattern(
                                id = it.getInt(it.getColumnIndexOrThrow(DesignPatternContract.FavoritePatternEntry.COLUMN_ID)),
                                categoryId = it.getInt(it.getColumnIndexOrThrow(DesignPatternContract.FavoritePatternEntry.COLUMN_CATEGORY_ID)),
                                name = name,
                                summary = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.FavoritePatternEntry.COLUMN_DESCRIPTION)),
                                url = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.FavoritePatternEntry.COLUMN_INTENT)),
                                imageName = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.FavoritePatternEntry.COLUMN_IMAGE_NAME)),
                                isFavorite = true
                            )
                        )
                    }
                }
                _favoritesState.value = FavoritesState.Success(favorites)
            } catch (e: Exception) {
                _favoritesState.value = FavoritesState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun loadRecents() {
        viewModelScope.launch {
            _recentsState.value = RecentsState.Loading
            try {
                val recentCursor = contentResolver.query(
                    DesignPatternContract.RecentPatternEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
                val recents = mutableListOf<Pattern>()
                recentCursor?.use {
                    while (it.moveToNext()) {
                        val name = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.RecentPatternEntry.COLUMN_NAME))
                        recents.add(
                            Pattern(
                                id = it.getInt(it.getColumnIndexOrThrow(DesignPatternContract.RecentPatternEntry.COLUMN_ID)),
                                categoryId = it.getInt(it.getColumnIndexOrThrow(DesignPatternContract.RecentPatternEntry.COLUMN_CATEGORY_ID)),
                                name = name,
                                summary = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.RecentPatternEntry.COLUMN_DESCRIPTION)),
                                url = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.RecentPatternEntry.COLUMN_INTENT)),
                                imageName = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.RecentPatternEntry.COLUMN_IMAGE_NAME)),
                                isFavorite = false // We don't know if it's a favorite from this table
                            )
                        )
                    }
                }
                _recentsState.value = RecentsState.Success(recents)
            } catch (e: Exception) {
                _recentsState.value = RecentsState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
