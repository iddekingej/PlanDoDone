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

    private fun rawQuery(pQuery:String):Cursor{
        return db.rawQuery(pQuery, emptyArray())
    }

    

    private fun hasResult(pQuery:String):Boolean{
        rawQuery(pQuery).use{
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

        fun saveProject(pId:Int,pName:String,pIsActive:Boolean){
        val lValue=ContentValues()
        lValue.put("name",pName)
        lValue.put("enabled",if(pIsActive)1 else 0)
        if(pId==-1){
            db.insert("project",null,lValue)
        } else {
            db.update("project",lValue,"id=?",arrayOf(pId.toString()))
        }
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
            Todo(it.getInt(0),pProjectId,it.getInt(3),it.getString(1),it.getString(2))
        }
    }

    fun getActiveTodos():LinkedList<Todo>
    {
        return fetchRows("select id,title,description,status,id_project from "+Todo.TABLE_NAME+" where status ="+Todo.STATUS_STARTED){
            Todo(it.getInt(0),it.getInt(4),it.getInt(3),it.getString(1),it.getString(2))
        }
    }


    fun deleteTodo(pTodoId: Int)
    {
        db.delete(Todo.TABLE_NAME,Todo.F_ID+"=?",arrayOf(pTodoId.toString()))
    }

    fun hasProjects():Boolean
    {
        return hasResult("select 1 from project")
    }

}