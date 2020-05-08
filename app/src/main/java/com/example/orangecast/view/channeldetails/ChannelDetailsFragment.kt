package com.example.orangecast.view.channeldetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orangecast.App
import com.example.orangecast.R
import com.example.orangecast.databinding.FragmentChannelDetailsBinding
import com.example.orangecast.entity.Artist
import com.example.orangecast.entity.Feed
import com.example.orangecast.entity.ViewEvent
import com.example.orangecast.view.BaseFragment
import com.example.orangecast.view.episodes.EpisodesAdapter
import com.example.orangecast.view.snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_channel_details.*
import kotlinx.android.synthetic.main.view_channel_details_top.view.*
import javax.inject.Inject

class ChannelDetailsFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: ChannelDetailsViewModel
    private lateinit var binding: FragmentChannelDetailsBinding
    private val args: ChannelDetailsFragmentArgs by navArgs()
    private val episodesAdapter = EpisodesAdapter { episode ->

    }

    override fun inject() {
        App.appComponent(context)?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChannelDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun initView() {
        initButtons()
        initRefreshing()
        initEpisodesList()

        subscribeToViewModel()
    }

    private fun initButtons() {
        binding.toolbarView.backButton.setOnClickListener { onBackPressed() }
    }

    private fun initRefreshing() {
        if (context == null) return
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context!!, R.color.colorGradientDarkEnd))
        binding.swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorAccent))
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.getChannelDetails() }
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun initEpisodesList() {
        binding.episodesListRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = episodesAdapter
        }
    }

    private fun subscribeToViewModel() {
        viewModel.getEventLiveData().subscribeToEvent()
        viewModel.setFeedUrl(args.artistFeedUrl)
        viewModel.getChannelDetails()
    }

    override fun onProgress(event: ViewEvent.Progress<*>) {
        binding.channelListProgress.root.visibility = if (event.inProgress) View.VISIBLE else View.GONE
        if (!event.inProgress) binding.swipeRefreshLayout.isRefreshing  = false
    }

    override fun onError(event: ViewEvent.Error<*>) {
        snackbar(binding.rootContainer, event.message)
    }

    override fun onData(event: ViewEvent.Data<*>) {
        when (event.data) {
            is Artist -> {
                showChannelDetails(event.data)
                showChannelFeed(event.data.feed)
            }
        }
    }

    private fun showChannelDetails(channel: Artist) {
        Picasso.get().load(channel.artworkUrl100).into(header?.author_image)
        binding.headerView.authorTitle.text = channel.collectionName
        binding.headerView.authorName.text = channel.artistName
    }

    private fun showChannelFeed(feed: Feed?) {
        header?.author_description?.text = feed?.description
        episodesAdapter.setList(feed?.episodes ?: listOf())
    }

}