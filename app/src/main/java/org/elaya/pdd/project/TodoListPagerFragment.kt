package org.elaya.pdd.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import org.elaya.pdd.R
import org.elaya.pdd.databinding.FragmentTodolistBinding
import org.elaya.pdd.settings.Globals
import org.elaya.pdd.todo.ToDoEditFragment
import org.elaya.pdd.todo.Todo
import org.elaya.pdd.todo.TodoList
import org.elaya.pdd.tools.fragments.FragmentBase
import java.util.*

class TodoListPagerFragment:FragmentBase() {
    companion object {
        const val P_ProjectID = "projectId"


        fun start():TodoListPagerFragment
        {
            return TodoListPagerFragment();
        }

        fun start(pProjectId:Int):TodoListPagerFragment{
            val lBundle=Bundle();
            lBundle.putInt(P_ProjectID,pProjectId);
            val lFragment=TodoListPagerFragment();
            lFragment.arguments=lBundle;
            return lFragment;
        }



    }
    private var  projectId:Int=-1;
    private var  binding:FragmentTodolistBinding?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lArguments=arguments;
        if(lArguments != null){
            projectId=lArguments.getInt(P_ProjectID);
        }
    }

    override fun onCreateView(
        pInflater: LayoutInflater,
        pContainer: ViewGroup?,
        pSavedInstanceState: Bundle?
    ): View? {
        super.onCreateView(pInflater, pContainer, pSavedInstanceState);
        val lDb=Globals.db;
        var lTodoList: LinkedList<Todo>?=null;
        val lBinding=FragmentTodolistBinding.inflate(pInflater,pContainer,false);
        binding=lBinding;

        if(lDb != null) {

            if (projectId == -1) {
                lTodoList = lDb.getActiveTodos()
                lBinding.title.setText(R.string.project_active_todo)
            } else {
                lTodoList =lDb.getTodos(projectId);
                val lProject=Globals.db?.getProject(projectId);
                if(lProject != null){
                    lBinding.title.text=lProject.name;
                }
            }
        }
        val lList=object:TodoList(lBinding.todoList){
            override fun onClickEvent(pData: Todo) {

                startFragment("editTodo"){
                    ToDoEditFragment.newInstance(pData)
                }
            }

        };
        if(lTodoList != null) {
            lList.makeList(pInflater, lTodoList);
        }
        return  lBinding.root;
    }
}