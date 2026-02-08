package com.capstone.designpatterntutorial.services

import android.app.IntentService
import android.content.Intent
import com.capstone.designpatterntutorial.database.DesignPatternContract
import com.capstone.designpatterntutorial.model.converter.MainScreenConverter
import com.capstone.designpatterntutorial.model.mainscreen.Pattern
import com.capstone.designpatterntutorial.util.getSerializableCompat

class RecentDbService : IntentService("RecentDbService") {

    companion object {
        const val ACTION_INSERT = "com.capstone.designpatterntutorial.services.action.INSERT_RECENT"
        const val PATTERN = "PATTERN"
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_INSERT -> {
                val pattern = intent.getSerializableCompat<Pattern>(PATTERN)
                pattern?.let { insertRecentPattern(it) }
            }
        }
    }

    private fun insertRecentPattern(pattern: Pattern) {
        contentResolver.insert(
            DesignPatternContract.RecentPatternEntry.CONTENT_URI,
            MainScreenConverter.toFavoritePatternContentValues(pattern) // We can reuse this for recents
        )
    }
}
