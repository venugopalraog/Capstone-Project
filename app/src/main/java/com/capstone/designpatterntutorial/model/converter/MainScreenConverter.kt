package com.capstone.designpatterntutorial.model.converter;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.capstone.designpatterntutorial.database.DesignPatternContract;
import com.capstone.designpatterntutorial.database.DesignPatternContract.CategoryEntry;
import com.capstone.designpatterntutorial.database.DesignPatternContract.PatternEntry;
import com.capstone.designpatterntutorial.model.favorite.FavoriteScreenData;
import com.capstone.designpatterntutorial.model.mainscreen.Category;
import com.capstone.designpatterntutorial.model.mainscreen.MainScreenData;
import com.capstone.designpatterntutorial.model.mainscreen.Pattern;

import java.util.ArrayList;


/**
 * Created by gubbave on 4/27/2017.
 */

public class MainScreenConverter {

    //Convert data from CategoryList Table to model
    public static MainScreenData convertCategoryListEntry(Cursor cursor) {

        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }

        MainScreenData mainScreenData = new MainScreenData();
        ArrayList<Category> tabList = new ArrayList<>();

        cursor.moveToFirst();

        for (int pos = 0; pos < cursor.getCount(); pos++) {
            Category category = new Category();
            ArrayList<Pattern> patternList = new ArrayList<>();

            category.setId(cursor.getInt(cursor.getColumnIndex(CategoryEntry.COLUMN_ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_NAME)));
            category.setDescription(cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_DESCRIPTION)));
            category.setPatternList(patternList);

            tabList.add(category);
            cursor.moveToNext();
        }

        mainScreenData.setCategoryList(tabList);
        return mainScreenData;
    }

    //Convert data from CategoryDetails Table to model
    public static void convertCategoryDetailsEntry(ContentResolver contentResolver, Cursor cursor, ArrayList<Pattern> patternList) {

        if (cursor == null || cursor.getCount() <= 0) {
            return;
        }

        cursor.moveToFirst();
        for (int pos = 0; pos < cursor.getCount(); pos++) {
            Pattern pattern = new Pattern();

            String name = cursor.getString(cursor.getColumnIndex(PatternEntry.COLUMN_NAME));

            pattern.setId(cursor.getInt(cursor.getColumnIndex(PatternEntry.COLUMN_ID)));
            pattern.setCategoryId(cursor.getInt(cursor.getColumnIndex(PatternEntry.COLUMN_CATEGORY_ID)));
            pattern.setName(name);
            pattern.setDescription(cursor.getString(cursor.getColumnIndex(PatternEntry.COLUMN_DESCRIPTION)));
            pattern.setIntent(cursor.getString(cursor.getColumnIndex(PatternEntry.COLUMN_INTENT)));
            pattern.setImageName(cursor.getString(cursor.getColumnIndex(PatternEntry.COLUMN_IMAGE_NAME)));
            pattern.setFavorite(isFavoritePattern(contentResolver, name));

            patternList.add(pattern);
            cursor.moveToNext();
        }
    }

    public static boolean isFavoritePattern(ContentResolver contentResolver, String name) {
        String selection = String.format("%s=?", DesignPatternContract.FavoritePatternEntry.COLUMN_NAME);
        String[] selectionArgs = {name};
        Cursor cursor = contentResolver.query(DesignPatternContract.FavoritePatternEntry.CONTENT_URI,
                DesignPatternContract.FavoritePatternEntry.FAVORITE_PATTERN_COLUMNS,
                selection,
                selectionArgs,
                null);
        boolean result = ((cursor != null) && (cursor.getCount() > 0));

        cursor.close();
        return result;
    }

    public static ContentValues toFavoritePatternContentValues(Pattern pattern) {
        ContentValues cv = new ContentValues();

        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_ID, pattern.getId());
        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_CATEGORY_ID, pattern.getCategoryId());
        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_NAME, pattern.getName());
        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_DESCRIPTION, pattern.getDescription());
        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_INTENT, pattern.getIntent());
        cv.put(DesignPatternContract.FavoritePatternEntry.COLUMN_IMAGE_NAME, pattern.getImageName());

        return cv;
    }

    //Convert data from CategoryDetails Table to model
    public static FavoriteScreenData convertFavoriteScreenData(Cursor cursor) {
        FavoriteScreenData favoriteScreenData = new FavoriteScreenData();
        favoriteScreenData.setPatternList(new ArrayList<Pattern>());

        if (cursor == null || cursor.getCount() <= 0) {
            return favoriteScreenData;
        }

        ArrayList<Pattern> patternList = favoriteScreenData.getPatternList();

        cursor.moveToFirst();
        for (int pos = 0; pos < cursor.getCount(); pos++) {
            Pattern pattern = new Pattern();

            pattern.setId(cursor.getInt(cursor.getColumnIndex(DesignPatternContract.FavoritePatternEntry.COLUMN_ID)));
            pattern.setCategoryId(cursor.getInt(cursor.getColumnIndex(DesignPatternContract.FavoritePatternEntry.COLUMN_CATEGORY_ID)));
            pattern.setName(cursor.getString(cursor.getColumnIndex(DesignPatternContract.FavoritePatternEntry.COLUMN_NAME)));
            pattern.setDescription(cursor.getString(cursor.getColumnIndex(DesignPatternContract.FavoritePatternEntry.COLUMN_DESCRIPTION)));
            pattern.setIntent(cursor.getString(cursor.getColumnIndex(DesignPatternContract.FavoritePatternEntry.COLUMN_INTENT)));
            pattern.setImageName(cursor.getString(cursor.getColumnIndex(DesignPatternContract.FavoritePatternEntry.COLUMN_IMAGE_NAME)));

            patternList.add(pattern);
            cursor.moveToNext();
        }

        return favoriteScreenData;
    }
}
