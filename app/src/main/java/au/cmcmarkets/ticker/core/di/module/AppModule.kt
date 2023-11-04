package au.cmcmarkets.ticker.core.di.module

import android.content.Context
import au.cmcmarkets.ticker.CmcApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: CmcApp): Context = app.applicationContext

}