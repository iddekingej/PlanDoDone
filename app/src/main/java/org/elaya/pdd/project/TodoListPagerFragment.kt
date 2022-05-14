package org.elaya.pdd.project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.elaya.pdd.R
import org.elaya.pdd.databinding.FragmentTodolistBinding
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
            project=getDB()?.getProject(projectId)
            lastProject=lArguments.getBoolean(P_lastProject)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TODO", "onResume $projectId")
        val lBinding=binding
        if(lBinding != null){

            refreshData()
            val lParent=lBinding.root.parent
            if(lParent != null) {
                Log.d("TODO","On Resume view="+lParent.javaClass.name+":"+(lParent as View).id)
            }
        }

    }

    fun refreshData()
    {
        val lBinding=binding
        if(lBinding != null) {
            if (projectId == -1) {

                lBinding.title.setText(R.string.project_all_todo)

            } else {
                val lProject = getDB()?.getProject(projectId)
                if (lProject != null) {
                    lBinding.title.text = lProject.name
                }
            }
        }
    }

    override fun onCreateView(
        pInflater: LayoutInflater,
        pContainer: ViewGroup?,
        pSavedInstanceState: Bundle?
    ): View {
        super.onCreateView(pInflater, pContainer, pSavedInstanceState)
        Log.d("TODO", "onCreateView $projectId")

        val lDb=getDB()
        var lTodoList: LinkedList<Todo>?=null
        val lBinding=FragmentTodolistBinding.inflate(pInflater,pContainer,false)
        binding=lBinding


        if(lDb != null) {

            if (projectId == -1) {
                lTodoList = lDb.getActiveTodos()
                lBinding.title.setText(R.string.project_all_todo)

            } else {
                lTodoList =lDb.getTodos(projectId)
                val lProject=getDB()?.getProject(projectId)
                if(lProject != null){
                    lBinding.title.text=lProject.name
                }
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

        startFragment("editTodo") {

            ToDoEditFragment.newInstance(project)
        }

    }
}