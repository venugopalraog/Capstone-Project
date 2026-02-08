package com.capstone.designpatterntutorial.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.capstone.designpatterntutorial.database.DesignPatternContract.FavoritePatternEntry
import com.capstone.designpatterntutorial.database.DesignPatternContract.RecentPatternEntry

class DesignPatternProvider : ContentProvider() {

    private lateinit var openHelper: DesignPatternDbHelper

    override fun onCreate(): Boolean {
        openHelper = DesignPatternDbHelper(context!!)
        // This ensures the database is created and populated before any other part of the app tries to access it.
        openHelper.writableDatabase
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor: Cursor
        when (sUriMatcher.match(uri)) {
            CATEGORY -> cursor = openHelper.readableDatabase.query(
                DesignPatternContract.CategoryEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            PATTERN -> cursor = openHelper.readableDatabase.query(
                DesignPatternContract.PatternEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            FAVORITE_PATTERN -> cursor = openHelper.readableDatabase.query(
                FavoritePatternEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            RECENT_PATTERN -> cursor = openHelper.readableDatabase.query(
                RecentPatternEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String {
        throw RuntimeException("We are not implementing getType in Design Pattern Tutorial.")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = openHelper.writableDatabase
        val match = sUriMatcher.match(uri)
        val returnUri: Uri
        when (match) {
            FAVORITE_PATTERN -> {
                val id = db.insertWithOnConflict(FavoritePatternEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
                returnUri = if (id > 0) {
                    FavoritePatternEntry.buildPatternUri(id)
                } else {
                    throw android.database.SQLException("Failed to insert row into $uri")
                }
            }
            RECENT_PATTERN -> {
                val id = db.insertWithOnConflict(RecentPatternEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
                returnUri = if (id > 0) {
                    RecentPatternEntry.buildPatternUri(id)
                } else {
                    throw android.database.SQLException("Failed to insert row into $uri")
                }
            }
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return returnUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = openHelper.writableDatabase
        val match = sUriMatcher.match(uri)
        val rowsDeleted: Int
        var sel = selection
        if (null == sel) sel = "1"
        when (match) {
            FAVORITE_PATTERN -> rowsDeleted = db.delete(FavoritePatternEntry.TABLE_NAME, sel, selectionArgs)
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
        if (rowsDeleted != 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return rowsDeleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    companion object {
        private val sUriMatcher = buildUriMatcher()
        const val CATEGORY = 100
        const val PATTERN = 101
        const val FAVORITE_PATTERN = 102
        const val RECENT_PATTERN = 103
        private fun buildUriMatcher(): UriMatcher {
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = DesignPatternContract.CONTENT_AUTHORITY
            matcher.addURI(authority, DesignPatternContract.PATH_CATEGORY, CATEGORY)
            matcher.addURI(authority, DesignPatternContract.PATH_PATTERN, PATTERN)
            matcher.addURI(authority, DesignPatternContract.PATH_FAVORITE_PATTERN, FAVORITE_PATTERN)
            matcher.addURI(authority, DesignPatternContract.PATH_RECENT_PATTERN, RECENT_PATTERN)
            return matcher
        }
    }
}
