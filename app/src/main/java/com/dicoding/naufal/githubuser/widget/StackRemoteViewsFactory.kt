package com.dicoding.naufal.githubuser.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.dicoding.naufal.githubuser.ImageUserWidget
import com.dicoding.naufal.githubuser.R
import com.dicoding.naufal.githubuser.database.UserContract
import com.dicoding.naufal.githubuser.helper.MappingHelper
import com.dicoding.naufal.githubuser.model.WidgetModel

class StackRemoteViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<WidgetModel>()

    override fun onDataSetChanged() {
        val thread: Thread = object : Thread() {
            override fun run() {
                val cursor = mContext.contentResolver.query(UserContract.UserColumns.CONTENT_URI, null, null, null, null)
                val mData = MappingHelper.mapCursorToArrayList(cursor)
                for (userModel in mData){
                    try {
                        val bitmap = Glide.with(mContext)
                            .asBitmap()
                            .load(userModel.avatar)
                            .submit(100, 100)
                            .get()
                        val widgetModel = WidgetModel(username = userModel.username, avatarBitmap = bitmap)
                        mWidgetItems.add(widgetModel)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onCreate() {

    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.widget_image, mWidgetItems[position].avatarBitmap)
        rv.setTextViewText(R.id.widget_textview, mWidgetItems[position].username)

        val extras = bundleOf(
            ImageUserWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.widget_image, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}