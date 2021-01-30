package com.dicoding.naufal.githubuser.helper

import android.database.Cursor
import com.dicoding.naufal.githubuser.database.UserContract
import com.dicoding.naufal.githubuser.model.UserModel

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<UserModel>{
        val notesList = ArrayList<UserModel>()

        notesCursor?.apply {
            while (moveToNext()){
                val userId = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_USERID))
                val userName = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_USERNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL))
                notesList.add(UserModel(userId = userId, username = userName, avatar = avatarUrl))
            }
        }
        return notesList
    }
}