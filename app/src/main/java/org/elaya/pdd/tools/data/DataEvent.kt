package org.elaya.pdd.tools.data

interface DataEvent<U> {
    fun event(pItem:U)
}