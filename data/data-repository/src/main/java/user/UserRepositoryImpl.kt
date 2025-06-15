package user

class UserRepositoryImpl : UserRepository {
    override suspend fun getUserInfo(): Result<User?> {
        return Result.success(
            User(
                id = "a1b2c3",
                userName = "John Doe",
                email = "john.mclean@examplepetstore.com"
            )
        )
    }

    override suspend fun logoutUser(): Result<Unit> {
        return Result.success(Unit)
    }
}