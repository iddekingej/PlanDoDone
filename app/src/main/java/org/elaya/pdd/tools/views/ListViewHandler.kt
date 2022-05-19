package org.elaya.pdd.tools.views

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.viewbinding.ViewBinding
import org.elaya.pdd.R
import org.elaya.pdd.tools.data.Data
import org.elaya.pdd.tools.data.DataEvent

abstract class ListViewHandler<T : ViewGroup,U : Data,V : ViewBinding>(pParent: T) {
    private val parent:T = pParent
    private var _onClickEvent:DataEvent<U>?=null
    private var views:Array<ViewBinding?>?=null;
    private var items:List<U>?=null;
    private var selected:Int=-1;
    private var _selectedItem:U?=null;
    val selectedItem:U? get()=_selectedItem;



    protected abstract fun makeView(pInflater:LayoutInflater,pParent:ViewGroup):V

    protected abstract fun fillData(pView:V,pData:U)

    protected abstract fun checkData(pObject:U?):Boolean

    protected fun getResources(): Resources
    {
        return parent.context.resources
    }

    abstract fun onClickEvent(pData:U)

    fun findObject(pObject:U):Int{
        val lItems=items;
        if(lItems != null) {
            for ((lIndex, lItem) in lItems.withIndex()) {
                if(lItem.id == pObject.id ){
                    return lIndex;
                }
            }
        }
        return -1;
    }

    fun setSelection(pObject:U)
    {
        val lFoundIndex=findObject(pObject)
        if(lFoundIndex != -1 && lFoundIndex != selected){
            setSelection(lFoundIndex);
        } else {
            _selectedItem=null;
            selected=-1;
        }
    }

    private fun displaySelection(pIndex:Int,pSelected:Boolean)
    {
        val lViews=views;
        if(lViews != null) {
            val lView = lViews[pIndex];
            if(lView != null) {
                if (pSelected) {
                    lView.root.setBackgroundResource(R.drawable.selected)
                } else {
                    lView.root.setBackgroundColor(Color.TRANSPARENT)
                }
                if(lView.root is ViewGroup) {
                    val lTextView =(lView.root as ViewGroup).getChildAt(0);
                    if (lTextView is TextView) {

                        lTextView.setTextColor(
                            getColor(
                                parent.context,
                                if (pSelected) {
                                    R.color.color_text_selected
                                } else {
                                    R.color.gen_text_color
                                }
                            )
                        )
                    }
                }
            }
        }
    }

    fun setSelection(pIndex:Int){
        var lItems = items;
        if(lItems != null) {
            if (selected != -1) {
                displaySelection(selected, false)
            }
            selected = pIndex;
            _selectedItem = lItems[selected];
            displaySelection(pIndex, true)
        }
    }


    private fun clickEvent(pView:View) {
        var lItems = items;
        if (lItems != null) {
            var lIndex=pView.tag;
            if(lIndex is Int) {

                val lObject = lItems[lIndex];
                if (checkData(lObject)) {
                    onClickEvent(lObject )
                    setSelection(lIndex);
                }
            }

        }
    }

    fun makeList(pLayoutInflater: LayoutInflater,pItems:List<U>)
    {

        parent.removeAllViews()
        views= Array(pItems.size){null}
        items=pItems;
        var lCnt=0;
        pItems.forEach {
            val lItem= makeView(pLayoutInflater,parent)
            lItem.root.tag=lCnt
            fillData(lItem,it)
            lItem.root.setOnClickListener(this::clickEvent)
            var lItems=views
            if(lItems != null) {
                lItems[lCnt] = lItem;
            }
            lCnt++
        }
        val lSelectedItem=_selectedItem;
        selected=-1
        _selectedItem=null
        if(lSelectedItem != null) {
            setSelection(lSelectedItem);
        }
    }

}