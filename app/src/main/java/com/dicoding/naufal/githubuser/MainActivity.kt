package com.dicoding.naufal.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.naufal.githubuser.adapter.UserAdapter
import com.dicoding.naufal.githubuser.databinding.ActivityMainBinding
import com.dicoding.naufal.githubuser.helper.VariableHelper.Companion.isFromFavorite
import com.dicoding.naufal.githubuser.model.UserModel
import com.dicoding.naufal.githubuser.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallBack{
            override fun onItemClicked(userModel: UserModel?, position: Int) {
                intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                isFromFavorite = false
                intent.putExtra(DetailUserActivity.EXTRA_USER, userModel)
                startActivity(intent)
            }
        })


        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        userViewModel.getUser().observe(this, object : Observer<ArrayList<UserModel>>{
            override fun onChanged(userItems: ArrayList<UserModel>) {
                if (userItems != null){
                    adapter.setData(userItems)
                    showLoading(false)
                }
            }
        })
        userViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                showLoading(false)
                binding.guideLinear.visibility = View.VISIBLE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_user_title)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                binding.guideLinear.visibility = View.GONE
                userViewModel.setUser(query, UserViewModel.SEARCH_USER_KEY)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(searchView.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.close_app -> {
                finishAffinity()
                return true
            }
            R.id.action_change_settings -> {
                startActivity(Intent(this, SettingActivity::class.java))
                return true
            }
            R.id.action_favorite_users -> {
                startActivity(Intent(this, FavoriteUserActivity::class.java))
                return true
            }
            else -> return true
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}