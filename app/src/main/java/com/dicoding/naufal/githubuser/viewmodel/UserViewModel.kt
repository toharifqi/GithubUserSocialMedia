package com.dicoding.naufal.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.naufal.githubuser.BuildConfig
import com.dicoding.naufal.githubuser.model.UserModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class UserViewModel: ViewModel() {

    val listUsers = MutableLiveData<ArrayList<UserModel>>()
    private val personalToken = BuildConfig.OPEN_GITHUB_API_KEY
    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    companion object{
        const val SEARCH_USER_KEY = "search_user_key"
        const val FOLLOWER_KEY = "user_follower_key"
        const val FOLLOWING_KEY = "user_following_key"
    }

    fun setUser(username: String, key: String){

        val urlSearch = "https://api.github.com/search/users?q="
        val urlUserDetail = "https://api.github.com/users/"
        val urlFollowers = "/followers"
        val urlFollowing = "/following"

        when(key){
            SEARCH_USER_KEY -> {
                val urlString = urlSearch + username
                getGithubUser(urlString, true)
            }
            FOLLOWER_KEY -> {
                val urlString = urlUserDetail + username + urlFollowers
                getGithubUser(urlString, false)
            }
            FOLLOWING_KEY -> {
                val urlString = urlUserDetail + username + urlFollowing
                getGithubUser(urlString, false)
            }
        }

    }

    private fun getGithubUser(url: String, search: Boolean) {
        val listItems = ArrayList<UserModel>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $personalToken")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)

                    if (search){
                        val responseObject = JSONObject(result)
                        val list = responseObject.getJSONArray("items")

                        for (i in 0 until list.length()){
                            val userJSON = list.getJSONObject(i)
                            val userModel = UserModel()
                            userModel.username = userJSON.getString("login")
                            userModel.userId = userJSON.getString("id")
                            userModel.avatar = userJSON.getString("avatar_url")

                            listItems.add(userModel)
                        }
                    }else{
                        val jsonArray = JSONArray(result)

                        for (i in 0 until jsonArray.length()){
                            val userJSON = jsonArray.getJSONObject(i)
                            val userModel = UserModel()
                            userModel.username = userJSON.getString("login")
                            userModel.userId = userJSON.getString("id")
                            userModel.avatar = userJSON.getString("avatar_url")

                            listItems.add(userModel)
                        }
                    }

                    listUsers.postValue(listItems)
                }catch (e: Exception){
                    statusMessage.value = Event(e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                val errorMessage = when(statusCode){
                    401 -> "$statusCode: Bad Request"
                    403 -> "$statusCode: Forbidden"
                    404 -> "$statusCode: Not Found"
                    else -> "$statusCode: ${error.message}"
                }
                statusMessage.value = Event(errorMessage)

            }
        })
    }

    fun getUser(): LiveData<ArrayList<UserModel>>{
        return listUsers
    }
}