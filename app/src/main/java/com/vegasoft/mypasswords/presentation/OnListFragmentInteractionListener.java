package com.vegasoft.mypasswords.presentation;

import com.vegasoft.mypasswords.presentation.ui_models.UIRecord;

public interface OnListFragmentInteractionListener {
    void onItemClick(UIRecord item);
    void saveClick();

    void removeClick(UIRecord mRecord);
}
