package com.dicoding.naufal.myfavoritegithubusers.database

import android.net.Uri
import android.provider.BaseColumns

object UserContract {

    const val AUTHORITY = "com.dicoding.naufal.githubuser"
    const val SCHEME = "content"

    internal class UserColumns : BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite_user"
            const val COLUMN_NAME_USERID = "user_id"
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME_AVATAR_URL = "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}