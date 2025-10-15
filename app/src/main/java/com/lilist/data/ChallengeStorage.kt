package com.lilist.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChallengeStorage(private val context: Context) {
    private val prefs = context.getSharedPreferences("challenge_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveChallenges(challenges: List<Challenge>) {
        val json = gson.toJson(challenges)
        prefs.edit().putString("challenges", json).apply()
    }

    fun loadChallenges(): List<Challenge> {
        val json = prefs.getString("challenges", null)
        return try {
            if (json != null) {
                gson.fromJson(json, object : TypeToken<List<Challenge>>() {}.type)
            } else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
