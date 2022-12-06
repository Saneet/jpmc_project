package com.saneet.demo.dagger

import com.saneet.demo.DemoApplication
import com.saneet.demo.MainActivity
import com.saneet.demo.network.NetworkModule
import com.saneet.demo.schooldetails.SchoolDetailsFragment
import com.saneet.demo.schoollist.SchoolListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DemoAppModule::class, NetworkModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun appModule(module: DemoAppModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: DemoApplication)
    fun inject(mainActivity: MainActivity)
    fun inject(schoolListFragment: SchoolListFragment)
    fun inject(schoolListFragment: SchoolDetailsFragment)
}