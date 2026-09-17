package com.sam.firebaseauthentication_r.di

import com.sam.firebaseauthentication_r.authentication.AuthRepository
import com.sam.firebaseauthentication_r.authentication.AuthRepositoryImpl
import com.sam.firebaseauthentication_r.datastore.DatastoreRepository
import com.sam.firebaseauthentication_r.datastore.DatastoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindDatastoreRepository(impl: DatastoreRepositoryImpl): DatastoreRepository
}