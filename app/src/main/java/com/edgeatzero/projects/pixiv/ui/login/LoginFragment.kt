package com.edgeatzero.projects.pixiv.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.observe
import androidx.viewpager.widget.ViewPager
import com.edgeatzero.library.base.BaseFragment
import com.edgeatzero.library.ext.activityViewModels
import com.edgeatzero.projects.pixiv.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val binding by binding()

    private val model by activityViewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ButtonRegister.setOnClickListener {
            (binding.root.parent as? ViewPager)?.currentItem = 1
        }
        binding.TextInputEditTextUsername.addTextChangedListener(afterTextChanged = {
            model.postUsername(it.toString())
        })
        binding.TextInputEditTextPassword.addTextChangedListener(afterTextChanged = {
            model.postPassword(it.toString())
        })
        binding.TextInputEditTextPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) model.action()
            false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.model = model
        model.usernameError.observe(viewLifecycleOwner) {
            binding.TextInputLayoutUsername.error = if (it.enable) getString(it.message) else null
        }
        model.passwordError.observe(viewLifecycleOwner) {
            binding.TextInputLayoutPassword.error = if (it.enable) getString(it.message) else null
        }
    }

}
