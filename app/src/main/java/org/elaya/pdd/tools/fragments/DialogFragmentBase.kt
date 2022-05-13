package org.elaya.pdd.tools.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import org.elaya.pdd.db.DataSource
import org.elaya.pdd.settings.Globals

abstract class DialogFragmentBase:DialogFragment(){

    protected fun getDB(): DataSource?{
        var lContext=context;
        if(lContext != null) {
            return Globals.getDB(lContext);
        }
        return null;
    }


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