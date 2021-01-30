package com.dicoding.naufal.githubuser.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dicoding.naufal.githubuser.database.UserContract.UserColumns.Companion.COLUMN_NAME_USERID
import com.dicoding.naufal.githubuser.database.UserContract.UserColumns.Companion.TABLE_NAME
import com.dicoding.naufal.githubuser.helper.DatabaseHelper
import java.sql.SQLException

class UserHelper(context: Context) {

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: UserHelper? = null

        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: UserHelper(context)
            }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open(){
        database = dataBaseHelper.writableDatabase
    }

    fun close(){
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_NAME_USERID ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$COLUMN_NAME_USERID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$COLUMN_NAME_USERID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$COLUMN_NAME_USERID = '$id'", null)
    }
}