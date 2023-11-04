package au.cmcmarkets.ticker.core.di

import au.cmcmarkets.ticker.CmcApp
import au.cmcmarkets.ticker.core.di.module.ActivityFragmentModule
import au.cmcmarkets.ticker.core.di.module.AppModule
import au.cmcmarkets.ticker.core.di.module.NetworkModule
import au.cmcmarkets.ticker.core.di.module.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        ActivityFragmentModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<CmcApp> {

    @Component.Factory
    abstract class Builder : AndroidInjector.Factory<CmcApp>
}