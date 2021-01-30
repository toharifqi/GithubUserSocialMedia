package com.dicoding.naufal.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel  (
    var username: String? = null,
    var userId: String? = null,
    var avatar: String? = null
): Parcelable