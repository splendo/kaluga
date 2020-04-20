package com.splendo.kaluga.example.shared.viewmodel.info

import com.splendo.kaluga.architecture.navigation.*
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel

class DialogSpec : NavigationBundleSpec<DialogSpecRow>(setOf(DialogSpecRow.TitleRow, DialogSpecRow.MessageRow))

sealed class DialogSpecRow : NavigationBundleSpecRow<String>(NavigationBundleSpecType.StringType) {
    object TitleRow : DialogSpecRow()
    object MessageRow : DialogSpecRow()
}

class LinkSpec : NavigationBundleSpec<LinkSpecRow>(setOf(LinkSpecRow.LinkRow))

sealed class LinkSpecRow : NavigationBundleSpecRow<String>(NavigationBundleSpecType.StringType) {
    object LinkRow : LinkSpecRow()
}

class MailSpec : NavigationBundleSpec<MailSpecRow<*>>(setOf(MailSpecRow.ToRow, MailSpecRow.SubjectRow))

sealed class MailSpecRow<V>(associatedType: NavigationBundleSpecType<V>) : NavigationBundleSpecRow<V>(associatedType) {
    object ToRow : MailSpecRow<List<String>>(NavigationBundleSpecType.StringArrayType)
    object SubjectRow : MailSpecRow<String>(NavigationBundleSpecType.StringType)
}

sealed class InfoNavigation<B: NavigationBundleSpecRow<*>>(bundle: NavigationBundle<B>) : NavigationAction<B>(bundle) {

    class Dialog(bundle: NavigationBundle<DialogSpecRow>) : InfoNavigation<DialogSpecRow>(bundle)
    class Link(bundle: NavigationBundle<LinkSpecRow>) : InfoNavigation<LinkSpecRow>(bundle)
    class Mail(bundle: NavigationBundle<MailSpecRow<*>>) : InfoNavigation<MailSpecRow<*>>(bundle)
}

class InfoViewModel(navigator: Navigator<InfoNavigation<*>>) : NavigatingViewModel<InfoNavigation<*>>(navigator) {

    sealed class Button(val title: String) {
        object About : Button("About")
        object Website : Button("Kaluga.io")
        object GitHub : Button("GitHub")
        object Mail : Button("Contact")
    }

    val buttons = observableOf(listOf(Button.About, Button.Website, Button.GitHub, Button.Mail))

    fun onButtonPressed(button: Button) {
        navigator.navigate(when(button) {
            is Button.About -> InfoNavigation.Dialog( DialogSpec().toBundle { row ->
                when (row) {
                    is DialogSpecRow.TitleRow -> row.associatedType.convertValue("About Us")
                    is DialogSpecRow.MessageRow -> row.associatedType.convertValue("Kaluga is developed by Splendo Consulting BV")
                }
            })
            is Button.Website -> InfoNavigation.Link(LinkSpec().toBundle { row ->
                when (row) {
                    is LinkSpecRow.LinkRow -> row.associatedType.convertValue("https://kaluga.splendo.com")
                }
            })
            is Button.GitHub -> InfoNavigation.Link(LinkSpec().toBundle { row ->
                when (row) {
                    is LinkSpecRow.LinkRow -> row.associatedType.convertValue("https://github.com/splendo/kaluga")
                }
            })
            is Button.Mail -> InfoNavigation.Mail( MailSpec().toBundle { row ->
                when (row) {
                    is MailSpecRow.ToRow -> row.associatedType.convertValue(listOf("info@splendo.com"))
                    is MailSpecRow.SubjectRow -> row.associatedType.convertValue("Question about Kaluga")
                }
            })
        })
    }

}