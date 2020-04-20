package com.splendo.kaluga.example.shared.viewmodel.architecture

import com.splendo.kaluga.architecture.navigation.*
import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.architecture.observable.ObservableResult
import com.splendo.kaluga.architecture.observable.subjectOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel

class DetailsSpec : NavigationBundleSpec<DetailsSpecRow<*>>(setOf(DetailsSpecRow.NameRow, DetailsSpecRow.NumberRow))

sealed class DetailsSpecRow<V>(associatedType: NavigationBundleSpecType<V>) : NavigationBundleSpecRow<V>(associatedType) {
    object NameRow : DetailsSpecRow<String>(NavigationBundleSpecType.StringType)
    object NumberRow : DetailsSpecRow<Int>( NavigationBundleSpecType.IntegerType)
}

class CloseDetailsNavigation(bundle: NavigationBundle<DetailsSpecRow<*>>): NavigationAction<DetailsSpecRow<*>>(bundle)

class ArchitectureDetailsViewModel(initialName: String, initialNumber: Int, navigator: Navigator<CloseDetailsNavigation>) : NavigatingViewModel<CloseDetailsNavigation>(navigator) {

    private val _name  = subjectOf(initialName, coroutineScope)
    val name: Observable<String> = _name
    private val _number  = subjectOf(initialNumber.toString(), coroutineScope)
    val number: Observable<String> = _number

    private var nameResult: ObservableResult<String> by _name
    private var numberResult: ObservableResult<String> by _number

    fun onInversePressed() {
        val newName: String? by nameResult
        val newNumber: String? by numberResult

        newName?.let {
            nameResult = ObservableResult.Result(it.reversed())
        }
        newNumber?.let {
            numberResult = ObservableResult.Result(it.reversed())
        }
    }

    fun onClosePressed() {
        val name: String? by nameResult
        val number: String? by numberResult
        navigator.navigate(CloseDetailsNavigation(DetailsSpec().toBundle {row ->
            when (row) {
                is DetailsSpecRow.NameRow -> row.convertValue(name ?: "")
                is DetailsSpecRow.NumberRow -> row.convertValue(number?.toIntOrNull() ?: 0)
            }
        }))
    }

}