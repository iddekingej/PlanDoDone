package org.elaya.pdd.todo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import org.elaya.pdd.R
import org.elaya.pdd.databinding.FragmentTodoEditBinding
import org.elaya.pdd.project.Project
import org.elaya.pdd.settings.Globals
import org.elaya.pdd.tools.data.ArraySpinnerAdapter
import org.elaya.pdd.tools.fragments.FragmentBase

class ToDoEditFragment : FragmentBase() {
    private var binding: FragmentTodoEditBinding? = null
    private var project: Project? = null
    private var todo: Todo? = null
    private var projectAdapter: ArraySpinnerAdapter<Project>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lArguments = arguments
        if (lArguments != null) {

            todo = lArguments.get(P_TODO) as Todo?
            val lTodo = todo
            project = if (lTodo != null) {
                val lProjectId = lTodo.projectId
                Globals.db?.getProject(lProjectId)
            } else {
                lArguments.get(P_PROJECT) as Project?
            }
        }
    }

    @MenuRes
    override fun getMenuId(): Int {

        return R.menu.menu_todo_edit

    }


    override fun onOptionsItemSelected(pItem: MenuItem): Boolean {

        if (pItem.itemId == R.id.delete) {
            if (todo != null) {
                val lDb = Globals.db
                val lTodo = todo
                if (lTodo != null && lDb != null) {
                    lDb.deleteTodo(lTodo.id)
                }
            }
            goBack()
        }
        return super.onOptionsItemSelected(pItem)

    }

    private fun getIndexOf(pInfo: List<Project>, pProjectId: Int): Int {
        var lCnt = 0
        pInfo.forEach {
            if (it.id == pProjectId) {
                return lCnt
            }
            lCnt++
        }
        return -1
    }


    override fun onCreateView(
        pInflater: LayoutInflater,
        pContainer: ViewGroup?,
        pSavedInstanceState: Bundle?
    ): View {
        val lBinding = FragmentTodoEditBinding.inflate(pInflater, pContainer, false)

        binding = lBinding
        val lProject = project
        val lTodo = todo

        if (lTodo != null) {
            lBinding.titleInput.setText(lTodo.title)
            lBinding.descriptionInput.text = lTodo.description
            lBinding.status.setSelection(lTodo.status)
            lBinding.saveButton.isEnabled = lTodo.title.isNotEmpty()
        } else {
            lBinding.saveButton.isEnabled = false
        }
        val lDb = Globals.db
        val lContext = context
        if (lDb != null && lContext != null && lProject != null) {
            val lProjects = lDb.getProjects()


            projectAdapter = ArraySpinnerAdapter(
                lContext,
                android.R.layout.simple_spinner_dropdown_item,
                lProjects
            )

            lBinding.projectSelection.adapter = projectAdapter
            lBinding.projectSelection.setSelection(getIndexOf(lProjects, lProject.id))
        }
        lBinding.titleInput.requestFocus()
        lBinding.titleInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(pText: Editable?) {
                lBinding.saveButton.isEnabled = pText != null && pText.isNotEmpty()
            }

        })
        lBinding.saveButton.setOnClickListener(this::saveTodo)
        lBinding.cancelButton.setOnClickListener(this::cancelPressed)

        return lBinding.root
    }

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun afterMenu(pMenu: Menu) {
        super.afterMenu(pMenu)

        val lItem = pMenu.findItem(R.id.delete)
        lItem?.isVisible = todo != null

    }

    private fun cancelPressed(pView: View) {
        goBack()
    }


    private fun saveTodo(pView: View) {
        val lBinding = binding
        val lDs = Globals.db
        if (lBinding != null && lDs != null) {
            val lTodo = todo
            val lTitle = lBinding.titleInput.text.toString()
            val lDescription = lBinding.descriptionInput.text.toString()
            val lStatus = lBinding.status.selectedItemPosition
            val lProjectSelection = lBinding.projectSelection.selectedItem

            if (lProjectSelection is Project) {
                if (lTodo == null) {
                    lDs.addTodo(lProjectSelection.id, lStatus, lTitle, lDescription)
                } else {
                    lDs.editTodo(lTodo.id, lProjectSelection.id, lStatus, lTitle, lDescription)
                }

            }
            goBack()
        }

    }

    companion object {
        private const val P_PROJECT = "project"
        private const val P_TODO = "todo"

        fun newInstance(pProject: Project): Fragment {
            val lFragment = ToDoEditFragment()
            val lArguments = Bundle()
            lArguments.putParcelable(P_PROJECT, pProject)
            lFragment.arguments = lArguments
            return lFragment
        }

        fun newInstance(pTodo: Todo): Fragment {
            val lFragment = ToDoEditFragment()
            val lArguments = Bundle()
            lArguments.putParcelable(P_TODO, pTodo)
            lFragment.arguments = lArguments
            return lFragment
        }

    }
}