package org.elaya.pdd.settings

import android.content.Context
import org.elaya.pdd.db.DataSource

object Globals {
    private var vDb:DataSource?=null
    val db: DataSource?
    get()=vDb

    fun applicationSetup(pContext:Context){
        vDb=DataSource(pContext)
    }
}