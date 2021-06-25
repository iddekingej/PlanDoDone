package org.elaya.pdd.project

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import org.elaya.pdd.R
import org.elaya.pdd.tools.views.ListViewHandler

abstract class ProjectListViewHandler(pParent:LinearLayout) : ListViewHandler<LinearLayout, Project>(pParent) {

    override fun checkData(pObject: Any): Boolean {
        return pObject is Project
    }

    override fun getItemRes(): Int {
        return R.layout.adapter_project
    }


     override fun fillData(pView: View, pData: Project) {
         val lName:TextView=pView.findViewById(R.id.name)
         lName.text = pData.name
     }


}