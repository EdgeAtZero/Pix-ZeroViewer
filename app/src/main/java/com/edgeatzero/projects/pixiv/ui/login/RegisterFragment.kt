package com.edgeatzero.projects.pixiv.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.observe
import androidx.viewpager.widget.ViewPager
import com.edgeatzero.library.base.BaseFragment
import com.edgeatzero.library.ext.activityViewModels
import com.edgeatzero.projects.pixiv.databinding.FragmentRegisterBinding

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override val binding by binding()

    private val model by activityViewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ButtonRegister.setOnClickListener {
            (binding.root.parent as? ViewPager)?.currentItem = 0
        }
        binding.TextInputEditTextNickname.addTextChangedListener(afterTextChanged = {
            model.postNickname(it.toString())
        })
        binding.TextInputEditTextNickname.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) model.action()
            false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.model = model
        model.nicknameError.observe(viewLifecycleOwner) {
            binding.TextInputLayoutNickname.error = if (it.enable) getString(it.message) else null
        }
    }

}
