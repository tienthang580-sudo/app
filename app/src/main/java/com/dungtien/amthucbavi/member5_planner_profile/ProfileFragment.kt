package com.dungtien.amthucbavi.member5_planner_profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.databinding.FragmentProfileBinding
import com.dungtien.amthucbavi.member1_auth_home.AuthActivity
import com.dungtien.amthucbavi.member4_cooking_saved.ViewingHistoryActivity
import com.dungtien.amthucbavi.services.AuthService

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authService = AuthService.getInstance(requireContext())
        val currentUser = authService.currentUser

        if (currentUser != null) {
            binding.etProfileName.setText(currentUser.fullName)
            binding.etProfileEmail.setText(currentUser.email)
            binding.etProfilePhone.setText(currentUser.phone ?: "")
        }

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etProfileName.text.toString().trim()
            val phone = binding.etProfilePhone.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentUser != null) {
                DatabaseHelper.getInstance(requireContext()).updateProfile(currentUser.id, name, phone)
                val updatedUser = DatabaseHelper.getInstance(requireContext()).getUser(currentUser.id)
                if (updatedUser != null) {
                    authService.updateCurrentUser(updatedUser)
                }
                Toast.makeText(requireContext(), "Đã cập nhật hồ sơ thành công.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCallRes.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0835272818"))
            startActivity(intent)
        }

        binding.btnWebRes.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://nhahangdungtienbavi.com"))
            startActivity(intent)
        }

        binding.btnNavHistory.setOnClickListener {
            startActivity(Intent(requireContext(), ViewingHistoryActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            authService.logout()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
