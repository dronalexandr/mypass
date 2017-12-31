package com.vegasoft.mypasswords.presentation.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vegasoft.mypasswords.R;
import com.vegasoft.mypasswords.data.db.RecordsDBHelper;
import com.vegasoft.mypasswords.data.entity.Record;
import com.vegasoft.mypasswords.presentation.OnListFragmentInteractionListener;
import com.vegasoft.mypasswords.presentation.ViewPhotoActivity;
import com.vegasoft.mypasswords.utils.BitmapHelpers;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewRecordFragment extends Fragment {

    private static final String ARG_RECORD = "record";
    private static final int ACTION_PHOTO = 1;
    private static final int ACTION_GALLERY = 0;
    private OnListFragmentInteractionListener mListener;

    private Record mRecord;

    private ImageView imageView;

    public ViewRecordFragment() {
    }

    public static ViewRecordFragment newInstance(Record record) {
        ViewRecordFragment fragment = new ViewRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_RECORD, record);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecord = (Record) getArguments().getSerializable(ARG_RECORD);
        }

        if (mRecord == null) {
            mRecord = new Record();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        if (!TextUtils.isEmpty(mRecord.getImage())) {
            Picasso.with(getActivity()).load(new File(mRecord.getImage())).into(imageView);
        }
        imageView = view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureChooser();
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (Activity.RESULT_OK != resultCode)
            return;
        final Uri data;
        switch (requestCode) {
            case ACTION_PHOTO:
                data = imageReturnedIntent.getData();
                if (data != null) {
                    processPhoto(data);
                }
                break;
            case ACTION_GALLERY:
                data = imageReturnedIntent.getData();
                if (data != null) {
                    processPhoto(data);
                }
                break;
        }
    }

    /**
     * Process photo after taking from gallery of camera
     *
     * @param data Uri
     */
    private void processPhoto(Uri data) {
        String realPathFromURI = BitmapHelpers.getRealPathFromURI(getActivity(), data);
        Bitmap bitmap = BitmapHelpers.prepareAndGetBitmap(
                realPathFromURI, null);
        if (bitmap != null) {
            imageView.setTag(realPathFromURI);
            imageView.setImageBitmap(bitmap);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final Intent intent = new Intent(getActivity(), ViewPhotoActivity.class);
                    final Bundle b = new Bundle();
                    b.putString(ViewPhotoActivity.ARG_PHOTO, (String) view.getTag());
                    intent.putExtras(b);
                    startActivity(intent);
                    return false;
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPictureChooser();
                }
            });
        }
    }

    private void showPictureChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Image");
        builder.setItems(new CharSequence[]{"Gallery", "Camera"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case ACTION_GALLERY:
                                // GET IMAGE FROM THE GALLERY
                                Intent intent = new Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                                startActivityForResult(chooser, ACTION_GALLERY);
                                break;

                            case ACTION_PHOTO:
                                // GET IMAGE FROM THE CAMERA
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, ACTION_PHOTO);
                                }
                                break;

                            default:
                                break;
                        }
                    }
                });

        builder.show();
    }


    private void save() {
        RecordsDBHelper recordsDBHelper = new RecordsDBHelper(getActivity());
        Record record = new Record();
        record.setName("name " + System.currentTimeMillis());
        record.setDate(System.currentTimeMillis());
        record.setSite("site");
        record.setImage((String) imageView.getTag());
        recordsDBHelper.addRecord(record);
        if (mListener != null) {
            mListener.saveClick();
        }
    }

}
