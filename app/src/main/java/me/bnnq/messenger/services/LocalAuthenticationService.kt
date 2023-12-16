package me.bnnq.messenger.services

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.bnnq.messenger.BuildConfig
import me.bnnq.messenger.models.Result
import me.bnnq.messenger.models.User
import me.bnnq.messenger.services.abstractions.IAuthenticationService
import java.time.Duration
import java.time.Instant

class LocalAuthenticationService(private val context: Context) : IAuthenticationService {
    private val storageName = "user_prefs"
    private var users : List<User> = listOf()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        val storage = context.getSharedPreferences(storageName, Context.MODE_PRIVATE)
        val userListSerialized = storage.getString(::users.name, "[]")
        if (userListSerialized == null) {
            users = listOf()
            return
        }

        users = Json.decodeFromString(userListSerialized)
        val timeDifference = Duration.between(Instant.now(), Instant.ofEpochMilli(storage.getLong("lastAuthenticationTime", 0)))
        val authenticationExpiresAt = if (BuildConfig.DEBUG) 2 else 10080
        if (storage.contains(::currentUser.name) && timeDifference.toMinutes() < authenticationExpiresAt) {
            currentUser = Json.decodeFromString(storage.getString(::currentUser.name, "")!!)
            login(currentUser!!.username, currentUser!!.password, trackAuthenticationTime = false)
        }

    }

    private fun saveUsers() {
        val storage = context.getSharedPreferences(storageName, Context.MODE_PRIVATE)
        with (storage.edit()) {
            putString(::users.name, Json.encodeToString(users))
            apply()
        }
    }

    private var isCurrentUserAuthenticated : Boolean = false

    override fun login(username: String, password: String, trackAuthenticationTime : Boolean): Result {
        currentUser = users.find { it.username == username && it.password == password }
        if (currentUser != null) {
            isCurrentUserAuthenticated = true

            val storage = context.getSharedPreferences(storageName, Context.MODE_PRIVATE)
            with (storage.edit()) {
                putString(::currentUser.name, Json.encodeToString(currentUser))
                putLong("lastAuthenticationTime", Instant.now().toEpochMilli())

                apply()
            }

            return Result()
        }

        return Result("Incorrect username or password")
    }

    override fun register(username: String, password: String): Result {
        if (users.any { it.username == username }) {
            return Result("User with such name already exists")
        }

        users = users + User(0, username, password)
        saveUsers()

        return Result()
    }

    private var currentUser : User? = null
    override fun getCurrentUser(): User? {
        return currentUser
    }

    override fun isAuthenticated(): Boolean {
        return isCurrentUserAuthenticated
    }

}