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
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        try {
            val inputStream = context.resources.openRawResource(R.raw.design_pattern)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val statements = reader.readText().split(";\n".toRegex()).toTypedArray()
            for (statement in statements) {
                if (statement.trim().isNotEmpty()) {
                    db.execSQL(statement)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Not needed for this project
    }
}
