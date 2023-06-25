package com.example.todolist.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.*
import com.example.todolist.databinding.FragmentToDoListBinding
import com.example.todolist.ui.completedtask.CompletedItemAdapter


class ToDoListFragment : Fragment() {
    private val viewModel: ToDoListViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as ToDoListApplication).database.itemDao()
        )
    }

    private var _binding: FragmentToDoListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemListAdapter {
            val action =
                ToDoListFragmentDirections.actionToDoListFragmentToTaskDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        val completedItemAdapter = CompletedItemAdapter { item ->
            // Item clicked
        }


        viewModel.completedItems.observe(viewLifecycleOwner) { completedItems ->
            completedItems?.let {
                completedItemAdapter.submitList(it)
            }
        }

        completedItemAdapter.getItemCountLiveData().observe(viewLifecycleOwner, Observer { count ->
            binding.tvCompletedCount.text = "($count)"
        })

        binding.plusImageButton.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToAddTaskFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }

        binding.tvCompleted.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToCompletedTaskFragment(

            )
            this.findNavController().navigate(action)
        }
    }
}
