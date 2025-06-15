package user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserInfo(): Result<User?>
    suspend fun logoutUser(): Result<Unit>
}