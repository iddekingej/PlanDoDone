package org.elaya.pdd.project

interface ProjectEditSaveHandler {
    fun saveProject(pId:Int,pName:String,pIsActive:Boolean)
}