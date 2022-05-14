package org.elaya.pdd.project

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.viewpager2.widget.ViewPager2
import org.elaya.pdd.R
import org.elaya.pdd.databinding.FragmentProcesListBinding
import org.elaya.pdd.tools.fragments.FragmentBase


/**
 * Display the projects
 * Use the [ProjectListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectListFragment : FragmentBase() {
    private var binding:FragmentProcesListBinding?=null
    private var projectHandler:ProjectListViewHandler?=null
    private var currentProject:Project?=null
    private var todoListAdapter:TodoListAdapter?=null
    private var currentSelectedProject:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(FL_EDIT_PROJECT,this::onEditProjectResult)
    }

    private fun onEditProjectResult(pRequestKey: String, pResult: Bundle) {
        Log.d("TODO","onEditProjectResult")
        refreshCurrent()
        setupProjectList()
    }

    private fun editProject(pView:View)
    {
        val lCurrentProject=currentProject
        if(lCurrentProject != null){
            startDialogFragment("projectEdit", FL_EDIT_PROJECT) {
                ProjectEditFragment.newInstance(lCurrentProject.id,lCurrentProject.name,lCurrentProject.isActive)
            }
        }
    }
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
        lBinding.projectEdit.setOnClickListener(this::editProject)
        val lTodoListAdapter=TodoListAdapter(this)
        todoListAdapter=lTodoListAdapter
        lBinding.todoListPager.adapter=lTodoListAdapter

        lBinding.todoListPager.registerOnPageChangeCallback(
            object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(pPosition: Int) {
                    super.onPageSelected(pPosition)
                    todoListPageSelected(pPosition )
                }
            }
        )
        lBinding.goFirst.setOnClickListener{

            lBinding.todoListPager.currentItem=0;
        }
        lBinding.nextTodo.setOnClickListener {
            lBinding.todoListPager.currentItem +=1;
        }
        lBinding.previousTodo.setOnClickListener {
            lBinding.todoListPager.currentItem -=1;
        }
        return lBinding.root
    }

    private fun setProjectColor(pIndex:Int,pSelected:Boolean)
    {
        val lBinding=binding
        if(lBinding != null) {
            val lView = lBinding.projectList.getChildAt(pIndex) as LinearLayout
            lView.setBackgroundColor(if(pSelected){ getColor(R.color.color_selected)} else {Color.TRANSPARENT})
            val lTextView = lView.getChildAt(0) as TextView
            lTextView.setTextColor(getColor(if(pSelected){R.color.color_text_selected}
                                            else {R.color.gen_text_color }))
        }
    }

    private fun todoListPageSelected(pPosition:Int)
    {
        val lBinding=binding
        if(lBinding != null) {
            if (currentSelectedProject >= 0 && currentSelectedProject < lBinding.projectList.childCount) {
                setProjectColor(currentSelectedProject, false)
            }
            if (pPosition > 0) {
                currentProject = todoListAdapter?.getProjectByPos(pPosition - 1)
            } else {
                currentProject = null
            }
            if (pPosition >= 0 && pPosition <= lBinding.projectList.childCount) {
                setProjectColor(pPosition, true)
            }
            currentSelectedProject = pPosition
            lBinding.previousTodo.visibility = if (pPosition > 0) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
            lBinding.projectEdit.visibility = if (currentProject != null) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
            val lLast = getDB()?.getLastProjectId()

            lBinding.nextTodo.visibility = if (lLast != null && pPosition != lLast) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

    }


    private fun clickItem(pProject:Project)
    {
        val lProjectPageAdapter=todoListAdapter
        if(lProjectPageAdapter !=null) {
            val lNo = lProjectPageAdapter.getPosFromProject(pProject)
            if (lNo >= 0) {
                val lBinding = binding
                if (lBinding != null) {
                    lBinding.todoListPager.currentItem = lNo+1
                }
            } else if(pProject.id==-1){
                val lBinding = binding
                if (lBinding != null) {
                    lBinding.todoListPager.currentItem = 0
                }

            }
        }
    }

    private fun newProject(pView:View){
        val lActivity=activity
        if(lActivity != null) {

            startDialogFragment("projectEdit", FL_EDIT_PROJECT){
                ProjectEditFragment.newInstance()
            }
        }
    }


    private fun setupProjectList()
    {
        val lDb= getDB()
        val lBinding = binding

        if(lDb != null && lBinding != null) {
            val lList = lDb.getProjects()
            lList.add(0,Project(Project.ID_ALL_PROJECTS,resources.getString(R.string.project_all_todo),true))
            projectHandler?.makeList(layoutInflater, lList)

            if( lList.size != 0) {
                val lProjectPageAdapter=todoListAdapter
                if(lProjectPageAdapter !=null) {
                    lProjectPageAdapter.refreshProjectList()
                    val lPager=binding?.todoListPager
                    if(lPager != null) {
                        lProjectPageAdapter.notifyDataSetChanged()
                    }
                }
                lBinding.todoListPager.visibility=View.VISIBLE
            } else {
                lBinding.todoListPager.visibility=View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupProjectList()
    }

    private fun refreshCurrent()
    {
        val lBinding=binding
        if(lBinding != null) {
             val lPage = childFragmentManager.findFragmentByTag("f" + lBinding.todoListPager.currentItem)

            if(lPage  is TodoListPagerFragment){
                lPage.refreshData()
            } else {
                Log.d("TODO","Fragment is null")
            }

        }
    }

    override fun onFragmentResult(pRequestKey: String, pResult: Bundle) {
        if(pRequestKey == ProjectListFragment.javaClass.name) {
            refreshCurrent()
        }
        setupProjectList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }

    companion object {

        const val FL_EDIT_PROJECT="EDIT_PROJECT"
        @JvmStatic
        fun newInstance() = ProjectListFragment()
    }
}