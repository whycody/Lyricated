package com.whycody.wordslife.home.banner.lyricated

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentLyricatedBannerBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.random.Random

class LyricatedBannerFragment : Fragment() {

    private lateinit var binding: FragmentLyricatedBannerBinding
    private lateinit var vector: VectorChildFinder
    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLyricatedBannerBinding.inflate(inflater)
        val pulseAnim = AnimationUtils.loadAnimation(context, R.anim.pulse_anim)
        binding.astronautIllustration.startAnimation(pulseAnim)
        vector = VectorChildFinder(context, R.drawable.il_lyricated, binding.lyricatedIllustration)
        getDownLettersPaths().forEach { it.fillColor = getRandomColor(it.fillColor) }
        binding.lyricatedIllustration.invalidate()
        startAnimation(binding)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun startAnimation(binding: FragmentLyricatedBannerBinding) {
        job = CoroutineScope(IO).launch {
            delay(5000)
            CoroutineScope(Main).launch {
                getDownLettersPaths().forEach { getAnim(it).start() }
                startAnimation(binding)
            }
        }
    }

    private fun getAnim(path: VectorDrawableCompat.VFullPath): ValueAnimator {
        val currentColor = path.fillColor
        val randomColor = getRandomColor(currentColor)
        val anim = ValueAnimator.ofArgb(currentColor, randomColor).setDuration(1000)
        anim.addUpdateListener {
            if(path.pathName == FIRST_LETTER) refreshImageView(it, path)
            else setFillColor(it, path)
        }
        return anim
    }

    private fun refreshImageView(animator: ValueAnimator, path: VectorDrawableCompat.VFullPath) {
        setFillColor(animator, path)
        binding.lyricatedIllustration.invalidate()
    }

    private fun setFillColor(animator: ValueAnimator, path: VectorDrawableCompat.VFullPath) {
        path.fillColor = animator.animatedValue as Int
    }

    private fun getDownLettersPaths(): List<VectorDrawableCompat.VFullPath> {
        val pathsList = mutableListOf<VectorDrawableCompat.VFullPath>()
        for(i in 6..9) pathsList.add(vector.findPathByName("letter$i"))
        return pathsList
    }

    private fun getRandomColor(fillColor: Int): Int {
        val colors = mutableListOf(
            ContextCompat.getColor(activity?.applicationContext!!, R.color.astronaut_blue),
            ContextCompat.getColor(activity?.applicationContext!!, R.color.astronaut_yellow),
            ContextCompat.getColor(activity?.applicationContext!!, R.color.astronaut_red))
        colors.remove(fillColor)
        return colors[Random.nextInt(0, colors.size)]
    }

    companion object {
        private const val FIRST_LETTER = "letter6"
    }
}