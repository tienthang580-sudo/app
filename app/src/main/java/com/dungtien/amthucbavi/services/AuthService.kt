package com.dungtien.amthucbavi.services

import android.content.Context
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.models.User

class AuthService private constructor(context: Context) {

    private val prefs = context.getSharedPreferences("dung_tien_auth", Context.MODE_PRIVATE)
    private val dbHelper = DatabaseHelper.getInstance(context)

    var currentUser: User? = null
        private set

    companion object {
        private const val KEY_USER_ID = "signed_in_user_id"

        @Volatile
        private var INSTANCE: AuthService? = null

        fun getInstance(context: Context): AuthService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthService(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun restoreSession(): User? {
        val id = prefs.getLong(KEY_USER_ID, -1L)
        if (id == -1L) return null
        val user = dbHelper.getUser(id)
        if (user == null) {
            prefs.edit().remove(KEY_USER_ID).apply()
            return null
        }
        currentUser = user
        return user
    }

    fun login(email: String, pass: String): User? {
        val user = dbHelper.findUser(email, pass) ?: return null
        currentUser = user
        prefs.edit().putLong(KEY_USER_ID, user.id).apply()
        return user
    }

    fun register(fullName: String, email: String, phone: String, pass: String): User {
        val id = dbHelper.addUser(fullName, email, phone, pass)
        val user = dbHelper.getUser(id) ?: throw IllegalStateException("Cannot read created user.")
        currentUser = user
        prefs.edit().putLong(KEY_USER_ID, id).apply()
        return user
    }

    fun logout() {
        prefs.edit().remove(KEY_USER_ID).apply()
        currentUser = null
    }

    fun updateCurrentUser(user: User) {
        currentUser = user
    }
}
