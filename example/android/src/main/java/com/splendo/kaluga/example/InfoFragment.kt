package com.splendo.kaluga.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelFragment
import com.splendo.kaluga.example.shared.viewmodel.info.InfoViewModel
import kotlinx.android.synthetic.main.fragment_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoFragment : KalugaViewModelFragment<InfoViewModel>(R.layout.fragment_info) {

    override val viewModel: InfoViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = InfoAdapter(viewModel).apply {
            info_buttons.adapter = this
        }
        viewModel.buttons.observe(this, Observer { adapter.buttons = it })
    }
}

class InfoAdapter(private val viewModel: InfoViewModel) : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    class InfoViewHolder(val button: AppCompatButton) : RecyclerView.ViewHolder(button)

    var buttons: List<InfoViewModel.Button> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val button = LayoutInflater.from(parent.context).inflate(R.layout.view_feature_button, parent, false) as AppCompatButton
        return InfoViewHolder(button)
    }

    override fun getItemCount(): Int = buttons.size

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        buttons.getOrNull(position)?.let { button ->
            holder.button.text = button.title
            holder.button.setOnClickListener { viewModel.onButtonPressed(button) }
        } ?: run {
            holder.button.text = null
            holder.button.setOnClickListener(null)
        }
    }
}

class InfoDialog(val title: String, val message: String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.dialog_info, container, false)

        v.findViewById<TextView>(R.id.title).text = title
        v.findViewById<TextView>(R.id.message).text = message

        return v
    }

}