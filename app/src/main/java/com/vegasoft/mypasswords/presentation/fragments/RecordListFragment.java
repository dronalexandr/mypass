package com.vegasoft.mypasswords.presentation.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vegasoft.mypasswords.R;
import com.vegasoft.mypasswords.data.db.RecordsDBHelper;
import com.vegasoft.mypasswords.data.entity.Record;
import com.vegasoft.mypasswords.data.loader.LoaderResult;
import com.vegasoft.mypasswords.data.loader.RecordsLoader;
import com.vegasoft.mypasswords.presentation.OnListFragmentInteractionListener;
import com.vegasoft.mypasswords.presentation.adapter.RecordAdapter;

import java.util.ArrayList;

public class RecordListFragment extends Fragment {

    private static final int RECORD_LOADER_ID = 0;

    private OnListFragmentInteractionListener mListener;
    private RecordAdapter adapter;

    private LoaderManager.LoaderCallbacks<LoaderResult<ArrayList<Record>>> generalLoader = new LoaderManager.LoaderCallbacks<LoaderResult<ArrayList<Record>>>() {

        @Override
        public Loader<LoaderResult<ArrayList<Record>>> onCreateLoader(int arg0,
                                                                      Bundle arg1) {
            return new RecordsLoader(getActivity());
        }

        @Override
        public void onLoadFinished(
                Loader<LoaderResult<ArrayList<Record>>> loader,
                LoaderResult<ArrayList<Record>> result) {
            if (getView() == null) {
                return;
            }
            getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
            if (result.error == null) {
                if (result.data != null) {
                    adapter.setItems(result.data);
                    if (result.data.size() < 1) {
                        getView().findViewById(R.id.empty_message).setVisibility(View.VISIBLE);
                    }
                } else {
                    getView().findViewById(R.id.empty_message).setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<LoaderResult<ArrayList<Record>>> loader) {
        }

    };

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            return super.getSwipeThreshold(viewHolder);
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
            if (getView() == null) {
                return;
            }
            final int adapterPosition = viewHolder.getAdapterPosition();
            final Record record = adapter.getRecord(adapterPosition);
            new RecordsDBHelper(getActivity()).deleteRecord(record.getId());
            adapter.removeItem(adapterPosition);
            final Snackbar snackbar = Snackbar
                    .make(getView().findViewById(R.id.myCoordinatorLayout), "Do you want return record?", Snackbar.LENGTH_SHORT)
                    .setAction("YES", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new RecordsDBHelper(getActivity()).addRecord(record);
                            adapter.addItem(adapterPosition, record);
                            getView().findViewById(R.id.empty_message).setVisibility(View.GONE);
                        }
                    });

            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
            if (adapter.getItemCount() < 1) {
                getView().findViewById(R.id.empty_message).setVisibility(View.VISIBLE);
            }
        }
    };

    public RecordListFragment() {
    }

    public static RecordListFragment newInstance() {
        return new RecordListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pass_list, container, false);

        // Set the adapter
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecordAdapter(mListener);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().restartLoader(RECORD_LOADER_ID, null,
                generalLoader).forceLoad();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
