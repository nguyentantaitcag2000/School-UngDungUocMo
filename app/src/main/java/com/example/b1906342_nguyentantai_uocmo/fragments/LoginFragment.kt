package com.example.b1906342_nguyentantai_uocmo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.b1906342_nguyentantai_uocmo.R
import com.example.b1906342_nguyentantai_uocmo.apis.Constants
import com.example.b1906342_nguyentantai_uocmo.databinding.FragmentLoginBinding
import com.example.b1906342_nguyentantai_uocmo.models.RequestRegisterOrLogin
import com.example.b1906342_nguyentantai_uocmo.sharedpreferences.AppSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAppSharedPreferences: AppSharedPreferences
    private var username = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        mAppSharedPreferences = AppSharedPreferences(requireContext())

        binding.apply {
            tvRegister.setOnClickListener{
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, RegisterFragment())
                    .commit()
            }
            btnLogin.setOnClickListener {
                if(edtUsername.text.isNotEmpty())
                {
                    username = edtUsername.text.toString().trim()
                    loginUser(username)
                }
            }
        }
        return binding.root
    }
    private fun loginUser(username: String){
        binding.apply {
            progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main){
                    val response = Constants.getInstance().loginUser(RequestRegisterOrLogin(username)).body()
                    if(response != null)
                    {
                        if(response.success)
                        {
                            mAppSharedPreferences.putIdUser("idUser", response.idUser!!)
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, WishListFragment())
                                .commit()
                            progressBar.visibility = View.GONE
                        }else{
                            tvMessage.text = response.message
                            tvMessage.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

}