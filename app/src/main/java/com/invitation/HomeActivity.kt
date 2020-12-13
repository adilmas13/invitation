package com.invitation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.mehta.akshaywedding.*
import de.hdodenhof.circleimageview.BuildConfig
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            navigation.menu.setGroupCheckable(0, true, true)
            container.setBackgroundResource(0)
            when (item.itemId) {
                R.id.invitation -> {
                    replaceFragment(InvitationFragment())
                    setToolbarTitle(R.string.invitation)
                }
                R.id.gallery -> {
                    replaceFragment(GalleryFragment())
                    setToolbarTitle(R.string.gallery)
                }
                R.id.mandva -> {
                    replaceFragment(MandavaFragment())
                    setToolbarTitle(R.string.mandva)
                }
                R.id.sangeet -> {
                    replaceFragment(SangeetFragment())
                    setToolbarTitle(R.string.sangeet_sandhya)
                }
                R.id.marriage -> {
                    replaceFragment(MarriageFragment())
                    setToolbarTitle(R.string.marriage)
                }
            }
            true
        }

    private val test = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.invitation -> {
                navigation.selectedItemId = R.id.invitation
            }
            R.id.gallery -> {
                navigation.selectedItemId = R.id.gallery
            }
            R.id.mandva -> {
                navigation.selectedItemId = R.id.mandva
            }
            R.id.sangeet -> {
                navigation.selectedItemId = R.id.sangeet
            }
            R.id.marriage -> {
                navigation.selectedItemId = R.id.marriage
            }
            R.id.location -> {
                unSelectAll()
                replaceFragment(LocationFragment())
                setToolbarTitle(R.string.location)
            }
            R.id.share -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
                )
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
        }
        drawer_layout.closeDrawers()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setup()
        unSelectAll()
    }

    private fun unSelectAll() {
        navigation.menu.setGroupCheckable(0, false, true)
    }

    private fun setup() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
            title = getString(R.string.akshay_weds_hetal)
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        nav_view.setNavigationItemSelectedListener(test)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setToolbarTitle(@StringRes id: Int) {
        supportActionBar?.title = getString(id)
    }

    private fun replaceFragment(fragment: Fragment) {
        if (supportFragmentManager.fragments.size > 0 &&
            supportFragmentManager.fragments[0].tag == fragment.javaClass.canonicalName
        ) {
            return
        }
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(R.id.container, fragment, fragment.javaClass.canonicalName)
            .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

}
