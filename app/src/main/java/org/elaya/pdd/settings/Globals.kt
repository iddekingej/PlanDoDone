package org.elaya.pdd.settings

import android.content.Context
import org.elaya.pdd.db.DataSource

object Globals {
    private var _db:DataSource?=null
    val db: DataSource?
    get()=_db

    fun applicationSetup(pContext:Context){
        _db=DataSource(pContext)
    }
}