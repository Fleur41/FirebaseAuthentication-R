package com.sam.firebaseauthentication_r.di

import com.sam.firebaseauthentication_r.authentication.AuthRepository
import com.sam.firebaseauthentication_r.authentication.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}