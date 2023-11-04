package au.cmcmarkets.ticker.core.di.module

import au.cmcmarkets.ticker.feature.main.MainActivity
import au.cmcmarkets.ticker.feature.orderticket.OrderTicketFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributesMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributesOrderTicketFragment(): OrderTicketFragment
}
