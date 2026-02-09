package com.capstone.designpatterntutorial.database

import android.net.Uri
import android.provider.BaseColumns

object DesignPatternContract {
    const val CONTENT_AUTHORITY = "com.capstone.designpatterntutorial"
    @JvmField
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

    const val PATH_CATEGORY = "category"
    const val PATH_PATTERN = "pattern"
    const val PATH_FAVORITE_PATTERN = "favorite_pattern"
    const val PATH_RECENT_PATTERN = "recent_pattern"

    object CategoryEntry : BaseColumns {
        @JvmStatic
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build()
        const val TABLE_NAME = "category"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        @JvmStatic
        val CATEGORY_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_DESCRIPTION
        )
    }

    object PatternEntry : BaseColumns {
        @JvmStatic
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PATTERN).build()
        const val TABLE_NAME = "pattern"
        const val COLUMN_ID = "id"
        const val COLUMN_CATEGORY_ID = "categoryId"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_INTENT = "intent"
        const val COLUMN_IMAGE_NAME = "imageName"
        @JvmStatic
        val PATTERN_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_INTENT,
            COLUMN_DESCRIPTION,
            COLUMN_IMAGE_NAME,
            COLUMN_CATEGORY_ID
        )
    }

    object FavoritePatternEntry : BaseColumns {
        @JvmStatic
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_PATTERN).build()
        const val TABLE_NAME = "favorite_pattern"
        const val COLUMN_ID = "id"
        const val COLUMN_CATEGORY_ID = "categoryId"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_INTENT = "intent"
        const val COLUMN_IMAGE_NAME = "imageName"
        @JvmStatic
        val FAVORITE_PATTERN_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_INTENT,
            COLUMN_DESCRIPTION,
            COLUMN_IMAGE_NAME,
            COLUMN_CATEGORY_ID
        )

        @JvmStatic
        fun buildPatternUri(id: Long): Uri {
            return CONTENT_URI.buildUpon().appendPath(id.toString()).build()
        }
    }

    object RecentPatternEntry : BaseColumns {
        @JvmStatic
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECENT_PATTERN).build()
        const val TABLE_NAME = "recent_pattern"
        const val COLUMN_ID = "id"
        const val COLUMN_CATEGORY_ID = "categoryId"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_INTENT = "intent"
        const val COLUMN_IMAGE_NAME = "imageName"
        @JvmStatic
        val RECENT_PATTERN_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_INTENT,
            COLUMN_DESCRIPTION,
            COLUMN_IMAGE_NAME,
            COLUMN_CATEGORY_ID
        )

        @JvmStatic
        fun buildPatternUri(id: Long): Uri {
            return CONTENT_URI.buildUpon().appendPath(id.toString()).build()
        }
    }
}