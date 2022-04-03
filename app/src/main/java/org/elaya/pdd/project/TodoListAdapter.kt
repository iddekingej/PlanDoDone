package org.elaya.pdd.project

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.elaya.pdd.settings.Globals
import java.util.*


class TodoListAdapter(pFragment:Fragment): FragmentStateAdapter(pFragment) {
    private var projects:LinkedList<Project>?=null

    init {
        projects= Globals.db?.getProjects()
    }

    override fun getItemId(pPosition: Int): Long {
        return pPosition.toLong();
    }
    fun getProjectByPos(pPos:Int):Project?{
        val lProjects=projects;
        if(lProjects != null){
            if(pPos>=0 && pPos< lProjects.size) {
                return lProjects[pPos];
            }
        }
        return null;
    }



    fun getPosFromProject(pProject:Project):Int{
        val lProjects=projects;
        if(lProjects != null) {
            for (lCnt in 0..lProjects.size) {
                if(lProjects[lCnt].id==pProject.id){
                    return lCnt;
                }
            }
        }
        return -1;
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
        Log.d("TODO","Refresh adapter");
        projects= Globals.db?.getProjects()
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