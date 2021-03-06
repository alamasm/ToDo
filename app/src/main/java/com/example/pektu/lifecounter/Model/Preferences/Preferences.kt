package com.example.pektu.lifecounter.Model.Preferences

interface Preferences {
    companion object {
        const val INT_IN_PREFS_DEFAULT_VALUE = -1
        const val STRING_IN_PREFS_DEFAULT_VALUE = "-1"
        const val BOOL_IN_PREFS_DEFAULT_VALUE = false
    }

    fun saveInt(tag: String, i: Int)
    fun saveString(tag: String, s: String)
    fun saveBoolean(tag: String, b: Boolean)

    fun getInt(tag: String): Int
    fun getString(tag: String): String
    fun getBoolean(tag: String): Boolean
}