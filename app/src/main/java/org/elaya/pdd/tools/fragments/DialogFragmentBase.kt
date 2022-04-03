package org.elaya.pdd.tools.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment

abstract class DialogFragmentBase:DialogFragment(){


    protected fun dismissResult(pResults:Bundle){
        val lArguments=arguments
        if(lArguments != null) {
            val lKey=lArguments.getString(FragmentBase.P_KEY)
            if(lKey != null){
                Log.d("TODO", "Dismiss key=$lKey")
                parentFragmentManager.setFragmentResult(lKey,pResults)
            }
        }
        //todo exception when result is not set
        dismiss()
    }




}