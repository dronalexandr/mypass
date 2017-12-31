package com.vegasoft.mypasswords.data.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.vegasoft.mypasswords.data.db.RecordsDBHelper;
import com.vegasoft.mypasswords.data.entity.Record;

import java.util.ArrayList;

public class RecordsLoader extends AsyncTaskLoader<LoaderResult<ArrayList<Record>>> {
    private RecordsDBHelper recordsDBHelper;

    public RecordsLoader(Context context) {
        super(context);
        recordsDBHelper = new RecordsDBHelper(context);
    }

    @Override
    public LoaderResult<ArrayList<Record>> loadInBackground() {
        return new LoaderResult<>(recordsDBHelper.getRecords());
    }
}
