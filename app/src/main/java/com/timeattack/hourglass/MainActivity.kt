package com.timeattack.hourglass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.timeattack.hourglass.Fragment.HomeFragment
import com.timeattack.hourglass.Fragment.PeopleFragment
import com.timeattack.hourglass.Fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG : String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Firebase
        auth = FirebaseAuth.getInstance()

        // 하단메뉴바
        title = resources.getString(R.string.home)
        loadFragment(HomeFragment())

        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home-> {
                    title=resources.getString(R.string.home)
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_people-> {
                    title=resources.getString(R.string.people)
                    loadFragment(PeopleFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_view-> {
                    title=resources.getString(R.string.settings)
                    loadFragment(SettingsFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false
        }
        // 로그아웃
        bt_logout.setOnClickListener {
            auth.signOut()
            finish()
        }

    }


    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
