package com.gmail.pavlovsv93.whattoseetoday

import android.content.SearchRecentSuggestionsProvider

class BasSuggestionProvider : SearchRecentSuggestionsProvider(){
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.gmail.pavlovsv93.whattoseetoday.BasSuggestionProvider"
        const val MODE: Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }
}