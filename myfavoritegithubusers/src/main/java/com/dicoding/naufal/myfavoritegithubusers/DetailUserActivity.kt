package com.dicoding.naufal.myfavoritegithubusers

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.naufal.myfavoritegithubusers.adapter.TabAdapter
import com.dicoding.naufal.myfavoritegithubusers.database.UserContract
import com.dicoding.naufal.myfavoritegithubusers.database.UserContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.naufal.myfavoritegithubusers.databinding.ActivityDetailUserBinding
import com.dicoding.naufal.myfavoritegithubusers.model.UserModel
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private var userModel: UserModel? = null
    private var userName: String? = null
    private var userId: String? = null
    private var avatarUrl: String? = null
    private lateinit var uriWithId: Uri
    private val personalToken = BuildConfig.OPEN_GITHUB_API_KEY

    companion object{
        const val EXTRA_USER = "extra_usermodel"
        private val TAG = DetailUserActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userModel = intent.getParcelableExtra(EXTRA_USER)

        userName = userModel?.username
        userId = userModel?.userId
        avatarUrl = userModel?.avatar

        if (userModel != null){
            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + userId)
        }

        getUserDetail(userName)

        val tabAdapter = TabAdapter(this, supportFragmentManager)
        val viewPager = binding.viewPager
        tabAdapter.userName = userName
        viewPager.adapter = tabAdapter
        val tabLayout = binding.tablayout
        tabLayout.setupWithViewPager(viewPager)

        supportActionBar?.elevation = 0f

        var statusFavorite = false
        val cursor = userId?.let { contentResolver.query(uriWithId, null, null, null, null) }
        if (cursor != null) {
            statusFavorite = cursor.count > 0
        }

        setStatusFavorite(statusFavorite)

        binding.fab.setOnClickListener {
            insertOrDeleteDataUser(statusFavorite)
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)
        }
    }

    private fun insertOrDeleteDataUser(statusFavorite: Boolean) {
        if (statusFavorite){
            contentResolver.delete(uriWithId, null, null)
            showSnackbarMessage(resources.getString(R.string.snackbar_deleted_message, userName))
        }else{
            val values = ContentValues()
            values.put(UserContract.UserColumns.COLUMN_NAME_USERID, userId)
            values.put(UserContract.UserColumns.COLUMN_NAME_USERNAME, userName)
            values.put(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL, avatarUrl)

            contentResolver.insert(CONTENT_URI, values)
            showSnackbarMessage(resources.getString(R.string.snackbar_added_message, userName))
        }
    }

    private fun showSnackbarMessage(string: String) {
        Snackbar.make(binding.coordinatorRoot, string, Snackbar.LENGTH_SHORT).show()
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite){
            binding.fab.setImageResource(R.drawable.ic_baseline_favorite)
        }else{
            binding.fab.setImageResource(R.drawable.ic_baseline_favorite_border)
        }

    }

    private fun getUserDetail(userName: String?) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$userName"

        client.addHeader("Authorization", "token $personalToken")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObjects = JSONObject(result)

                    Glide.with(this@DetailUserActivity).load(responseObjects.getString("avatar_url"))
                        .apply(RequestOptions().override(150, 150))
                        .into(binding.ivAvatar)
                    binding.tvFullname.text = responseObjects.getString("name")
                    binding.tvUsername.text = responseObjects.getString("login")
                    binding.tvCompany.text = responseObjects.getString("company")
                    binding.tvLocation.text = responseObjects.getString("location")
                    binding.tvRepositories.text = responseObjects.getString("public_repos")
                    binding.tvFollowers.text = responseObjects.getString("followers")
                    binding.tvFollowing.text = responseObjects.getString("following")
                }catch (e: Exception){
                    Toast.makeText(this@DetailUserActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.VISIBLE

                val errorMessage = when(statusCode){
                    401 -> "$statusCode: Bad Request"
                    403 -> "$statusCode: Forbidden"
                    404 -> "$statusCode: Not Found"
                    else -> "$statusCode: ${error.message}"
                }
                Toast.makeText(this@DetailUserActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        return true
    }
}