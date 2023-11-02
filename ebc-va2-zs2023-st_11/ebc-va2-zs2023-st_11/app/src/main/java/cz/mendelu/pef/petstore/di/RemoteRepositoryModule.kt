package cz.mendelu.pef.petstore.di


import cz.mendelu.pef.petstore.communication.pets.pets.PetsAPI
import cz.mendelu.pef.petstore.communication.pets.pets.PetsRemoteRepositoryImpl
import cz.mendelu.pef.petstore.communication.users.UsersApi
import cz.mendelu.pef.petstore.communication.users.UsersRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {

    @Provides
    @Singleton
    fun providePetsRemoteRepository(petsAPI: PetsAPI): PetsRemoteRepositoryImpl
        = PetsRemoteRepositoryImpl(petsAPI)

    @Provides
    @Singleton
    fun provideUsersRemoteRepository(usersApi: UsersApi): UsersRemoteRepositoryImpl
            = UsersRemoteRepositoryImpl(usersApi)
}