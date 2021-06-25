package org.elaya.pdd.tools.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import org.elaya.pdd.tools.data.DataEvent

abstract class ListViewHandler<T : ViewGroup,U>(pParent: T) {
    private val parent:T = pParent
    private var _onClickEvent:DataEvent<U>?=null


    @LayoutRes protected  abstract fun getItemRes():Int
    protected abstract fun fillData(pView:View,pData:U)

    protected abstract fun checkData(pObject:Any):Boolean

    abstract fun onClickEvent(pData:U)
    private fun clickEvent(pView:View){

            val lObject=pView.tag
        if(checkData(lObject)) {
                onClickEvent(lObject as U)
            }

        }

    fun makeList(pLayoutInflater: LayoutInflater,pItems:List<U>)
    {
        val lLayout=getItemRes()
        parent.removeAllViews()
        pItems.forEach {
            val lItem=pLayoutInflater.inflate(lLayout,parent,false)
            lItem.tag = it
            fillData(lItem,it)
            parent.addView(lItem)
            lItem.setOnClickListener(this::clickEvent)
        }
    }

}