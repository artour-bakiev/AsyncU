package bakiev.artour.asyncu.di

import bakiev.artour.asyncu.services.DefaultDispatcherProvider
import bakiev.artour.asyncu.services.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}
