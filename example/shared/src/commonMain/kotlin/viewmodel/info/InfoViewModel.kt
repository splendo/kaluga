package com.splendo.kaluga.example.shared.viewmodel.info

import com.splendo.kaluga.architecture.navigation.*
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel

class DialogSpec : NavigationBundleSpec<DialogSpecRow<*>>(setOf(DialogSpecRow.TitleRow, DialogSpecRow.MessageRow))

sealed class DialogSpecRow<V> : NavigationBundleSpecRow<V> {
    object TitleRow : DialogSpecRow<String>() {
        override val key: String = "TitleRow"
        override val associatedType = NavigationBundleSpecType.StringType
    }
    object MessageRow : DialogSpecRow<String>() {
        override val key: String = "MessageRow"
        override val associatedType = NavigationBundleSpecType.StringType
    }
}

class LinkSpec : NavigationBundleSpec<LinkSpecRow<*>>(setOf(LinkSpecRow.LinkRow))

sealed class LinkSpecRow<V> : NavigationBundleSpecRow<V> {
    object LinkRow : LinkSpecRow<String>() {
        override val key: String = "LinkRow"
        override val associatedType = NavigationBundleSpecType.StringType
    }
}

class MailSpec : NavigationBundleSpec<MailSpecRow<*>>(setOf(MailSpecRow.ToRow, MailSpecRow.SubjectRow))

sealed class MailSpecRow<V> : NavigationBundleSpecRow<V> {
    object ToRow : MailSpecRow<List<String>>() {
        override val key: String = "ToRow"
        override val associatedType = NavigationBundleSpecType.StringArrayType
    }
    object SubjectRow : MailSpecRow<String>() {
        override val key: String = "SubjectRow"
        override val associatedType = NavigationBundleSpecType.StringType
    }
}

sealed class InfoNavigation<B: NavigationBundleSpecRow<*>> : NavigationAction<B>() {

    data class Dialog(private val title: String, private val message: String) : InfoNavigation<DialogSpecRow<*>>() {
        private val dialogSpec = DialogSpec()
        override val bundle: NavigationBundle<DialogSpecRow<*>>?
            get() = dialogSpec.toBundle { row ->
                when (row) {
                    is DialogSpecRow.TitleRow -> row.associatedType.convertValue(title)
                    is DialogSpecRow.MessageRow -> row.associatedType.convertValue(message)
                }
            }
    }
    data class Link(val url: String) : InfoNavigation<LinkSpecRow<*>>() {
        private val linkSped = LinkSpec()
        override val bundle: NavigationBundle<LinkSpecRow<*>>?
            get() = linkSped.toBundle { row ->
                when (row) {
                    is LinkSpecRow.LinkRow -> row.associatedType.convertValue(url)
                }
            }
    }
    data class Mail(val to: String, val subject: String) : InfoNavigation<MailSpecRow<*>>() {
        private val mailSpec = MailSpec()
        override val bundle: NavigationBundle<MailSpecRow<*>>?
            get() = mailSpec.toBundle { row ->
                when (row) {
                    is MailSpecRow.ToRow -> row.associatedType.convertValue(listOf(to))
                    is MailSpecRow.SubjectRow -> row.associatedType.convertValue(subject)
                }
            }
    }
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
            is Button.About -> InfoNavigation.Dialog("About Us", "Kaluga is developed by Splendo Consulting BV")
            is Button.Website -> InfoNavigation.Link("https://kaluga.splendo.com")
            is Button.GitHub -> InfoNavigation.Link("https://github.com/splendo/kaluga")
            is Button.Mail -> InfoNavigation.Mail("info@splendo.com", "Question about Kaluga")
        })
    }

}