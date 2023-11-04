package au.cmcmarkets.ticker

import au.cmcmarkets.ticker.core.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class CmcApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}