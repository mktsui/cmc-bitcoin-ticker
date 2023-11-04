package au.cmcmarkets.ticker.core.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import au.cmcmarkets.ticker.core.di.viewmodel.ViewModelFactory
import au.cmcmarkets.ticker.core.di.viewmodel.ViewModelKey
import au.cmcmarkets.ticker.feature.orderticket.OrderTicketViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(OrderTicketViewModel::class)
    abstract fun bindOrderTicketViewModel(viewModel: OrderTicketViewModel): ViewModel

}
