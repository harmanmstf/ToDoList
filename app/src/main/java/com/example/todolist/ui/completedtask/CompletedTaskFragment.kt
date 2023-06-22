package com.example.todolist.ui.completedtask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.ToDoListApplication
import com.example.todolist.databinding.FragmentCompletedTaskBinding
import com.example.todolist.databinding.FragmentToDoListBinding
import com.example.todolist.ui.list.InventoryViewModelFactory
import com.example.todolist.ui.list.ItemListAdapter
import com.example.todolist.ui.list.ToDoListFragmentDirections
import com.example.todolist.ui.list.ToDoListViewModel


class CompletedTaskFragment : Fragment() {

    private val viewModel: ToDoListViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as ToDoListApplication).database.itemDao()
        )
    }

    private var _binding: FragmentCompletedTaskBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CompletedItemAdapter {
            val action =
                CompletedTaskFragmentDirections.actionCompletedTaskFragmentToTaskDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerViewCompleted.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerViewCompleted.adapter = adapter
        viewModel.completedItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }


    }


}