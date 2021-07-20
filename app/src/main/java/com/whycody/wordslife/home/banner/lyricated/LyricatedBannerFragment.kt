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
    private var lastPath: VectorDrawableCompat.VFullPath? = null
    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLyricatedBannerBinding.inflate(inflater)
        val pulseAnim = AnimationUtils.loadAnimation(context, R.anim.pulse_anim)
        binding.astronautIllustration.startAnimation(pulseAnim)
        vector = VectorChildFinder(context, R.drawable.il_lyricated, binding.lyricatedIllustration)
        lastPath = getRandomPath()
        lastPath!!.fillColor = getRandomColor(lastPath!!.fillColor)
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
                if(lastPath != null) getAnim(lastPath!!, ContextCompat.getColor(context!!, R.color.background_gray)).start()
                val randomPath = getRandomPath()
                lastPath = randomPath
                getAnim(randomPath).start()
                startAnimation(binding)
            }
        }
    }

    private fun getAnim(path: VectorDrawableCompat.VFullPath, color: Int = getRandomColor(path.fillColor)): ValueAnimator {
        val anim = ValueAnimator.ofArgb(path.fillColor, color).setDuration(1000)
        anim.addUpdateListener {
            refreshImageView(it, path)
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

    private fun getRandomPath(): VectorDrawableCompat.VFullPath {
        val randomPath = vector.findPathByName("letter${Random.nextInt(1, 10)}")
        return if(lastPath == null || randomPath != lastPath) randomPath else getRandomPath()
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