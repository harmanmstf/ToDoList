package com.example.todolist.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.*
import com.example.todolist.databinding.FragmentToDoListBinding


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
