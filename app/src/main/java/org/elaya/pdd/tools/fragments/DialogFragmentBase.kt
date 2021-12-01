package org.elaya.pdd.tools.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult

open class DialogFragmentBase:DialogFragment(){

    protected fun dismissResult(pResults:Bundle){
        val lArguments=arguments
        if(lArguments != null) {
            val lKey=lArguments.getString(FragmentBase.P_KEY)
            if(lKey != null){
                setFragmentResult(lKey,pResults)
            }
        }
        //todo exception when result is not set
        dismiss()
    }




}