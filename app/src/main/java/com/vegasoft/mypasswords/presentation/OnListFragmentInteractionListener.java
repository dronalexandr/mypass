package com.vegasoft.mypasswords.presentation;

import com.vegasoft.mypasswords.data.entity.Record;

public interface OnListFragmentInteractionListener {
    void onItemClick(Record item);
    void saveClick();

    void removeClick(Record mRecord);
}
