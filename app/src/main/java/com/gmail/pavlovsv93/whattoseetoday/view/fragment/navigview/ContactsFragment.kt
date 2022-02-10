package com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentContactsBinding
import com.gmail.pavlovsv93.whattoseetoday.model.AppStateContact
import com.gmail.pavlovsv93.whattoseetoday.model.Contact
import com.gmail.pavlovsv93.whattoseetoday.utils.showSnackBarAction
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.ContactViewModel
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.ContactsAdapter

const val REQUEST_CODE = 200
const val ARG_CONTACT_NUMBER = "ARG_CONTACT_NUMBER"
const val KEY_CONTACT_NUMBER = "KEY_CONTACT_NUMBER"

class ContactsFragment : Fragment() {

    interface OnClickContacts {
        fun onClickContact(contact: Contact)
    }

    private val adapter: ContactsAdapter = ContactsAdapter(object : OnClickContacts {
        override fun onClickContact(contact: Contact) {
            val data = Bundle()
            data.putParcelable(ARG_CONTACT_NUMBER, contact)
            parentFragmentManager.setFragmentResult(KEY_CONTACT_NUMBER, data)
            Toast.makeText(context, contact.name, Toast.LENGTH_SHORT).show()
        }
    })

    private val viewModel: ContactViewModel by lazy {
        ViewModelProvider(this).get(ContactViewModel::class.java)
    }

    private var _binding: FragmentContactsBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.recyclerviewContacts
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        viewModel.getData().observe(viewLifecycleOwner, Observer<AppStateContact> { state ->
            renderData(state)
        })

        checkPermissionContact()
    }

    private fun checkPermissionContact() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, android.Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getContact()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Необходимо получить доступ, для работы с контактной книгой")
                        .setPositiveButton("Предоставить") { _, _ ->
                            requestPermissions(
                                arrayOf(Manifest.permission.READ_CONTACTS),
                                REQUEST_CODE
                            )
                        }
                        .setNegativeButton("Отмена") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                else -> requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact()
                } else {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Отказ")
                            .setMessage("Вы не можите использовать полноценный функционал")
                            .setNegativeButton("Закрыть") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    }
                }
            }
        }

    }

    private fun getContact() {
        context?.let {
            val contactResolver: ContentResolver = it.contentResolver
            viewModel.getContact(contactResolver)
        }
    }

    private fun renderData(state: AppStateContact) {
        when (state) {
            AppStateContact.OnLoading -> binding.fragmentContactProgress.isVisible = true
            is AppStateContact.OnError -> {
                binding.fragmentContactText.isVisible = true
                view?.showSnackBarAction(
                    state.toString(),
                    getString(R.string.reload),
                    { getContact() })
            }
            is AppStateContact.OnSuccess -> {
                adapter.setContactsList(state.contactData)
                binding.recyclerviewContacts.isVisible = true
                binding.fragmentContactProgress.isVisible = false
                if (state.contactData?.size != 0) {
                    binding.fragmentContactText.isVisible = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = ContactsFragment()
    }
}
