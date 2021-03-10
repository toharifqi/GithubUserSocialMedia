package com.dicoding.naufal.githubuser.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.naufal.githubuser.database.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import com.dicoding.naufal.githubuser.database.UserContract.UserColumns.Companion.COLUMN_NAME_USERID
import com.dicoding.naufal.githubuser.database.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import com.dicoding.naufal.githubuser.database.UserContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
){
    companion object{
        private const val DATABASE_NAME = "dbfavoriteuser"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " ($COLUMN_NAME_USERID TEXT PRIMARY KEY," +
                " $COLUMN_NAME_USERNAME TEXT NOT NULL," +
                " $COLUMN_NAME_AVATAR_URL TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


}