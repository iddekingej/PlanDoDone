package org.elaya.pdd.project

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.elaya.pdd.settings.Globals
import java.util.*


class TodoListAdapter(pFragment: Fragment) : FragmentStateAdapter(pFragment) {
    private var projects:LinkedList<Project>?=null
    private var context:Context?=null

    init {
        var lContext=pFragment.context
        if(lContext != null){
            context=lContext;
            val lProjects=Globals.getDB(lContext).getProjects();

            projects=lProjects
        }
    }


    override fun getItemId(pPosition: Int): Long {
        val lProjects=projects;
        if(lProjects != null && pPosition>0){
            return lProjects[pPosition-1].id.toLong();
        }
        return -1;
    }


    fun getProjectById(pProjectId:Int):Int{
        val lProjects=projects
        if(lProjects != null) {
            for((lCnt, lProject) in lProjects.withIndex()){

                if(lProject.id==pProjectId){
                    return lCnt
                }
            }
        }

        return -1
    }



    fun getProjectByPos(pPos:Int):Project?{


        val lProjects=projects
        if(lProjects != null){
            if(pPos>=0 && pPos< lProjects.size) {
                return lProjects[pPos]
            }
        }

        return null
    }



    fun getPosFromProject(pProject:Project):Int{
        val lProjects=projects
        if(lProjects != null) {
            for((lCnt, lProject) in lProjects.withIndex()){
                if(lProject.id==pProject.id){
                    return lCnt
                }
            }
        }
        return -1
    }



    override fun getItemCount(): Int {
        val lProjects=projects
        if(lProjects != null){
            return lProjects.size+1
        }
        return 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshProjectList()
    {
        val lContext=context;
        if(lContext != null) {
            projects = Globals.getDB(lContext).getProjects()
        }
        notifyDataSetChanged()
    }

    override fun createFragment(pPosition: Int): Fragment {
        if(pPosition>0){
            val lProjects=projects
            if(lProjects != null) {
                if (pPosition - 1 < lProjects.size) {
                    return TodoListPagerFragment.start(lProjects[pPosition - 1].id,pPosition==lProjects.size)
                }
            }
        }
        return TodoListPagerFragment.start()
    }
}