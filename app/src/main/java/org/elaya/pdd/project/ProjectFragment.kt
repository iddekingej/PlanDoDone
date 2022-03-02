package org.elaya.pdd.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.elaya.pdd.databinding.ActivityProjectBinding
import org.elaya.pdd.settings.Globals
import org.elaya.pdd.todo.ToDoEditFragment
import org.elaya.pdd.todo.Todo
import org.elaya.pdd.todo.TodoList
import org.elaya.pdd.tools.fragments.FragmentBase

class ProjectFragment:FragmentBase() {
    private var projectId:Int=-1
    private var project:Project?=null
    private var todoList:TodoList?=null

    companion object{
        const val P_ProjectID="projectId"

        fun start(pProjectId:Int):ProjectFragment
        {

            val lBundle=Bundle()
            lBundle.putInt(P_ProjectID,pProjectId)
            val lFragment=ProjectFragment()
            lFragment.arguments=lBundle
            return lFragment
        }
    }



    override fun onFragmentResult(pRequestKey: String, pResult: Bundle) {
        project = Globals.db?.getProject(projectId)
        if(project==null){
            goBack()
        } else {
            binding?.projectName?.text = project?.name
        }

    }

    private var binding:ActivityProjectBinding?=null

    private fun editProject(pView: View){
        val lProject=project
        if(lProject != null) {

            startDialogFragment("projectEdit") {
                    ProjectEditFragment.newInstance(
                        lProject.id,
                        lProject.name,
                        lProject.isActive
                    )
            }
        }
    }



    override fun onCreateView(
        pInflater: LayoutInflater, pContainer: ViewGroup?,
        pSavedInstanceState: Bundle?
    ): View {
        val lBinding= ActivityProjectBinding.inflate(layoutInflater)
        binding=lBinding

        val lProjectId=arguments?.getInt(P_ProjectID,-1)
        if(lProjectId != null) {
            projectId=lProjectId
            val lProject = Globals.db?.getProject(lProjectId)
            project = lProject
            if (lProject != null) {
                lBinding.projectName.text = lProject.name
                lBinding.editButton.setOnClickListener(this::editProject)
            }
        }
        lBinding.addTodo.setOnClickListener(this::addTodo)
        todoList=object:TodoList(lBinding.todoList){
            override fun onClickEvent(pData:Todo){
                startFragment("editTodo"){
                    ToDoEditFragment.newInstance(pData)
                }
            }
        }
        return lBinding.root
    }

    override fun onResume() {
        super.onResume()
        makeTodoList()
    }

    private fun addTodo(pView:View)
    {
        val lProject=project
        if(lProject != null) {
            startFragment("editTodo") {

                ToDoEditFragment.newInstance(lProject)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null;
    }

    private fun makeTodoList()
    {
        val lProject=project
        val lBinding=binding
        val lTodolist=todoList

        val lLayoutInflater=layoutInflater
        if(lProject != null && lBinding != null && lTodolist != null) {
            val lDb=Globals.db
            if(lDb != null) {
                val lTodos= lDb.getTodos(lProject.id)

                lTodolist.makeList(lLayoutInflater,lTodos)
            }
        }
    }

}