package com.dicoding.naufal.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.naufal.githubuser.DetailUserActivity
import com.dicoding.naufal.githubuser.adapter.UserAdapter
import com.dicoding.naufal.githubuser.databinding.FragmentFollowerBinding
import com.dicoding.naufal.githubuser.model.UserModel
import com.dicoding.naufal.githubuser.viewmodel.UserViewModel

class FollowsFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    companion object{
        private val ARG_USERNAME = "username"
        private val INPUT_KEY = "input_key"

        fun newInstance(username: String?, inputKey: String?): FollowsFragment{
            val fragment = FollowsFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            bundle.putString(INPUT_KEY, inputKey)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallBack{
            override fun onItemClicked(userModel: UserModel?, position: Int) {
                val intent = Intent(context, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USER, userModel)
                startActivity(intent)
            }
        })

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        userViewModel.getUser().observe(viewLifecycleOwner, object : Observer<ArrayList<UserModel>> {
            override fun onChanged(userItems: ArrayList<UserModel>) {
                if (userItems != null){
                    adapter.setData(userItems)
                    showLoading(false)
                }
            }
        })
        userViewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                showLoading(false)
            }
        })

        val username = arguments?.getString(ARG_USERNAME)
        val inputKey = arguments?.getString(INPUT_KEY)
        showLoading(true)
        if (inputKey != null) {
            userViewModel.setUser(username.toString(), inputKey)
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }
}