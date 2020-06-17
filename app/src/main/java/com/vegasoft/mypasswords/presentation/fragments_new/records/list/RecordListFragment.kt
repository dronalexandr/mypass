package com.vegasoft.mypasswords.presentation.fragments_new.records.list

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vegasoft.mypasswords.R
import com.vegasoft.mypasswords.presentation.OnListFragmentInteractionListener
import com.vegasoft.mypasswords.presentation.adapter.RecordAdapter
import kotlinx.android.synthetic.main.fragment_record_list.empty_message as emptyMessage
import kotlinx.android.synthetic.main.fragment_record_list.myCoordinatorLayout

class RecordListFragment : Fragment() {

    private var mListener: OnListFragmentInteractionListener? = null
    private lateinit var adapter: RecordAdapter


    var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            return super.getSwipeThreshold(viewHolder)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {

            val adapterPosition = viewHolder.adapterPosition
            val record = adapter.getRecord(adapterPosition) ?: return
            viewModel.removeRecord(requireContext(), record.id)
            adapter.removeItem(adapterPosition)
            view?.let {
                val snackbar = Snackbar
                        .make(myCoordinatorLayout, "Do you want to restore record?", Snackbar.LENGTH_SHORT)
                        .setAction("YES") {
                            viewModel.addRecord(requireContext(), record)
                            adapter.addItem(adapterPosition, record)
                            emptyMessage.visibility = View.GONE
                        }
                snackbar.setActionTextColor(Color.RED)
                val sbView = snackbar.view
                val textView = sbView.findViewById<TextView>(R.id.snackbar_text)
                textView.setTextColor(Color.WHITE)
                snackbar.show()
                if (adapter.isEmpty) {
                    emptyMessage.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        fun newInstance() = RecordListFragment()
    }

    private lateinit var viewModel: RecordListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_record_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RecordListViewModel::class.java)
        viewModel._records.observe(viewLifecycleOwner, Observer {
            adapter.setItems(it)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(
                    requireContext(),
                    it.message,
                    Toast.LENGTH_SHORT
            ).show()
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadRecords(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the adapter
        val recyclerView: RecyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        adapter = RecordAdapter(mListener)
        recyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnListFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}
