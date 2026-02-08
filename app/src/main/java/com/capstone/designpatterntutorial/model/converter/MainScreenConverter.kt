package com.capstone.designpatterntutorial.model.converter

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import com.capstone.designpatterntutorial.database.DesignPatternContract
import com.capstone.designpatterntutorial.model.mainscreen.Category
import com.capstone.designpatterntutorial.model.mainscreen.MainScreenData
import com.capstone.designpatterntutorial.model.mainscreen.Pattern

object MainScreenConverter {

    fun convertCategoryListEntry(cursor: Cursor?): MainScreenData? {
        if (cursor == null || cursor.count <= 0) {
            return null
        }

        val mainScreenData = MainScreenData()
        val tabList = ArrayList<Category>()

        cursor.moveToFirst()

        for (pos in 0 until cursor.count) {
            val category = Category(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DesignPatternContract.CategoryEntry.COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(DesignPatternContract.CategoryEntry.COLUMN_NAME)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(DesignPatternContract.CategoryEntry.COLUMN_DESCRIPTION)),
                patternList = ArrayList()
            )
            tabList.add(category)
            cursor.moveToNext()
        }

        mainScreenData.categoryList = tabList
        return mainScreenData
    }

    fun convertCategoryDetailsEntry(contentResolver: ContentResolver, cursor: Cursor?, patternList: ArrayList<Pattern>) {
        if (cursor == null || cursor.count <= 0) {
            return
        }

        cursor.moveToFirst()
        for (pos in 0 until cursor.count) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_NAME))
            val pattern = Pattern(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_ID)),
                categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_CATEGORY_ID)),
                name = name,
                summary = cursor.getString(cursor.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_DESCRIPTION)),
                url = cursor.getString(cursor.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_INTENT)),
                imageName = cursor.getString(cursor.getColumnIndexOrThrow(DesignPatternContract.PatternEntry.COLUMN_IMAGE_NAME)),
                isFavorite = isFavoritePattern(contentResolver, name)
            )
            patternList.add(pattern)
            cursor.moveToNext()
        }
    }

    private fun isFavoritePattern(contentResolver: ContentResolver, name: String): Boolean {
        val selection = String.format("%s=?", DesignPatternContract.FavoritePatternEntry.COLUMN_NAME)
        val selectionArgs = arrayOf(name)
        val cursor = contentResolver.query(
            DesignPatternContract.FavoritePatternEntry.CONTENT_URI,
            DesignPatternContract.FavoritePatternEntry.FAVORITE_PATTERN_COLUMNS,
            selection,
            selectionArgs,
            null
        )
        val result = cursor != null && cursor.count > 0

        cursor?.close()
        return result
    }

    fun toFavoritePatternContentValues(pattern: Pattern): ContentValues {
        val cv = ContentValues()
        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_ID, pattern.id)
        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_CATEGORY_ID, pattern.categoryId)
        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_NAME, pattern.name)
        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_DESCRIPTION, pattern.summary)
        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_INTENT, pattern.url)
        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_IMAGE_NAME, pattern.imageName)
        return cv
    }
}
