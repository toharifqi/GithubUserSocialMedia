package com.dicoding.naufal.githubuser.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.dicoding.naufal.githubuser.database.UserContract
import com.dicoding.naufal.githubuser.helper.MappingHelper
import com.dicoding.naufal.githubuser.model.UserModel


class StackWidgetService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext)
    }
}