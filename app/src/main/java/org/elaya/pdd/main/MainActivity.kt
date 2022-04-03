package org.elaya.pdd.main

import android.os.Bundle
import org.elaya.pdd.R
import org.elaya.pdd.databinding.ActivityMainBinding
import org.elaya.pdd.project.ProjectListFragment
import org.elaya.pdd.tools.activities.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lBinding=ActivityMainBinding.inflate(layoutInflater)
        val lTransaction = supportFragmentManager.beginTransaction()
        lTransaction.replace(R.id.mainContent,ProjectListFragment.newInstance(), "projectEdit")
            .commitAllowingStateLoss()
        setContentView(lBinding.root)

    }


        override fun onBackPressed() {
        if(supportFragmentManager.fragments.size>0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }





}