package com.vegasoft.mypasswords.presentation.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vegasoft.mypasswords.R;
import com.vegasoft.mypasswords.bussiness.ConfigManager;
import com.vegasoft.mypasswords.bussiness.SecureManager;
import com.vegasoft.mypasswords.data.db.RecordsDBHelper;
import com.vegasoft.mypasswords.data.entity.Record;
import com.vegasoft.mypasswords.presentation.OnListFragmentInteractionListener;
import com.vegasoft.mypasswords.presentation.ViewPhotoActivity;
import com.vegasoft.mypasswords.utils.BitmapHelpers;

import org.jetbrains.annotations.NotNull;

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
    private EditText nameEditText;
    private EditText groupEditText;
    private EditText siteEditText;
    private EditText userEditText;
    private EditText passEditText;
    private ConfigManager configManager;

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
    public void onAttach(@NotNull Activity activity) {
        super.onAttach(activity);
        configManager = new ConfigManager(activity);
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.imageView);
        nameEditText = view.findViewById(R.id.name_editText);
        groupEditText = view.findViewById(R.id.group_editText);
        siteEditText = view.findViewById(R.id.site_editText);
        userEditText = view.findViewById(R.id.user_editText);
        passEditText = view.findViewById(R.id.password_editText);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureChooser();
            }
        });

        view.findViewById(R.id.save_button).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        final String image = mRecord.getImage();
        if (!TextUtils.isEmpty(image)) {
            imageView.setTag(image);
            Picasso.get().load(new File(image)).into(imageView);
        }
        final String name = mRecord.getName();
        if (!TextUtils.isEmpty(name)) {
            nameEditText.setText(name);
        }
        final String group = mRecord.getGroup();
        if (!TextUtils.isEmpty(group)) {
            groupEditText.setText(group);
        }
        final String site = mRecord.getSite();
        if (!TextUtils.isEmpty(site)) {
            siteEditText.setText(site);
        }
        final String user = mRecord.getUser();
        if (!TextUtils.isEmpty(user)) {
            userEditText.setText(user);
        } else {
            if (mRecord.getId() == null) {
                userEditText.setText(new ConfigManager(requireActivity()).getDefaultUser());
            }
        }
        final String pass = mRecord.getPass();
        if (!TextUtils.isEmpty(pass)) {
            switch (configManager.getEncryption()) {
                case RSA:
                    passEditText.setText(SecureManager.decryptRSA(pass));
                    break;
                case AES:
                    passEditText.setText(SecureManager.decryptAES(pass));
                    break;
            }
        }

        nameEditText.addTextChangedListener(new MyWatcher(name));
        groupEditText.addTextChangedListener(new MyWatcher(group));
        siteEditText.addTextChangedListener(new MyWatcher(site));
        userEditText.addTextChangedListener(new MyWatcher(user));
        passEditText.addTextChangedListener(new MyWatcher(pass));
    }

    @Override
    public void onAttach(@NotNull Context context) {
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
        final View view = getView();
        switch (requestCode) {
            case ACTION_PHOTO:
            case ACTION_GALLERY:
                data = imageReturnedIntent.getData();
                if (data != null) {
                    processPhoto(data);
                }
                if (view != null)
                    view.findViewById(R.id.save_button).setVisibility(View.VISIBLE);
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
        if (getActivity() == null) {
            return;
        }
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
                                if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
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
        mRecord.setName(nameEditText.getText().toString());
        mRecord.setGroup(groupEditText.getText().toString());
        mRecord.setDate(System.currentTimeMillis());
        mRecord.setSite(siteEditText.getText().toString());
        mRecord.setUser(userEditText.getText().toString());
        switch (configManager.getEncryption()) {
            case RSA:
                mRecord.setPass(SecureManager.encryptRSA(passEditText.getText().toString()));
                break;
            case AES:
                mRecord.setPass(SecureManager.encryptAES(passEditText.getText().toString()));
                break;
        }
        mRecord.setImage((String) imageView.getTag());
        recordsDBHelper.putRecord(mRecord);
        if (mListener != null) {
            mListener.saveClick();
        }
    }

    class MyWatcher implements TextWatcher {
        private String initState;

        MyWatcher(String initState) {
            if (initState == null) {
                this.initState = "";
            } else {
                this.initState = initState;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            final View view = getView();
            if (view == null) {
                return;
            }
            if (initState.equals(editable.toString())) {
                view.findViewById(R.id.save_button).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.save_button).setVisibility(View.VISIBLE);
            }
        }
    }

}
