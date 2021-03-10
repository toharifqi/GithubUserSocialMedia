package com.dicoding.naufal.githubuser.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.naufal.githubuser.R
import com.dicoding.naufal.githubuser.fragment.FollowsFragment
import com.dicoding.naufal.githubuser.viewmodel.UserViewModel.Companion.FOLLOWER_KEY
import com.dicoding.naufal.githubuser.viewmodel.UserViewModel.Companion.FOLLOWING_KEY

class TabAdapter(private val context: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var userName: String? = null

    @StringRes
    private val tabTitles = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = FollowsFragment.newInstance(userName, FOLLOWER_KEY)
            1 -> fragment = FollowsFragment.newInstance(userName, FOLLOWING_KEY)
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = tabTitles.size

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitles[position])
    }
}