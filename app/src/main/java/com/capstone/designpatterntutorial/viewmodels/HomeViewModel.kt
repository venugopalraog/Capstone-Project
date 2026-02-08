package com.capstone.designpatterntutorial.viewmodels

import android.app.Application
import android.content.ContentResolver
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.designpatterntutorial.database.DesignPatternContract
import com.capstone.designpatterntutorial.model.converter.MainScreenConverter
import com.capstone.designpatterntutorial.model.mainscreen.MainScreenData
import com.capstone.designpatterntutorial.model.mainscreen.Pattern
import com.capstone.designpatterntutorial.services.FavoriteDbService
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

sealed class HomeEvent {
    object LoadPatterns : HomeEvent()
    object LoadFavorites : HomeEvent()
    data class ToggleFavorite(val pattern: Pattern) : HomeEvent()
}

class HomeViewModel @Inject constructor(
    private val application: Application,
    private val contentResolver: ContentResolver
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state

    private val _favoritesState = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val favoritesState: StateFlow<FavoritesState> = _favoritesState

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
        }
    }

    private fun toggleFavorite(pattern: Pattern) {
        val intent = Intent(application, FavoriteDbService::class.java).apply {
            action = if (pattern.isFavorite) FavoriteDbService.ACTION_DELETE else FavoriteDbService.ACTION_INSERT
            putExtra(FavoriteDbService.PATTERN, pattern)
        }
        application.startService(intent)
        loadPatterns()
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
                                name = name,
                                summary = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.FavoritePatternEntry.COLUMN_DESCRIPTION)),
                                url = it.getString(it.getColumnIndexOrThrow(DesignPatternContract.FavoritePatternEntry.COLUMN_INTENT)),
                                type = "", // You might want to add a 'type' column to your database
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
}
