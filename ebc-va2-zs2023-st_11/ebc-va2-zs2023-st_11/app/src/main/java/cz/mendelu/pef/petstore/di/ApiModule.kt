package cz.mendelu.pef.petstore.di


import cz.mendelu.pef.petstore.communication.pets.pets.PetsAPI
import cz.mendelu.pef.petstore.communication.users.UsersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providePetsAPI(retrofit: Retrofit): PetsAPI
        = retrofit.create(PetsAPI::class.java)

    @Provides
    @Singleton
    fun provideUsersApi(retrofit: Retrofit): UsersApi
            = retrofit.create(UsersApi::class.java)
}