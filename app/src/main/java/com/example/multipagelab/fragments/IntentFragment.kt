package com.example.multipagelab.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.multipagelab.R
import com.example.multipagelab.databinding.FragmentIntentBinding
import java.io.File
import java.io.FileOutputStream

class IntentFragment : Fragment() {

    private var _binding: FragmentIntentBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.imageView.setImageURI(it)
            binding.btnShareInstagram.isEnabled = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnShareInstagram.setOnClickListener {
            selectedImageUri?.let { uri ->
                shareToInstagram(uri)
            } ?: Toast.makeText(requireContext(), R.string.choose_picture, Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareToInstagram(uri: Uri) {
        val instagramPackage = "com.instagram.android"

        if (isAppInstalled(instagramPackage)) {
            val imageFile = uriToFile(requireContext(), uri)
            val contentUri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.multipagelab.provider",
                imageFile
            )

            val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
                setDataAndType(contentUri, "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Instagram не установлен", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            requireContext().packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val file = File(context.cacheDir, "shared_image.jpg")
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()
        return file
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
