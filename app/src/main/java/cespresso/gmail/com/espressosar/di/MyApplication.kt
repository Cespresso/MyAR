package cespresso.gmail.com.espressosar.di

import android.app.Application
import cespresso.gmail.com.espressosar.data.source.ApiService
import cespresso.gmail.com.espressosar.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

class MyApplication : Application() {
    private val viewModelModule = module {
        single { ApiService().service }
        viewModel{MainViewModel(get())}
    }
    override fun onCreate(){
        super.onCreate()
        // start Koin!
        startKoin {
            // Android context
            androidContext(this@MyApplication)
            // modules
            modules(viewModelModule)
        }
    }
}
