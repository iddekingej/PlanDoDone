package org.elaya.pdd.tools.data

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class ArraySpinnerAdapter<T>(pContext: Context,pResource:Int,pList:List<T>): ArrayAdapter<T>(pContext,pResource,pList) {


        override fun getView(pPosition:Int,pConvertView:View?, pParent:ViewGroup):View
        {
            val lView = super.getView(pPosition, pConvertView, pParent);
            lView.setPadding(0,lView.paddingTop,lView.paddingRight,lView.paddingBottom);
            return lView;
        }
}