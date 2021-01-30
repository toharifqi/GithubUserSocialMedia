package com.dicoding.naufal.myfavoritegithubusers

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.naufal.myfavoritegithubusers.adapter.UserAdapter
import com.dicoding.naufal.myfavoritegithubusers.database.UserContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.naufal.myfavoritegithubusers.databinding.ActivityFavoriteUserBinding
import com.dicoding.naufal.myfavoritegithubusers.helper.MappingHelper
import com.dicoding.naufal.myfavoritegithubusers.model.UserModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: UserAdapter

    companion object{
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallBack{
            override fun onItemClicked(userModel: UserModel?, position: Int) {
                intent = Intent(this@FavoriteUserActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USER, userModel)
                startActivity(intent)
            }
        })

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUsersAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null){
            loadUsersAsync()
        }else{
            savedInstanceState.getParcelableArrayList<UserModel>(EXTRA_STATE)?.also { adapter.setData(it) }
        }
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main){
            showLoading(true)
            val deferredUsers = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(false)
            val users = deferredUsers.await()
            if (users.size > 0){
                adapter.setData(users)
            }else{
                showDesclaimer(true)
            }
        }
    }

    private fun showDesclaimer(b: Boolean) {
        if (b){
            binding.emptyLinear.visibility = View.VISIBLE
            showSnackbarMessage(getString(R.string.empty_favorite))
        }else{
            binding.emptyLinear.visibility = View.GONE
        }

    }

    private fun showSnackbarMessage(s: String) {
        Snackbar.make(binding.recyclerview , s, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.mData)
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                startActivity(Intent(Intent(this, FavoriteUserActivity::class.java)))
                return true
            }
            R.id.close_app -> {
                finishAffinity()
                return true
            }
            R.id.action_change_settings -> {
                startActivity(Intent(this, SettingActivity::class.java))
                return true
            }
            else -> return true
        }
    }
}