package org.elaya.pdd.todo

import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import org.elaya.pdd.R
import org.elaya.pdd.databinding.FragmentTodoEditBinding
import org.elaya.pdd.project.Project
import org.elaya.pdd.settings.Globals
import org.elaya.pdd.tools.fragments.FragmentBase

class ToDoEditFragment : FragmentBase(),AdapterView.OnItemSelectedListener {
    private var binding:FragmentTodoEditBinding?=null
    private var project:Project?=null
    private var todo:Todo?=null;
    private var projectAdapter:ArrayAdapter<Project>?=null;
    private var selectedProject:Project?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lArguments=arguments
        if(lArguments != null) {
            project = lArguments.get(P_PROJECT) as Project?
            todo=lArguments.get(P_TODO) as Todo?

        }
    }

    @MenuRes
    override fun getMenuId():Int
    {

            return R.menu.menu_todo_edit;

    }



    override fun onOptionsItemSelected(pItem: MenuItem): Boolean {

        if(pItem.itemId==R.id.delete){
               if(todo != null){
                   val lDb=Globals.db;
                   val lTodo=todo;
                   if(lTodo != null && lDb != null){
                       lDb.deleteTodo(lTodo.id)
                   }
               }
               goBack()
        }
        return super.onOptionsItemSelected(pItem);

    }

    private fun getIndexOf(pInfo:List<Project>,pProjectId:Int):Int
    {
        var lCnt:Int=0;
        pInfo.forEach(){
            if(it.id==pProjectId){
                return lCnt;
            }
            lCnt++;
        }
        return -1;
    }


    override fun onCreateView(
        pInflater: LayoutInflater,
        pContainer: ViewGroup?,
        pSavedInstanceState: Bundle?
    ): View {
        val lBinding= FragmentTodoEditBinding.inflate(pInflater,pContainer,false)

        binding=lBinding
        val lProject=project
        val lTodo=todo;
        var lProjectId:Int=0;
        if(lTodo != null) {
            lBinding.titleInput.setText(lTodo.title)
            lBinding.descriptionInput.setText(lTodo.description)
            lBinding.status.setSelection(lTodo.status)
            lProjectId=lTodo.projectId
        } else if(lProject != null){
            lProjectId=lProject.id

        }
        val lDb=Globals.db;
        val lActivity=activity;
        if(lDb != null && lActivity != null) {
            val lList=lDb.getProjects();
            projectAdapter = ArrayAdapter<Project>(lActivity,android.R.layout.simple_list_item_1,lList);

            lBinding.project.adapter=projectAdapter;
            lBinding.project.onItemSelectedListener=this;
            lBinding.project.setSelection(getIndexOf(lList,lProjectId))

        }
        lBinding.titleInput.requestFocus()
        lBinding.saveButton.setOnClickListener(this::saveTodo)
        lBinding.cancelButton.setOnClickListener(this::cancelPressed)
        return lBinding.root
    }

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun afterMenu(pMenu: Menu) {
        super.afterMenu(pMenu)

        val lItem=pMenu.findItem(R.id.delete);
        lItem?.isVisible = todo != null

    }

    private fun cancelPressed(pView:View){
        goBack();
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        selectedProject=parent?.getItemAtPosition(position) as Project
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun saveTodo(pView:View){
        val lBinding=binding
        val lDs=Globals.db
        if(lBinding != null && lDs != null) {
            val lTodo = todo;
            val lTitle = lBinding.titleInput.text.toString()
            val lDescription = lBinding.descriptionInput.text.toString()
            val lStatus=lBinding.status.selectedItemPosition;
            val lProject = selectedProject
            if (lProject != null) {
               if (lTodo == null) {
                   lDs.addTodo(lProject.id,lStatus, lTitle, lDescription)
                } else {
                    lDs.editTodo(lTodo.id,lProject.id,lStatus, lTitle, lDescription)
                }
            }
        }
        goBack()
    }

    companion object {
        private const val P_PROJECT = "project"
        private const val P_TODO="todo"

        fun newInstance(pProject:Project):Fragment {
            val lFragment=ToDoEditFragment()
            val lArguments=Bundle()
            lArguments.putParcelable(P_PROJECT,pProject)
            lFragment.arguments=lArguments
            return lFragment
        }

        fun newInstance(pTodo:Todo):Fragment
        {
            val lFragment=ToDoEditFragment()
            val lArguments=Bundle()
            lArguments.putParcelable(P_TODO,pTodo)
            lFragment.arguments=lArguments
            return lFragment
        }

    }
}