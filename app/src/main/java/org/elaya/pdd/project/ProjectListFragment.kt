package org.elaya.pdd.project

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.viewpager2.widget.ViewPager2
import org.elaya.pdd.R
import org.elaya.pdd.databinding.FragmentProcesListBinding
import org.elaya.pdd.settings.Globals
import org.elaya.pdd.tools.fragments.FragmentBase


/**
 * Display the projects
 * Use the [ProjectListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectListFragment : FragmentBase() {
    private var binding:FragmentProcesListBinding?=null
    private var projectHandler:ProjectListViewHandler?=null

    private var projectPagerAdapter:ProjectPageAdapter?=null
    private var currentSelectedProject:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.setFragmentResultListener(KEY_TODO_LIST_NAVIGATION,this) {
                _, pBundle-> todoListNavigation(pBundle)
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
        val lProjectPageAdapter=ProjectPageAdapter(this)
        projectPagerAdapter=lProjectPageAdapter
        lBinding.todoListPager.adapter=lProjectPageAdapter
        lBinding.todoListPager.registerOnPageChangeCallback(
            object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(pPosition: Int) {
                    super.onPageSelected(pPosition)
                    todoListPageSelected(pPosition )
                }
            }
        )

        return lBinding.root
    }

    private fun setProjectColor(pIndex:Int,pSelected:Boolean)
    {
        val lBinding=binding
        if(lBinding != null) {
            val lView = lBinding.projectList.getChildAt(pIndex) as LinearLayout
            lView.setBackgroundColor(if(pSelected){ getColor(R.color.color_selected)} else {Color.TRANSPARENT})
            val lTextView = lView.getChildAt(0) as TextView
            lTextView.setTextColor(getColor(if(pSelected){R.color.color_text_selected} else {R.color.color_text_not_selected}))
        }
    }

    private fun todoListPageSelected(pPosition:Int)
    {
        val lBinding=binding
        if(lBinding != null) {
            if (currentSelectedProject >=0 && currentSelectedProject<lBinding.projectList.childCount) {
                setProjectColor(currentSelectedProject,false)
            }
            val lSelected=pPosition-1
            if(lSelected>=0 && lSelected<=lBinding.projectList.childCount){
                setProjectColor(lSelected,true)
            }
            currentSelectedProject=lSelected
        }

    }

    private fun todoListNavigation(@NonNull pBundle:Bundle){
        val lDirection=pBundle.getInt(PAR_DIRECTION)
        val lBinding=binding
        if(lBinding != null){
            lBinding.todoListPager.currentItem += lDirection
        }


    }

    private fun clickItem(pProject:Project)
    {
        val lProjectPageAdapter=projectPagerAdapter;
        if(lProjectPageAdapter !=null) {
            val lNo = lProjectPageAdapter.getPosFromProject(pProject);
            if (lNo >= 0) {
                val lBinding = binding;
                if (lBinding != null) {
                    lBinding.todoListPager.currentItem = lNo+1;
                }
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

            if( lList.size != 0) {
                projectPagerAdapter?.refreshProjectList()
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

    override fun onFragmentResult(pRequestKey: String, pResult: Bundle) {
           setupProjectList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }

    companion object {
        const val KEY_TODO_LIST_NAVIGATION="todo_list_navigation"
        const val PAR_DIRECTION="direction"
        @JvmStatic
        fun newInstance() = ProjectListFragment()
    }
}