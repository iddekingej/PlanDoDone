package org.elaya.pdd.project

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.elaya.pdd.settings.Globals
import java.util.*


class ProjectPageAdapter(pFragment:Fragment): FragmentStateAdapter(pFragment) {
    private var projects:LinkedList<Project>?=null;

    init {
        projects= Globals.db?.getProjects();
    }

    override fun getItemCount(): Int {
        val lProjects=projects;
        if(lProjects != null){
            return lProjects.size+1
        }
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshProjectList()
    {
        projects= Globals.db?.getProjects();
        notifyDataSetChanged();
    }

    override fun createFragment(pPosition: Int): Fragment {
        if(pPosition>0){
            val lProjects=projects;
            if(lProjects != null) {
                if (pPosition - 1 < lProjects.size) {
                    return TodoListPagerFragment.start(lProjects[pPosition - 1].id);
                }
            }
        }
        return TodoListPagerFragment.start();
    }
}