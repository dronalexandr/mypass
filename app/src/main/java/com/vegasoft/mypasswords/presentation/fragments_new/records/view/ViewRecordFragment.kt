package com.vegasoft.mypasswords.presentation.fragments_new.records.view


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.vegasoft.mypasswords.R
import com.vegasoft.mypasswords.bussiness.ConfigManager
import com.vegasoft.mypasswords.bussiness.SecureManager
import com.vegasoft.mypasswords.data.persistence.models.Encryption
import com.vegasoft.mypasswords.presentation.OnListFragmentInteractionListener
import com.vegasoft.mypasswords.presentation.ViewPhotoActivity
import com.vegasoft.mypasswords.presentation.ui_models.SecuredData
import com.vegasoft.mypasswords.presentation.ui_models.UIRecord
import com.vegasoft.mypasswords.utils.BitmapHelpers
import kotlinx.android.synthetic.main.fragment_view_record.*
import java.io.File
import java.util.*
import kotlinx.android.synthetic.main.fragment_view_record.delete_imageButton as removeButton
import kotlinx.android.synthetic.main.fragment_view_record.group_editText as groupEditText
import kotlinx.android.synthetic.main.fragment_view_record.name_editText as nameEditText
import kotlinx.android.synthetic.main.fragment_view_record.password_editText as passEditText
import kotlinx.android.synthetic.main.fragment_view_record.close_button as closeButton
import kotlinx.android.synthetic.main.fragment_view_record.site_editText as siteEditText
import kotlinx.android.synthetic.main.fragment_view_record.user_editText as userEditText
import kotlinx.android.synthetic.main.fragment_view_record.imageView as imageView

class ViewRecordFragment : Fragment() {

    private var mListener: OnListFragmentInteractionListener? = null

    private var mRecord: UIRecord? = null
    private var configManager: ConfigManager? = null
    private var imageUrl: String? = null
    private var editMode: Boolean = false

    companion object {
        const val ARG_RECORD = "record"
        const val ACTION_PHOTO = 1
        const val ACTION_GALLERY = 0

        fun newInstance(record: UIRecord?): ViewRecordFragment {
            val fragment = ViewRecordFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_RECORD, record)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: ViewRecordViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewRecordViewModel::class.java)
        configManager = ConfigManager(activity)
        mRecord = arguments?.getParcelable(ARG_RECORD)
        editMode = mRecord != null
        initViews()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        if (Activity.RESULT_OK != resultCode) return
        when (requestCode) {
            ACTION_PHOTO, ACTION_GALLERY -> {
                imageReturnedIntent?.data?.let { processPhoto(it) }
            }
        }
    }

    /**
     * Process photo after taking from gallery of camera
     *
     * @param data Uri
     */
    private fun processPhoto(data: Uri) {
        val realPathFromURI = BitmapHelpers.getRealPathFromURI(activity, data)
        val bitmap = BitmapHelpers.prepareAndGetBitmap(
                realPathFromURI, null)
        if (bitmap != null) {
            imageUrl = realPathFromURI
            mRecord?.image = realPathFromURI
            imageView.setImageBitmap(bitmap)
            imageView.setOnLongClickListener { view ->
                val intent = Intent(activity, ViewPhotoActivity::class.java)
                val b = Bundle()
                b.putString(ViewPhotoActivity.ARG_PHOTO, view.tag as String)
                intent.putExtras(b)
                startActivity(intent)
                false
            }
            imageView.setOnClickListener { showPictureChooser() }
        }
    }

    private fun showPictureChooser() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Choose Image")
        builder.setItems(arrayOf<CharSequence>("Gallery", "Camera")
        ) { dialog, which ->
            when (which) {
                ACTION_GALLERY -> {
                    // GET IMAGE FROM THE GALLERY
                    val intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
                    val chooser = Intent.createChooser(intent, "Choose a Picture")
                    startActivityForResult(chooser, ACTION_GALLERY)
                }
                ACTION_PHOTO -> {
                    // GET IMAGE FROM THE CAMERA
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivityForResult(takePictureIntent, ACTION_PHOTO)
                    }
                }
                else -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun save() {
        mRecord = mRecord ?: UIRecord()
        mRecord?.id = mRecord?.id ?: UUID.randomUUID().toString()
        mRecord?.title = nameEditText.text.toString()
        val securedData = SecuredData(String.format(Locale.getDefault(), "%d", System.currentTimeMillis()), groupEditText.text.toString(), siteEditText.text.toString(), userEditText.text.toString(), ""
        )
        when (configManager?.encryption) {
            Encryption.RSA -> securedData.pass = SecureManager.encryptRSA(passEditText.text.toString())
            Encryption.AES -> securedData.pass = SecureManager.encryptAES(passEditText.text.toString())
            else -> securedData.pass = SecureManager.encryptAES(passEditText.text.toString())
        }
        imageUrl?.let { mRecord?.image = it }
        mRecord?.securedData = securedData
        if (mRecord?.isEmoty() == true) return
        viewModel.updateRecord(requireContext(), mRecord)
        mListener?.saveClick()
    }

    private fun initViews() {
        if (editMode) removeButton.visibility = View.VISIBLE

        imageView.setOnClickListener { showPictureChooser() }
        removeButton.setOnClickListener {
            mRecord?.id?.let { viewModel.removeRecord(requireContext(), it) }
            mListener?.removeClick(mRecord)
        }
        closeButton.setOnClickListener { save() }

        val image = mRecord?.image
        mRecord?.image?.let {
            if (it.isNotEmpty() && it.isNotBlank()) Picasso.get().load(File(it)).into(imageView)
        }
        if (!TextUtils.isEmpty(image)) {
            imageUrl = image
        }
        val name = mRecord?.title
        if (!TextUtils.isEmpty(name)) {
            nameEditText.setText(name)
        }
        val group = mRecord?.securedData?.group
        if (!TextUtils.isEmpty(group)) {
            groupEditText.setText(group)
        }
        val site = mRecord?.securedData?.site
        if (!TextUtils.isEmpty(site)) {
            siteEditText.setText(site)
        }
        val user = mRecord?.securedData?.user
        if (!TextUtils.isEmpty(user)) {
            userEditText.setText(user)
        } else {
            if (TextUtils.isEmpty(mRecord?.id)) {
                userEditText.setText(ConfigManager(requireActivity()).defaultUser)
            }
        }
        val pass = mRecord?.securedData?.pass
        if (!TextUtils.isEmpty(pass)) {
            when (configManager?.encryption) {
                Encryption.RSA -> passEditText.setText(SecureManager.decryptRSA(pass ?: ""))
                Encryption.AES -> passEditText.setText(SecureManager.decryptAES(pass ?: ""))
            }
        }
    }


}
