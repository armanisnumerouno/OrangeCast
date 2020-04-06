package com.example.orangecast.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.orangecast.R
import com.example.orangecast.view.discover.DiscoverFragment

class HomeFragment : Fragment() {

    private val discoverFragment = DiscoverFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun initBottomNavigationBar() {

    }

    private fun showFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()

    }
}