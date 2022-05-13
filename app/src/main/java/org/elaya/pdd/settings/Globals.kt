package org.elaya.pdd.settings

import android.content.Context
import org.elaya.pdd.db.DataSource

object Globals {
    private var vDb:DataSource?=null

    @Synchronized
     fun getDB(pContext:Context):DataSource{
        val lDb:DataSource?=vDb;
        lateinit var lReturn:DataSource;
        if(lDb != null){
            lReturn=lDb
        } else {
            lReturn=DataSource(pContext);
            vDb=lReturn
        }

        return lReturn;
    }

}