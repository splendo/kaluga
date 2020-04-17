package com.splendo.kaluga.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.KalugaViewModelActivity
import com.splendo.kaluga.example.shared.viewmodel.Feature
import com.splendo.kaluga.example.shared.viewmodel.FeatureListViewModel
import kotlinx.android.synthetic.main.activity_features_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeaturesListActivity : KalugaViewModelActivity<FeatureListViewModel>(R.layout.activity_features_list) {

    override val viewModel: FeatureListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = FeaturesAdapter(viewModel).apply {
            features_list.adapter = this
        }
        viewModel.feature.observe(this, Observer { adapter.features = it })
    }
}

class FeaturesAdapter(private val viewModel: FeatureListViewModel) : RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder>() {

    class FeatureViewHolder(val button: AppCompatButton) : RecyclerView.ViewHolder(button)

    var features: List<Feature> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val button = LayoutInflater.from(parent.context).inflate(R.layout.view_feature_button, parent, false) as AppCompatButton
        return FeatureViewHolder(button)
    }

    override fun getItemCount(): Int = features.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        features.getOrNull(position)?.let { feature ->
            holder.button.text = feature.title
            holder.button.setOnClickListener { viewModel.onFeaturePressed(feature) }
        } ?: run {
            holder.button.text = null
            holder.button.setOnClickListener(null)
        }
    }
}