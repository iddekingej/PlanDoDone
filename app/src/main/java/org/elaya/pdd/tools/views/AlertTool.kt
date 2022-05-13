package org.elaya.pdd.tools.views


import androidx.annotation.StringRes
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlin.jvm.JvmOverloads
import org.elaya.pdd.R

object AlertTool {
    /**
     *
     * @param pFragment   Context
     * @param pTitle     Title of alert
     * @param pButton    Positive button label text
     * @param pOnClick   OnClick event when pressed on button
     * @param pHasActivityFinish Finish activity when canceling alert
     * @return AlertDialogBuilder
     */
    private fun makeAlert(
        pFragment: Fragment,
        @StringRes pTitle: Int,
        @StringRes pButton: Int,
        pOnClick: DialogInterface.OnClickListener,
        pHasActivityFinish: Boolean
    ): AlertDialog.Builder? {
        val lContext = pFragment.context
        if(lContext != null) {
            val lBuilder = AlertDialog.Builder(lContext)
            lBuilder.setTitle(pTitle)
            lBuilder.setPositiveButton(pButton, pOnClick)
            if (pHasActivityFinish) {
                lBuilder.setOnCancelListener { dialog: DialogInterface? -> pFragment.parentFragmentManager.popBackStack() }
            }
            return lBuilder
        }
        return null
    }

    private fun show(pFragment: Fragment, pBuilder: AlertDialog.Builder) {
        val lActivity=pFragment.activity
        if(lActivity != null && !lActivity.isFinishing) {
            val lAlertDialog = pBuilder.create()
            lAlertDialog.setCanceledOnTouchOutside(false)
            lAlertDialog.show()
        }
    }

    @JvmOverloads
    fun simpleMessage(
        pFragment: Fragment,
        @StringRes pTitle: Int,
        pMessage: String?,
        @StringRes pButton: Int,
        pOnClick: DialogInterface.OnClickListener,
        pHasActivityFinish: Boolean = false
    ) {

            val lBuilder = makeAlert(pFragment, pTitle, pButton, pOnClick, pHasActivityFinish)
            if(lBuilder != null){
                lBuilder.setMessage(pMessage)
                show(pFragment, lBuilder)
            }
    }

    fun simpleMessage(
        pFragment: Fragment,
        @StringRes pTitle: Int,
        @StringRes pMessage: Int,
        @StringRes pButton: Int,
        pOnClick: DialogInterface.OnClickListener
    ) {
            val lBuilder = makeAlert(pFragment, pTitle, pButton, pOnClick, false)
            if(lBuilder != null) {
                lBuilder.setMessage(pMessage)
                show(pFragment, lBuilder)
            }

    }

    fun confirm(
        pFragment: Fragment,
        @StringRes pTitle: Int,
        @StringRes pMessage: Int,
        pOnClick: DialogInterface.OnClickListener?
    ) {
        val lContext = pFragment.context
        if(lContext != null) {
            val lBuilder = AlertDialog.Builder(lContext)
            lBuilder.setTitle(pTitle)
            lBuilder.setMessage(pMessage)
            lBuilder.setPositiveButton(R.string.label_gen_yes, pOnClick)
            lBuilder.setNegativeButton(R.string.label_gen_no, null)
            show(pFragment, lBuilder)
        }
    }
}