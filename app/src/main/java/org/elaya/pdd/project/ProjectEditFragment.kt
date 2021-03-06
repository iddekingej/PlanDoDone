package org.elaya.pdd.project

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import org.elaya.pdd.databinding.DialogProjectBinding
import org.elaya.pdd.settings.Globals
import org.elaya.pdd.tools.fragments.DialogFragmentBase

class ProjectEditFragment: DialogFragmentBase() {
    companion object {
        const val P_NAME = "name"
        const val P_IS_ACTIVE = "isActive"
        const val P_ID="id"
        const val R_PROJECT_ID="projectId"
        const val R_IS_NEW="isNew"

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
        val lDb=getDB()
        if(lBinding != null )  {
            var lId=-1;
            if(lDb != null) {
                lId = lDb.saveProject(
                    projectId,
                    lBinding.projectNameEdit.text.toString(),
                    lBinding.isActive.isChecked
                )
            }
            val lBundle=Bundle()
            lBundle.putInt(R_PROJECT_ID,lId)
            lBundle.putBoolean(R_IS_NEW,projectId == -1)
            dismissResult(lBundle)
        } else {
            dismiss()
        }
    }
    private fun dialogCancel(pView:View)
    {
        dismiss()
    }

    private fun deleteProject(pView:View)
    {
        getDB()?.deleteProject(projectId)
        dismissResult(Bundle())
    }

    override fun onCreateView(
        pInflater: LayoutInflater,
        pContainer: ViewGroup?,
        pSavedInstanceState: Bundle?
    ): View {

        val lBinding=DialogProjectBinding.inflate(pInflater,pContainer,false)
        lBinding.cancel.setOnClickListener(this::dialogCancel)
        lBinding.save.setOnClickListener(this::dialogSave)
        lBinding.remove.setOnClickListener(this::deleteProject)
        binding=lBinding
        val lArguments=arguments
        if(lArguments==null|| !lArguments.containsKey(P_NAME)){
            lBinding.isActive.isChecked=true
            projectId=-1
            lBinding.remove.visibility=View.GONE
            lBinding.save.isEnabled=false
        } else {
            val lName=lArguments.getString(P_NAME,"")
            lBinding.projectNameEdit.setText(lName)
            lBinding.isActive.isChecked=lArguments.getBoolean(P_IS_ACTIVE)
            projectId=lArguments.getInt(P_ID,-1)
            lBinding.remove.visibility=if(getDB()?.projectHasTodo(projectId)==true){ View.GONE} else {View.VISIBLE}
            lBinding.save.isEnabled=lName.isNotEmpty()
        }
        lBinding.projectNameEdit.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(pText: Editable?) {
                lBinding.save.isEnabled=pText !=null && pText.isNotEmpty()
            }

        })

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