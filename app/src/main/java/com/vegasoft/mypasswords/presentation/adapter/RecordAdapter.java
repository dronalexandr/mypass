package com.vegasoft.mypasswords.presentation.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vegasoft.mypasswords.R;
import com.vegasoft.mypasswords.presentation.OnListFragmentInteractionListener;
import com.vegasoft.mypasswords.presentation.ui_models.UIRecord;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<UIRecord> mValues;
    private final OnListFragmentInteractionListener mListener;

    public RecordAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
        mValues = new ArrayList<>();
    }

    public void setItems(List<UIRecord> items) {
        mValues = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return mValues.isEmpty();
    }

    public void removeItem(int position) {
        mValues.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(int position, UIRecord record) {
        mValues.add(position, record);
        notifyItemInserted(position);
    }

    public UIRecord getRecord(int position) {
        return mValues.get(position);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pass_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.mItem = mValues.get(position);
        if (!TextUtils.isEmpty(holder.mItem.getTitle())) {
            holder.mNameView.setText(holder.mItem.getTitle());
        }
        if (!TextUtils.isEmpty(holder.mItem.getSecuredData().getSite())) {
            holder.mSiteView.setText(holder.mItem.getSecuredData().getSite());
        }
        if (!TextUtils.isEmpty(holder.mItem.getImage())) {
            Picasso.get().load(new File(holder.mItem.getImage())).into(holder.mImageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onItemClick(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mNameView;
        final TextView mSiteView;
        final ImageView mImageView;
        UIRecord mItem;

        ViewHolder(View view) {
            super(view);
            mNameView = view.findViewById(R.id.name_textView);
            mSiteView = view.findViewById(R.id.site_textView);
            mImageView = view.findViewById(R.id.imageView);
        }
    }
}
