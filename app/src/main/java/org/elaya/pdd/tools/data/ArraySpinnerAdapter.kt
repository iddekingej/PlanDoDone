package org.elaya.pdd.tools.data

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class ArraySpinnerAdapter<T>: ArrayAdapter<T> {

        constructor(pContext: Context, pResource:Int, pList:List<T>):super(pContext,pResource,pList)
        {

        }

        constructor(pContext: Context, pResource:Int, pList:Array<T>):super(pContext,pResource,pList)
        {

        }
        override fun getView(pPosition:Int,pConvertView:View?, pParent:ViewGroup):View
        {
            val lView = super.getView(pPosition, pConvertView, pParent)
            lView.setPadding(0,lView.paddingTop,lView.paddingRight,lView.paddingBottom)
            lView.layoutParams.height=ViewGroup.LayoutParams.WRAP_CONTENT
            return lView
        }
}