package com.example.todolist.ui.taskdetail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.R
import com.example.todolist.ToDoListApplication
import com.example.todolist.data.Item
import com.example.todolist.databinding.FragmentTaskDetailBinding
import com.example.todolist.ui.list.InventoryViewModelFactory
import com.example.todolist.ui.list.ToDoListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class TaskDetailFragment : Fragment() {
    private val navigationArgs: TaskDetailFragmentArgs by navArgs()
    lateinit var item: Item

    private val viewModel: ToDoListViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as ToDoListApplication).database.itemDao()
        )
    }

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun bind(item: Item) {
        binding.apply {
            taskName.text = item.itemTask
            deleteTask.setOnClickListener { showConfirmationDialog() }
            editTask.setOnClickListener { editItem() }
            doneTask.setOnClickListener { completeItem() }
        }
    }


    private fun editItem() {
        val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToAddTaskFragment(
            getString(R.string.edit_fragment_title),
            item.id
        )
        this.findNavController().navigate(action)
    }


    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }


    private fun deleteItem() {
        viewModel.deleteItem(item)
        findNavController().navigateUp()
    }

    private fun completeItem() {
        item.let { it.isCompleted = true
        viewModel.updateItem(it)}
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.taskId

        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            item = selectedItem
            bind(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
