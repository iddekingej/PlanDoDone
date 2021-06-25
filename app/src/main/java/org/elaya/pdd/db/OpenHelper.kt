package org.elaya.pdd.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class OpenHelper(pContext:Context) : SQLiteOpenHelper(pContext,"data",null,1) {



    override fun onUpgrade(pDb: SQLiteDatabase?, pOldVersion: Int, pNewVersion: Int) {
// no upgrade yet
    }

    override fun onCreate(pDb: SQLiteDatabase?) {
        if(pDb != null) {
            pDb.execSQL("create table project(id integer primary key,name varchar,enabled int)")
            pDb.execSQL( "create table todo(id integer primary key,id_project int not null references project(id),title varchar,description varchar,status int,state_date date, end_date date,planned_start_date date, planned_end_data date)")
        }
        //Todo is PDB  is null what then
    }
}