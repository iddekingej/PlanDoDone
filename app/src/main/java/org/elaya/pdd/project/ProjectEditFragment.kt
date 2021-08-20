package org.elaya.pdd.project

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import org.elaya.pdd.databinding.DialogProjectBinding
import androidx.fragment.app.setFragmentResult
import org.elaya.pdd.tools.fragments.DialogFragmentBase

class ProjectEditFragment: DialogFragmentBase() {
    companion object {
        const val P_NAME = "name"
        const val P_IS_ACTIVE = "isActive"
        const val P_ID="id"

        fun newInstance(pId:Int,pName:String,pIsActive:Boolean):ProjectEditFragment{
            val lBundle=Bundle()
            lBundle.putString(P_NAME,pName)
            lBundle.putBoolean(P_IS_ACTIVE,pIsActive)
            lBundle.putInt(P_ID,pId)
            val lFragment=ProjectEditFragment()
            lFragment.arguments=lBundle
            return lFragment
        }

        fun newInstance():ProjectEditFragment{
            return ProjectEditFragment()
        }

    }

    private var binding:DialogProjectBinding?=null
    private var projectId:Int=-1

    private fun dialogSave(pView:View){
        val lBinding=binding
        if(lBinding != null) {
            val lBundle = Bundle().apply {
                putInt(P_ID, projectId)
                putString(P_NAME, lBinding.personNameEdit.text.toString())
                putBoolean(P_IS_ACTIVE,lBinding.isActive.isChecked)
            }

            dismissResult(lBundle)
        } else {
            dismiss()
        }
    }
    private fun dialogCancel(pView:View)
    {
        dismiss()
    }

    override fun onCreateView(
        pInflater: LayoutInflater,
        pContainer: ViewGroup?,
        pSavedInstanceState: Bundle?
    ): View {

        val lBinding=DialogProjectBinding.inflate(pInflater,pContainer,false)
        lBinding.cancel.setOnClickListener(this::dialogCancel)
        lBinding.save.setOnClickListener(this::dialogSave)

        val lArguments=arguments
        if(lArguments==null|| !lArguments.containsKey(P_NAME)){
            lBinding.isActive.isChecked=true
            projectId=-1
        } else {
            lBinding.personNameEdit.setText(lArguments.getString(P_NAME,""))
            lBinding.isActive.isChecked=lArguments.getBoolean(P_IS_ACTIVE)
            projectId=lArguments.getInt(P_ID,-1)
        }
        binding=lBinding
        return lBinding.root
    }



    override fun onResume() {
        super.onResume()
             val lDialog:Dialog?=dialog
        if(lDialog != null) {
                val lWindow: Window?=lDialog.window
            if(lWindow != null) {
                    val params = lWindow.attributes
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    lWindow.attributes=params
            }
            }
    }


}