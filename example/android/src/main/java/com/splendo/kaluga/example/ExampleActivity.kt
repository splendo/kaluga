package com.splendo.kaluga.example

import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.splendo.kaluga.architecture.KalugaViewModelActivity
import com.splendo.kaluga.example.shared.viewmodel.ExampleViewModel
import kotlinx.android.synthetic.main.activity_example.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExampleActivity : KalugaViewModelActivity<ExampleViewModel>(R.layout.activity_example) {

    override val viewModel: ExampleViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.tabs.observe(this, Observer { exampleTabs ->
            tabs.removeAllTabs()
            exampleTabs.forEach { tab ->
                tabs.addTab(tabs.newTab().setText(tab.title).setTag(tab))
            }
        })

        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val exampleTab = tab?.tag as? ExampleViewModel.Tab ?: return
                viewModel.tab.postValue(exampleTab)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val exampleTab = tab?.tag as? ExampleViewModel.Tab ?: return
                viewModel.tab.postValue(exampleTab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
    }

}