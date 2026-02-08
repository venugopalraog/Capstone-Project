package com.capstone.designpatterntutorial.services

import android.app.IntentService
import android.content.Intent
import com.capstone.designpatterntutorial.database.DesignPatternContract
import com.capstone.designpatterntutorial.model.converter.MainScreenConverter
import com.capstone.designpatterntutorial.model.mainscreen.Pattern

class FavoriteDbService : IntentService("FavoriteDbService") {

    companion object {
        const val ACTION_INSERT = "com.capstone.designpatterntutorial.services.action.INSERT"
        const val ACTION_DELETE = "com.capstone.designpatterntutorial.services.action.DELETE"
        const val PATTERN = "PATTERN"
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_INSERT -> {
                val pattern = intent.getSerializableExtra(PATTERN) as Pattern
                insertFavoritePattern(pattern)
            }
            ACTION_DELETE -> {
                val pattern = intent.getSerializableExtra(PATTERN) as Pattern
                deleteFavoritePattern(pattern)
            }
        }
    }

    private fun insertFavoritePattern(pattern: Pattern) {
        contentResolver.insert(
            DesignPatternContract.FavoritePatternEntry.CONTENT_URI,
            MainScreenConverter.toFavoritePatternContentValues(pattern)
        )
    }

    private fun deleteFavoritePattern(pattern: Pattern) {
        val selection = String.format("%s=?", DesignPatternContract.FavoritePatternEntry.COLUMN_NAME)
        val selectionArgs = arrayOf(pattern.name)
        contentResolver.delete(
            DesignPatternContract.FavoritePatternEntry.CONTENT_URI,
            selection,
            selectionArgs
        )
    }
}
