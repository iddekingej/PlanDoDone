package org.elaya.pdd.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
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
        const val P_lastProject="lastProject"

        fun start():TodoListPagerFragment
        {
            return TodoListPagerFragment()
        }

        fun start(pProjectId:Int,pLast:Boolean):TodoListPagerFragment{
            val lBundle=Bundle()
            lBundle.putInt(P_ProjectID,pProjectId)
            lBundle.putBoolean(P_lastProject,pLast)
            val lFragment=TodoListPagerFragment()
            lFragment.arguments=lBundle
            return lFragment
        }



    }
    private var  projectId:Int=-1
    private var  lastProject=false
    private var  binding:FragmentTodolistBinding?=null
    private var  project: Project?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lArguments=arguments
        if(lArguments != null){
            projectId=lArguments.getInt(P_ProjectID)
            project=Globals.db?.getProject(projectId)
            lastProject=lArguments.getBoolean(P_lastProject)
        }
    }

    override fun onResume() {
        super.onResume()

        val lLast=Globals.db?.getLastProjectId()
        val lBinding=binding
        if(lBinding != null){
            lBinding.previousTodo.visibility=if(projectId>-1){ View.VISIBLE} else {View.GONE}
            lBinding.nextTodo.visibility=if(lLast != null && projectId!=lLast){ View.VISIBLE} else {View.GONE}
        }
    }

    override fun onCreateView(
        pInflater: LayoutInflater,
        pContainer: ViewGroup?,
        pSavedInstanceState: Bundle?
    ): View? {
        super.onCreateView(pInflater, pContainer, pSavedInstanceState)
        val lDb=Globals.db
        var lTodoList: LinkedList<Todo>?=null
        val lBinding=FragmentTodolistBinding.inflate(pInflater,pContainer,false)
        binding=lBinding


        if(lDb != null) {

            if (projectId == -1) {
                lTodoList = lDb.getActiveTodos()
                lBinding.title.setText(R.string.project_active_todo)

            } else {
                lTodoList =lDb.getTodos(projectId)
                val lProject=Globals.db?.getProject(projectId)
                if(lProject != null){
                    lBinding.title.text=lProject.name
                }
            }

            lBinding.nextTodo.setOnClickListener {
                val lBundle=Bundle()
                lBundle.putInt(ProjectListFragment.PAR_DIRECTION,1)
                setFragmentResult(ProjectListFragment.KEY_TODO_LIST_NAVIGATION,lBundle)
            }
            lBinding.previousTodo.setOnClickListener {
                val lBundle=Bundle()
                lBundle.putInt(ProjectListFragment.PAR_DIRECTION,-1)
                setFragmentResult(ProjectListFragment.KEY_TODO_LIST_NAVIGATION,lBundle)
            }

            lBinding.addTodo.setOnClickListener {
                addTodo()
            }
        }
        val lList=object:TodoList(lBinding.todoList){
            override fun onClickEvent(pData: Todo) {

                startFragment("editTodo"){
                    ToDoEditFragment.newInstance(pData)
                }
            }

        }
        if(lTodoList != null) {
            lList.makeList(pInflater, lTodoList)
        }
        return  lBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }


    private fun addTodo()
    {
        val lProject=project
        if(lProject != null) {
            startFragment("editTodo") {

                ToDoEditFragment.newInstance(lProject)
            }
        }
    }
}