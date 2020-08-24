package com.edgeatzero.projects.pixiv.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.edgeatzero.library.base.BaseActivity
import com.edgeatzero.library.ext.startActivity
import com.edgeatzero.library.ext.viewModels
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.databinding.ActivityMainBinding
import com.edgeatzero.projects.pixiv.databinding.NavigationHeaderMainBinding
import com.edgeatzero.projects.pixiv.ui.common.inflateHeaderView
import com.edgeatzero.projects.pixiv.ui.login.LoginActivity
import com.edgeatzero.projects.pixiv.ui.login.LoginFragment
import com.edgeatzero.projects.pixiv.ui.search.SearchActivity
import com.edgeatzero.projects.pixiv.util.LambdaFragmentPagerAdapter
import com.edgeatzero.projects.pixiv.util.Settings

class MainActivity : BaseActivity<ActivityMainBinding>() {

    public override val binding by binding(R.layout.activity_main)

    private val model by viewModels<MainViewModel>()

    private val adapter by lazy {
        LambdaFragmentPagerAdapter(this, count = { 2 }, creator = {
            when (it) {
                0 -> RecommendedFragment()
                1 -> LoginFragment()
                else -> throw  IndexOutOfBoundsException()
            }
        })
    }

    override fun onBackPressed() {
        when {
            binding.DrawerLayout.isDrawerOpen(binding.NavigationView) -> {
                binding.DrawerLayout.closeDrawer(binding.NavigationView)
            }
            else -> super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Settings.isValidAccount.not()) {
            startActivity<LoginActivity>()
            finishAfterTransition()
        }
        delegate.localNightMode = Settings.nightMode
        super.onCreate(savedInstanceState)
        isLayoutFullscreen = true
        statusBarColor = Color.TRANSPARENT

        binding.ViewPager.adapter = adapter
        binding.AnimatedBottomBar.setupWithViewPager(binding.ViewPager)

        binding.NavigationView.inflateHeaderView<NavigationHeaderMainBinding>().apply {
            account = model.account
            lifecycleOwner = this@MainActivity
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                startActivity<SearchActivity>()
                true
            }
            else -> false
        }
    }

}
