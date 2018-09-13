package net.mieczkowski.yelpbusinessexample.controllers.search

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.toolbar_search.view.*
import net.mieczkowski.yelpbusinessexample.R
import net.mieczkowski.yelpbusinessexample.exts.hideKeyboard

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
class SearchViewHolder(context: Context) {

    val view = LayoutInflater.from(context).inflate(R.layout.toolbar_search, null)

    private val allowChars = charArrayOf('!', '#', '$', '%', '&', '+', ',', '­', '.', '/', ':', '?', '@')
    private var filter: InputFilter = InputFilter { source, start, end, _, _, _ ->
        for (index in start until end) {
            val toCheck = source[index]
            if (allowChars.contains(toCheck))
                return@InputFilter null
            else if (!Character.isLetterOrDigit(toCheck) && !Character.isSpaceChar(toCheck))
                return@InputFilter ""
        }
        null
    }

    init {
        view.editSearch.filters = arrayOf(filter)

        view.imgClear.setOnClickListener {
            setCurrentSearchQuery("")
        }
    }

    fun getSearchQuery(): String = view.editSearch.text.toString().trim()

    fun setCurrentSearchQuery(query: String) {
        view.editSearch.setText(query)
    }

    fun setOnQuerySelected(onTextEntered: (String) -> Unit) {
        view.editSearch.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    view.editSearch.hideKeyboard()
                    onTextEntered(getSearchQuery())
                    true
                }
                else -> false
            }
        }
    }

    fun setOnTextChange(onTextChange: (String) -> Unit) {
        view.editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val text = s.toString().trim()
                view.imgClear.visibility = if(text.isEmpty()) View.INVISIBLE else View.VISIBLE
                onTextChange(text)
            }

        })
    }
}