package com.example.b1906342_nguyentantai_uocmo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.b1906342_nguyentantai_uocmo.R
import com.example.b1906342_nguyentantai_uocmo.apis.Constants
import com.example.b1906342_nguyentantai_uocmo.databinding.FragmentAddBinding
import com.example.b1906342_nguyentantai_uocmo.models.RequestAddWish
import com.example.b1906342_nguyentantai_uocmo.sharedpreferences.AppSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var mAppSharedPreferences: AppSharedPreferences
    private var idUser = ""
    private var fullName = ""
    private var content = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        mAppSharedPreferences = AppSharedPreferences(requireContext())
        idUser = mAppSharedPreferences.getIdUser("idUser").toString()

        binding.apply {
            btnSave.setOnClickListener{
                if(edtFullName.text.isNotEmpty() && edtContent.text.isNotEmpty())
                {
                    fullName = edtFullName.text.toString().trim()
                    content = edtContent.text.toString().trim()
                    addWish(fullName, content)
                }
            }
        }
        return binding.root
    }

    private fun addWish(fullName: String, content: String)
    {
       binding.progressBar.visibility = View.VISIBLE
       CoroutineScope(Dispatchers.IO).launch {
           withContext(Dispatchers.Main){
               val response = Constants.getInstance()
                   .addWish(RequestAddWish(idUser, fullName, content))
                   .body()
               if(response != null)
               {
                   if(response.success)
                   {
                       binding.progressBar.visibility = View.GONE
                       Toast.makeText(requireContext(),response.message, Toast.LENGTH_LONG).show()
                       requireActivity().supportFragmentManager.beginTransaction()
                           .replace(R.id.frame_layout,WishListFragment())
                           .commit()
                   }
                   else{
                       binding.progressBar.visibility = View.GONE
                       Toast.makeText(requireContext(), response.message, Toast.LENGTH_LONG).show()
                       requireActivity().supportFragmentManager.beginTransaction()
                           .replace(R.id.frame_layout,LoginFragment())
                           .commit()
                   }
               }
           }
       }

    }



}