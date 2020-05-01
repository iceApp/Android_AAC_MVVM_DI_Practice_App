package com.example.meditation.view.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_main.*
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.meditation.viewmodel.MainViewModel
import com.example.meditation.R
import com.example.meditation.databinding.FragmentMainBinding
import net.minpro.meditation.PlayStatus

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_main,container,false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = activity
        }
        viewModel.initParameters()
        observeViewModel()
    }

    private fun observeViewModel() {
//        viewModel.txtLevel.observe(viewLifecycleOwner, Observer { levelTxt ->
//            txtLevel.text = levelTxt
//        })
//
//        viewModel.txtTheme.observe(viewLifecycleOwner, Observer { themeTxt ->
//            txtTheme.text = themeTxt
//        })
//
//        viewModel.displayTimeSeconds.observe(viewLifecycleOwner, Observer { displayTime ->
//            txtTime.text = displayTime
//        })
//
//        viewModel.msgUpperSmall.observe(viewLifecycleOwner, Observer { upperTxt ->
//            txtMsgUpperSmall.text = upperTxt
//        })
//
//        viewModel.msgLowerLarge.observe(viewLifecycleOwner, Observer { lowerTxt ->
//            txtMsgLowerLarge.text = lowerTxt
//        })

        viewModel.playStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status){
                PlayStatus.BEFORE_START -> btnPlay.setBackgroundResource(R.drawable.button_play)
            }
        })

        viewModel.themePicFileResId.observe(viewLifecycleOwner, Observer { themePicResId ->
            loadBackgroundImage(this,themePicResId,meditation_screen)

        })
    }

    private fun loadBackgroundImage(mainFragment: MainFragment, themePicResId: Int?, meditation_screen: ConstraintLayout?) {
        Glide.with(mainFragment).load(themePicResId).into(object: SimpleTarget<Drawable>(){
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                meditation_screen?.background = resource
            }
        })
    }
}
