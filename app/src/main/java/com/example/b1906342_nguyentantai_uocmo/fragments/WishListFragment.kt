package com.example.b1906342_nguyentantai_uocmo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.b1906342_nguyentantai_uocmo.R
import com.example.b1906342_nguyentantai_uocmo.adapters.WishAdapter
import com.example.b1906342_nguyentantai_uocmo.databinding.FragmentWishListBinding
import com.example.b1906342_nguyentantai_uocmo.models.Wish
import com.example.b1906342_nguyentantai_uocmo.sharedpreferences.AppSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.b1906342_nguyentantai_uocmo.apis.Constants
import com.example.b1906342_nguyentantai_uocmo.models.RequestDeleteWish
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
 * Use the [WishListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WishListFragment : Fragment() {

    private lateinit var binding: FragmentWishListBinding
    private lateinit var mWishList: ArrayList<Wish>
    private lateinit var mWishAdapter: WishAdapter
    private lateinit var mAppSharedPreferences: AppSharedPreferences
    private var idUser = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishListBinding.inflate(layoutInflater, container, false)

        mAppSharedPreferences = AppSharedPreferences(requireActivity())
        idUser = mAppSharedPreferences.getIdUser("idUser").toString()

        mWishList = ArrayList()

        getWishList()

        binding.btnAdd.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, AddFragment())
                .commit()
        }
        return binding.root
    }
    private fun getWishList()
    {
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                val response = Constants.getInstance().getWishList().body()
                if(response != null)
                {
                    mWishList.addAll(response)
                    initAdapterAndSetLayout()
                }
            }
        }
    }
    private fun initAdapterAndSetLayout()
    {
        if(context == null)
        {
            return
        }
        mWishAdapter = WishAdapter(requireActivity(), mWishList, mAppSharedPreferences,
            object: WishAdapter.IClickItemWish{
                override fun onClickUpdate(idWish: String, fullName: String, content: String){
                    mAppSharedPreferences.putWish("idWish", idWish)
                    mAppSharedPreferences.putWish("fullName", fullName)
                    mAppSharedPreferences.putWish("content", content)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, UpdateFragment())
                        .commit()
                }
                override fun onClickRemove(idWish: String){
                    deleteWish(idWish)
                }

            })
        binding.rcvWishList.adapter = mWishAdapter
        binding.rcvWishList.layoutManager = LinearLayoutManager(requireActivity())
        binding.progressBar.visibility = View.GONE
    }

    private fun deleteWish(idWish: String){
//        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val response = Constants.getInstance().deleteWish(RequestDeleteWish(idUser,idWish))
                .body()
            // Chạy ở luồng chính -> để không bị lỗi
            withContext(Dispatchers.Main)
            {
                binding.progressBar.visibility = View.VISIBLE
                if(response != null)
                {
                    if(response.success)
                    {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_LONG).show()

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, WishListFragment())
                        .commit()

                    }else{
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