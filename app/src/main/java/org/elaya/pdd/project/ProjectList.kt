package org.elaya.pdd.project

import java.util.LinkedList

class ProjectList {
    private var projects:List<Project> = LinkedList<Project>();

    companion object{
        private var _projects:ProjectList?=null;


         val projects:ProjectList
            get(){
            var lProjects= _projects;
            if(lProjects==null){
                lProjects=ProjectList();
                _projects=lProjects;
            }
            return lProjects;
        }

    }
}