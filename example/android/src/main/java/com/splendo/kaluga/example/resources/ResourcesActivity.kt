package com.splendo.kaluga.example.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityResourcesBinding
import com.splendo.kaluga.example.databinding.ViewListButtonBinding
import com.splendo.kaluga.example.shared.viewmodel.resources.Resource
import com.splendo.kaluga.example.shared.viewmodel.resources.ResourcesListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResourcesActivity : KalugaViewModelActivity<ResourcesListViewModel>() {
    override val viewModel: ResourcesListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityResourcesBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        val adapter = ResourcesAdapter(viewModel).apply {

            binding.resourcesList.adapter = this
        }
        viewModel.resources.observeInitialized { adapter.resources = it }
    }
}

class ResourcesAdapter(private val viewModel: ResourcesListViewModel) :
    RecyclerView.Adapter<ResourcesAdapter.ResourceViewHolder>() {

    class ResourceViewHolder(val binding: ViewListButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val button = binding.button
    }

    var resources: List<Resource> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val binding = ViewListButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResourceViewHolder(binding)
    }

    override fun getItemCount(): Int = resources.size

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        resources.getOrNull(position)?.let { resource ->
            holder.button.text = resource.title
            holder.button.setOnClickListener { viewModel.onResourceSelected(resource) }
        } ?: run {
            holder.button.text = null
            holder.button.setOnClickListener(null)
        }
    }
}
