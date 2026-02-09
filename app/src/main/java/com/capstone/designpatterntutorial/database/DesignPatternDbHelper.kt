package com.capstone.designpatterntutorial.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.capstone.designpatterntutorial.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class DesignPatternDbHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "design_pattern.db"
        private const val DATABASE_VERSION = 3 // Incremented version to force upgrade
    }

    override fun onCreate(db: SQLiteDatabase) {
        try {
            val inputStream = context.resources.openRawResource(R.raw.design_pattern)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val sql = reader.readText()
            val statements = sql.split(";")

            db.beginTransaction()
            try {
                for (statement in statements) {
                    if (statement.trim().isNotEmpty()) {
                        db.execSQL(statement)
                    }
                }
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // To keep things simple for this project, we'll just drop and recreate the table
        // if the database version changes.
        db.execSQL("DROP TABLE IF EXISTS ${DesignPatternContract.CategoryEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DesignPatternContract.PatternEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DesignPatternContract.FavoritePatternEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DesignPatternContract.RecentPatternEntry.TABLE_NAME}")
        onCreate(db)
    }
}
