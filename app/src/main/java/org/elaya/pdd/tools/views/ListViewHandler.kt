package org.elaya.pdd.tools.views

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import org.elaya.pdd.tools.data.DataEvent

abstract class ListViewHandler<T : ViewGroup,U,V : ViewBinding>(pParent: T) {
    private val parent:T = pParent
    private var _onClickEvent:DataEvent<U>?=null


    protected abstract fun makeView(pInflater:LayoutInflater,pParent:ViewGroup):V;

    protected abstract fun fillData(pView:V,pData:U)

    protected abstract fun checkData(pObject:Any):Boolean

    protected fun getResources(): Resources
    {
        return parent.context.resources;
    }

    abstract fun onClickEvent(pData:U)
    private fun clickEvent(pView:View){

        val lObject=pView.tag
        if(checkData(lObject)) {
                onClickEvent(lObject as U)
            }

        }

    fun makeList(pLayoutInflater: LayoutInflater,pItems:List<U>)
    {

        parent.removeAllViews()
        pItems.forEach {
            val lItem= makeView(pLayoutInflater,parent);
            lItem.root.tag=it;
            fillData(lItem,it)
            lItem.root.setOnClickListener(this::clickEvent)
        }
    }

}