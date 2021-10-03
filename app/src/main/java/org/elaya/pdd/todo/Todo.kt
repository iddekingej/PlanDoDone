package org.elaya.pdd.todo

import android.os.Parcel
import android.os.Parcelable

class Todo :Parcelable {

    private var _id:Int
    var projectId:Int
    var title:String
    var description:String
    var status:Int

    val id:Int get()=_id

    constructor(pId:Int,pProjectId: Int,pStatus:Int,pTitle:String,pDescription: String){
        _id=pId
        title=pTitle
        description=pDescription
        projectId=pProjectId
        status=pStatus
    }

    constructor(pSource:Parcel){
        _id=pSource.readInt()
        projectId=pSource.readInt()
        title=pSource.readString()?:""
        description=pSource.readString()?:""
        status=pSource.readInt()
    }

    override fun writeToParcel(pDest: Parcel?, pFlags: Int) {
        if (pDest != null) {
            pDest.writeInt(_id)
            pDest.writeInt(projectId)
            pDest.writeString(title)
            pDest.writeString(description)
            pDest.writeInt(status)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Todo> {
        override fun createFromParcel(pSource: Parcel): Todo {
            return Todo(pSource)
        }

        override fun newArray(pSize: Int): Array<Todo?> {
            return arrayOfNulls(pSize)
        }

        const val TABLE_NAME="todo"
        const val F_ID="id"
        const val F_TITLE="title"
        const val F_PROJECT_ID="id_project"
        const val F_DESCRIPTION="description"
        const val F_STATUS="status"

        const val STATUS_NEW=0
        const val STATUS_STARTED=1
        const val STATUS_FINISHED=2
        const val STATUS_WAITING=3
        const val STATUS_STOPPED=4
    }



    }