package org.elaya.pdd.tools.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.MenuRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import org.elaya.pdd.R
import org.elaya.pdd.db.DataSource
import org.elaya.pdd.settings.Globals


open class FragmentBase : Fragment(),FragmentResultListener {
    companion object{
        const val P_KEY="FRAGMENT_KEY"
    }
    private var menu:Menu?=null
    private fun getFragmentKey():String?
    {
        return javaClass.canonicalName
    }

    protected fun getDB():DataSource?{
        val lContext=context;
        if(lContext != null) {
            return Globals.getDB(lContext);
        }
        return null;
    }

    protected open fun getMenuId():Int
    {
        return -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lKey=getFragmentKey()
        if(lKey != null) {
            setFragmentResultListener(lKey, this::onFragmentResult)
        }
        @MenuRes val lMenu=getMenuId()
        if(lMenu != -1){
            setHasOptionsMenu(true)
        }
    }

    protected open fun afterMenu(pMenu:Menu)
    {

    }

    override fun onCreateOptionsMenu(pMenu: Menu, pInflater: MenuInflater) {
        super.onCreateOptionsMenu(pMenu, pInflater)
        @MenuRes val lMenuId=getMenuId()
        if(lMenuId != -1){
            pInflater.inflate(lMenuId,pMenu)
            menu=pMenu
            afterMenu(pMenu)
        }

    }

    @ColorInt fun  getColor(@ColorRes pColor:Int):Int
    {
        val lContext=context
        if(lContext != null){
            return  ContextCompat.getColor(lContext,pColor)
        }
        return Color.TRANSPARENT
    }

    fun showKeyboard()
    {
        val lActivity=activity
        if(lActivity != null) {
            val lImm = lActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            lImm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    fun hideKeyboard()
    {
        val lActivity=activity

        if(lActivity != null) {

            val lImm = lActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            lImm.hideSoftInputFromWindow(lActivity.window.decorView.windowToken,0)
        }
    }

    fun goBack()
    {
        val lActivity=activity
        lActivity?.supportFragmentManager?.popBackStackImmediate()
    }

    override fun onFragmentResult(pRequestKey: String, pResult: Bundle){
//Default do nothing

    }

    protected inline fun startDialogFragment(pTag:String?,pKey:String, pMakeFragment:()->Fragment) {
        val lActivity=activity
        if(lActivity != null) {
            val lTransaction = lActivity.supportFragmentManager.beginTransaction()
            val lFragment=pMakeFragment()
            var lArgs=lFragment.arguments
            if(lArgs==null){
                lArgs=Bundle()
                lFragment.arguments=lArgs
            }
            lArgs.putString(P_KEY,pKey)
            lTransaction.add(lFragment, pTag).commitAllowingStateLoss()
        }
    }




    protected inline fun startFragment( pTag:String?, pMakeFragment:()->Fragment) {
        val lActivity=activity
        if(lActivity != null) {
            val lTransaction = lActivity.supportFragmentManager.beginTransaction()
            lTransaction.replace(R.id.mainContent, pMakeFragment(), pTag).addToBackStack(null).commitAllowingStateLoss()
        }
    }
}