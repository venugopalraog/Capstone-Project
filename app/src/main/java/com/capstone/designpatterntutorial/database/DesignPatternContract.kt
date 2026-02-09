package com.capstone.designpatterntutorial.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gubbave on 4/17/2017.
 */

public class DesignPatternContract {

    public static final String CONTENT_AUTHORITY = "com.capstone.designpatterntutorial";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CATEGORY = "category";
    public static final String PATH_PATTERN = "pattern";
    public static final String PATH_FAVORITE_PATTERN = "favorite_pattern";
    public static final String PATH_RECENT_PATTERN = "recent_pattern";


    /* Inner class that defines the table contents of the category table */
    public static final class CategoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CATEGORY)
                .build();

        public static final String TABLE_NAME = "category";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";

        public static final String[] CATEGORY_COLUMNS = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_DESCRIPTION
        };

        public static Uri buildCategoryUri(long date) {
            return CONTENT_URI;
        }
    }

    /* Inner class that defines the table contents of the pattern table */
    public static final class PatternEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PATTERN)
                .build();

        public static final String TABLE_NAME = "pattern";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CATEGORY_ID = "categoryId";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_INTENT = "intent";
        public static final String COLUMN_IMAGE_NAME = "imageName";

        public static final String[] PATTERN_COLUMNS = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_INTENT,
                COLUMN_DESCRIPTION,
                COLUMN_IMAGE_NAME,
                COLUMN_CATEGORY_ID,
        };

        public static Uri buildPatternUri(long date) {
            return CONTENT_URI;
        }
    }

    /* Inner class that defines the table contents of the favorite_pattern table */
    public static final class FavoritePatternEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_PATTERN)
                .build();

        public static final String TABLE_NAME = "favorite_pattern";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CATEGORY_ID = "categoryId";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_INTENT = "intent";
        public static final String COLUMN_IMAGE_NAME = "imageName";

        public static final String[] FAVORITE_PATTERN_COLUMNS = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_INTENT,
                COLUMN_DESCRIPTION,
                COLUMN_IMAGE_NAME,
                COLUMN_CATEGORY_ID
        };

        public static Uri buildPatternUri(long date) {
            return CONTENT_URI;
        }
    }

    /* Inner class that defines the table contents of the recent_pattern table */
    public static final class RecentPatternEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECENT_PATTERN)
                .build();

        public static final String TABLE_NAME = "recent_pattern";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CATEGORY_ID = "categoryId";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_INTENT = "intent";
        public static final String COLUMN_IMAGE_NAME = "imageName";

        public static final String[] RECENT_PATTERN_COLUMNS = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_INTENT,
                COLUMN_DESCRIPTION,
                COLUMN_IMAGE_NAME,
                COLUMN_CATEGORY_ID
        };

        public static Uri buildPatternUri(long date) {
            return CONTENT_URI;
        }
    }

}