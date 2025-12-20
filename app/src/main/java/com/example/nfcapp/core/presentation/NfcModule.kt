package com.example.nfcapp.core.presentation

import com.example.nfcapp.core.data.repository.NfcRepositoryImpl
import com.example.nfcapp.core.data.source.ReadDataSource
import com.example.nfcapp.core.domain.ReadClassicCardUseCase
import com.example.nfcapp.core.domain.repository.NfcReadRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NfcModule {

    @Provides
    fun provideDataSource() = ReadDataSource()

    @Provides
    fun provideRepository(
        source: ReadDataSource
    ): NfcReadRepository = NfcRepositoryImpl(source)

    @Provides
    fun provideUseCase(
        repo: NfcReadRepository
    ) = ReadClassicCardUseCase(repo)
}