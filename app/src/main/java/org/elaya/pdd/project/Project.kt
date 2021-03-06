package org.elaya.pdd.project

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import org.elaya.pdd.R
import org.elaya.pdd.tools.data.Data

class Project(_id:Int,private var _name:String,private var _isActive:Boolean,): Data(_id),Parcelable {


    constructor(parcel: Parcel) : this(parcel.readInt(),parcel.readString() ?: "",parcel.readByte() != 0.toByte()){

    }


    fun setInfo(pName:String,pIsActive: Boolean){
        _name=pName
        _isActive=pIsActive
    }

    val name:String       get()=_name
    val isActive:Boolean  get()=_isActive


    override fun  toString():String
    {
        return _name;
    }

    override fun describeContents(): Int {
          return  0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if(dest != null){
            dest.writeInt(_id)
            dest.writeString(_name)
            dest.writeInt(if(_isActive)1 else 0)

        }
    }


    companion object CREATOR : Parcelable.Creator<Project> {
        override fun createFromParcel(parcel: Parcel): Project {
            return Project(parcel)
        }

        override fun newArray(size: Int): Array<Project?> {
            return arrayOfNulls(size)
        }

        fun makeAllProject(pContext:Context):Project
        {
            return Project(ID_ALL_PROJECTS,pContext.getString(R.string.project_all_todo),true);
        }

        const val TABLE_NAME="project"
        const val F_ID="id"
        const val F_NAME="name"
        const val ID_ALL_PROJECTS=-1;

    }
}