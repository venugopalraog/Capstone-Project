package com.capstone.designpatterntutorial.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by gubbave on 4/17/2017.
 */

public class DesignPatternDbHelper extends SQLiteOpenHelper {

    private String TAG = DesignPatternDbHelper.class.getSimpleName();

    /*
    * This is the name of our database. Database names should be descriptive and end with the
    * .db extension.
    */
    public static final String DATABASE_NAME = "design_pattern.db";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;


    public DesignPatternDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Create SQLiteDatabase ");
        fillData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading sample.com.sqlitesample.database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DesignPatternContract.CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DesignPatternContract.PatternEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DesignPatternContract.FavoritePatternEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DesignPatternContract.RecentPatternEntry.TABLE_NAME);

        onCreate(db);
    }

    /* Create CategoryList, CategoryDetails table and update the data from design_pattern.sql file */
    private void fillData(SQLiteDatabase d) {
        try {
            int resourceId = mContext.getResources().getIdentifier("design_pattern", "raw", mContext.getPackageName());
            InputStream stream = mContext.getResources().openRawResource(resourceId);
            InputStreamReader streamReader = new InputStreamReader(stream, Charsets.UTF_8);
            String sqliteString = CharStreams.toString(streamReader);
            String[] sqlQueryList = sqliteString.split(";");

            for (String sqlQuery : sqlQueryList) {
                if (StringUtils.isNotEmpty(sqlQuery)) {
                    Log.d(TAG, "Sql Query :: " + sqlQuery);
                    d.execSQL(sqlQuery + ";");
                }
            }
            stream.close();
        } catch (Exception e) {
            Log.d(TAG, "Exception :: " + e.getMessage());
        }
    }
}