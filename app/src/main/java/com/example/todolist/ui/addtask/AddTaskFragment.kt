package com.example.todolist.ui.addtask

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.ToDoListApplication
import com.example.todolist.data.Item
import com.example.todolist.databinding.FragmentAddTaskBinding
import com.example.todolist.ui.list.InventoryViewModelFactory
import com.example.todolist.ui.list.ToDoListViewModel
import com.example.todolist.ui.taskdetail.TaskDetailFragmentArgs


class AddTaskFragment : Fragment() {

    private val viewModel: ToDoListViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as ToDoListApplication).database
                .itemDao()
        )
    }
    private val navigationArgs: TaskDetailFragmentArgs by navArgs()

    lateinit var item: Item


    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.newTask.text.toString()
        )
    }


    private fun bind(item: Item) {

        binding.apply {
            newTask.setText(item.itemTask, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }
        }
    }


    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.newTask.text.toString(),
            )
            val action = AddTaskFragmentDirections.actionAddTaskFragmentToToDoListFragment()
            findNavController().navigate(action)
        }
    }


    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.taskId,
                this.binding.newTask.text.toString(),
                isCompleted = false
            )
            val action = AddTaskFragmentDirections.actionAddTaskFragmentToToDoListFragment()
            findNavController().navigate(action)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.taskId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                item = selectedItem
                bind(item)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}