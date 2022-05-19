package org.elaya.pdd.project

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout

import org.elaya.pdd.databinding.AdapterProjectBinding
import org.elaya.pdd.tools.views.ListViewHandler

abstract class ProjectListViewHandler(pParent:LinearLayout) : ListViewHandler<LinearLayout, Project,AdapterProjectBinding>(pParent) {

    override fun checkData(pObject: Project?): Boolean {
        return pObject is Project
    }


    override fun makeView(pInflater: LayoutInflater, pParent: ViewGroup): AdapterProjectBinding {
        return AdapterProjectBinding.inflate(pInflater,pParent,true)
    }

     override fun fillData(pView: AdapterProjectBinding, pData: Project) {
         pView.name.text=pData.name
     }


}