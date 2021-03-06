package org.elaya.pdd.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.elaya.pdd.project.Project
import org.elaya.pdd.todo.Todo
import java.util.*

class DataSource(pContext:Context) {
    private val db:SQLiteDatabase

    init{
        val lOpenHelper=OpenHelper(pContext)
        db=lOpenHelper.readableDatabase
    }


    private fun hasResult(pQuery:String, pParameters:Array<String>? ):Boolean{
        db.rawQuery(pQuery,pParameters).use{
            return it.moveToFirst()
        }
    }

    private inline fun <T>fetchRow(pQuery:String,pBody:(it:Cursor)->T):T? {
        return fetchRow(pQuery, emptyArray(),pBody)
    }

        private inline fun <T>fetchRow(pQuery:String,pArgs:Array<String> ,pBody:(it:Cursor)->T):T?{
            var it:Cursor?=null
            try {
                it=db.rawQuery(pQuery,pArgs)
                if(it.moveToNext()) {
                    return pBody(it)
                }
        }finally {
            it?.close()
            }
        return null
        }

    private fun fetchSingleFieldInt(pQuery:String):Int?{
        val lIt=db.rawQuery(pQuery,null)
        if(lIt.moveToNext()){
            return lIt.getInt(0)
        }
        lIt.close()
        return null
    }

    private inline fun <T>fetchRows(pQuery:String,pArgs:Array<String> ,pBody:(it:Cursor)->T):LinkedList<T>{
        var it:Cursor?=null
        val lList=LinkedList<T>()
        try {
            it=db.rawQuery(pQuery,pArgs)
            while(it.moveToNext()){
                val lData=pBody(it)
                lList.add(lData)
            }
        }finally {
            it?.close()
        }
        return lList
    }

    private inline fun <T>fetchRows(pQuery:String,pBody:(it:Cursor)->T):LinkedList<T>{
            return fetchRows(pQuery, emptyArray(),pBody)
    }

        fun saveProject(pId:Int,pName:String,pIsActive:Boolean):Int{
        val lValue=ContentValues()
        lValue.put("name",pName)
        lValue.put("enabled",if(pIsActive)1 else 0)
        var lId=pId;
        if(pId==-1){
            lId= db.insert("project",null,lValue).toInt()
        } else {
            db.update("project",lValue,"id=?",arrayOf(pId.toString()))
        }
        return lId;
    }

    fun getProject(pProjectId:Int):Project?{

        return fetchRow("select id,name,enabled from project where id=?",arrayOf(pProjectId.toString()) ){
             return Project(it.getInt(0),it.getString(1),it.getInt(2)==1)
        }

    }

    fun getProjects():LinkedList<Project>
    {

        return fetchRows("select id,name,enabled from project order by name"){
            Project(it.getInt(0), it.getString(1), it.getInt(2) == 1)
        }

    }

    fun projectHasTodo(pProjectId:Int):Boolean
    {
        return hasResult("select 1 as dm from "+Todo.TABLE_NAME+" where id_project=? limit 1",arrayOf(pProjectId.toString()) )
    }


    fun getLastProjectId():Int?{
        return fetchSingleFieldInt("select "+Project.F_ID+" from "+Project.TABLE_NAME+"  order by "+Project.F_NAME+" desc limit 1")
    }

    fun hasProjects():Boolean
    {
        return hasResult("select 1 as dm from "+Project.TABLE_NAME+" limit 1",null);
    }

    fun addTodo(pProjectId:Int,pStatus:Int,pTitle:String, pDescription:String){
        val lValues=ContentValues()
        lValues.put(Todo.F_TITLE,pTitle)
        lValues.put(Todo.F_PROJECT_ID,pProjectId)
        lValues.put(Todo.F_DESCRIPTION,pDescription)
        lValues.put(Todo.F_STATUS,pStatus)
        db.insert(Todo.TABLE_NAME,null,lValues)
    }

    fun editTodo(pId:Int,pProjectId:Int,pStatus:Int,pTitle:String,pDescription: String){
        val lValues=ContentValues()
        lValues.put(Todo.F_TITLE,pTitle)
        lValues.put(Todo.F_DESCRIPTION,pDescription)
        lValues.put(Todo.F_PROJECT_ID,pProjectId)
        lValues.put(Todo.F_STATUS,pStatus)
        db.update(Todo.TABLE_NAME,lValues,"id=?", arrayOf(pId.toString()))
    }

    fun getTodos(pProjectId: Int):LinkedList<Todo>
    {
        return fetchRows("select id,title,description,status from "+Todo.TABLE_NAME+" where id_project=?",arrayOf(pProjectId.toString())){
            Todo(it.getInt(0),pProjectId,it.getString(1),it.getString(2),it.getInt(3))
        }
    }

    fun getActiveTodos():LinkedList<Todo>
    {
        return fetchRows("select id,title,description,status,id_project from "+Todo.TABLE_NAME+" where status ="+Todo.STATUS_STARTED){
            Todo(it.getInt(0),it.getInt(4),it.getString(1),it.getString(2),it.getInt(3))
        }
    }


    fun deleteTodo(pTodoId: Int)
    {
        db.delete(Todo.TABLE_NAME,Todo.F_ID+"=?",arrayOf(pTodoId.toString()))
    }

    fun deleteProject(pProjectId: Int)
    {
        db.delete(Project.TABLE_NAME,Project.F_ID+"=?",arrayOf(pProjectId.toString()))
    }



}