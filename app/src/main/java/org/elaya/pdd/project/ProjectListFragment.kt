package org.elaya.pdd.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.elaya.pdd.databinding.FragmentProcesListBinding
import org.elaya.pdd.settings.Globals
import org.elaya.pdd.todo.Todo
import org.elaya.pdd.todo.TodoList
import org.elaya.pdd.tools.fragments.FragmentBase


/**
 * Display the projects
 * Use the [ProjectListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectListFragment : FragmentBase() {
    private var binding:FragmentProcesListBinding?=null
    private var projectHandler:ProjectListViewHandler?=null
    private var todoListHandler: TodoList?=null

    override fun onCreateView(
        pInflater: LayoutInflater, pContainer: ViewGroup?,
        pSavedInstanceState: Bundle?
    ): View {
        val lBinding= FragmentProcesListBinding.inflate(pInflater,pContainer,false)
        binding=lBinding
        val lList=lBinding.projectList
        projectHandler = object:ProjectListViewHandler(lList){
            override fun onClickEvent(pData:Project){
                clickItem(pData)
            }
        }
        lBinding.projectAdd.setOnClickListener(this::newProject)
        todoListHandler=object:TodoList(lBinding.todoList){
            override fun onClickEvent(pData: Todo) {
            }

        }
        return lBinding.root
    }


    private fun clickItem(pProject:Project)
    {
        val lActivity=activity
        if(lActivity !=null) {
            startFragment("project"){
                ProjectFragment.start(pProject.id)
            }
        }

    }

    private fun newProject(pView:View){
        val lActivity=activity
        if(lActivity != null) {

            startDialogFragment("projectEdit"){
                ProjectEditFragment.newInstance()
            }
        }
    }


    private fun setupProjectList()
    {
        val lDb= Globals.db
        val lBinding = binding

        if(lDb != null && lBinding != null) {
            val lList = lDb.getProjects()
            projectHandler?.makeList(layoutInflater, lList)
            if (lList.size == 0) {
                lBinding.projectAddHelp.visibility = View.VISIBLE
                lBinding.projectList.visibility = View.GONE
            } else {
                lBinding.projectAddHelp.visibility = View.GONE
                lBinding.projectList.visibility = View.VISIBLE
            }
            val lTodoList = todoListHandler
            if(lTodoList != null) {
                val lTodos = lDb.getActiveTodos()
                val lIsEmpty=lTodos.isEmpty()
                lBinding.todoListScroll.visibility=if(lIsEmpty){View.GONE} else {View.VISIBLE}
                if(!lTodos.isEmpty()) {
                    lTodoList.makeList(layoutInflater, lTodos)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        setupProjectList()
    }

    override fun onFragmentResult(pRequestKey: String, pResult: Bundle) {
                val lId=pResult.getInt(ProjectEditFragment.P_ID)
        val lName=pResult.getString(ProjectEditFragment.P_NAME)
        val lIsActive=pResult.getBoolean(ProjectEditFragment.P_IS_ACTIVE,false)
        Globals.db?.saveProject(lId,lName?:"",lIsActive)
                setupProjectList()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProjectListFragment()
    }
}