package ng.max.champion.shared.data.managers

import android.content.Context

class SharedPreferenceManager(private val context: Context) {


    private fun getSharedPreference() = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    private fun getEditor() = getSharedPreference().edit()

    fun saveString(key: String, value: String) = getEditor().putString(key, value).commit()

    fun getString(key: String, default: String? = null): String? = getSharedPreference().getString(key, default)

    fun saveBoolean(key: String, value: Boolean) = getEditor().putBoolean(key, value).commit()

    fun getBoolean(key: String): Boolean = getSharedPreference().getBoolean(key,false)

    fun saveNumber(key: String, default: Float = 0.0f): Float = getSharedPreference().getFloat(key, default)

    fun getNumber(key: String, default: Float = 0.0f): Float = getSharedPreference().getFloat(key, default)

    fun removeValue(key: String) = getEditor().remove(key).commit()

    companion object {
        private const val SHARED_PREF_NAME = "ng.max.champion.shared+preference+name"
    }
}