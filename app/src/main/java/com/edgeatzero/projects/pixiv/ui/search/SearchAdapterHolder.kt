package com.edgeatzero.projects.pixiv.ui.search

import androidx.recyclerview.widget.ConcatAdapter
import com.edgeatzero.projects.pixiv.database.SearchHistoryEntity
import com.edgeatzero.projects.pixiv.model.Tag

class SearchAdapterHolder {

    private val header by lazy { SearchHistoryHeader() }

    private val history by lazy { SearchHistoryAdapter() }
    private var historyVersion = -1

    private val suggestion by lazy { SearchSuggestionAdapter() }
    private var suggestionVersion = -1

    val adapter: ConcatAdapter by lazy { ConcatAdapter(header, history, suggestion) }

    fun setHeaderListener(block: () -> Unit) {
        header.setListener(block)
    }

    fun setListener(block: (String) -> Unit) {
        history.setListener(block)
        suggestion.setListener(block)
    }

    fun submitHistories(histories: List<SearchHistoryEntity>?, boolean: Boolean = false) {
        history.submitList(histories)
        header.display = boolean && histories.isNullOrEmpty().not()
        if (boolean) {
            historyVersion = -1
            return
        }
        if (historyVersion++ > suggestionVersion && suggestion.itemCount > 0) {
            suggestion.submitList(null)
        }
    }

    fun submitSuggestions(suggestions: List<Tag>?) {
        suggestion.submitList(suggestions)
        if (suggestions == null) {
            suggestionVersion = -1
            return
        }
        if (suggestionVersion++ > historyVersion && history.itemCount > 0) {
            history.submitList(null)
        }
    }

}
