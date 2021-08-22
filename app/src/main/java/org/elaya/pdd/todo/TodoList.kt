package org.elaya.pdd.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.annotation.DrawableRes
import org.elaya.pdd.R
import org.elaya.pdd.databinding.TodoIncItemBinding
import org.elaya.pdd.tools.views.ListViewHandler

abstract class TodoList(pParent: TableLayout): ListViewHandler<TableLayout, Todo, TodoIncItemBinding>(pParent) {


    override fun fillData(pView: TodoIncItemBinding, pData: Todo) {
        pView.title.text=pData.title
        val lStatusDesc=getResources().getStringArray(R.array.todo_status_modes);

        val lStatus=pData.status;
        if(lStatus==Todo.STATUS_FINISHED || lStatus==Todo.STATUS_STOPPED) {
            pView.title.setBackgroundResource(R.drawable.strike)
        }
        if(lStatus>=0 && lStatus<lStatusDesc.size) {
            pView.status.contentDescription = lStatusDesc[lStatus];
            @DrawableRes var lImage: Int = -1;
            if (lStatus == Todo.STATUS_STARTED) {
                lImage = R.drawable.todo_start
            } else if (lStatus == Todo.STATUS_NEW) {
                lImage = R.drawable.todo_new
            } else if (lStatus == Todo.STATUS_STOPPED) {
                lImage = R.drawable.todo_stopped
            }
            if (lImage != -1) {
                pView.status.setImageResource(lImage)
            }
        }
    }

    override fun checkData(pObject: Any): Boolean {
        return pObject is Todo;
    }



    override fun makeView(pInflater: LayoutInflater, pParent: ViewGroup): TodoIncItemBinding {
        return TodoIncItemBinding.inflate(pInflater,pParent,true);
    }
}