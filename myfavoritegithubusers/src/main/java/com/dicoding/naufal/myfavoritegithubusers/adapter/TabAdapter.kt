package com.dicoding.naufal.myfavoritegithubusers.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.naufal.myfavoritegithubusers.R
import com.dicoding.naufal.myfavoritegithubusers.fragment.FollowerFragment
import com.dicoding.naufal.myfavoritegithubusers.fragment.FollowingFragment

class TabAdapter(private val context: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var userName: String? = null

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = FollowerFragment.newInstance(userName)
            1 -> fragment = FollowingFragment.newInstance(userName)
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = 2

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }
}