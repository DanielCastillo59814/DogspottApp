package com.a000webhostapp.dogspott.dogspottapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a000webhostapp.dogspott.dogspottapp.retro.ConnectionAdapter
import com.a000webhostapp.dogspott.dogspottapp.retro.models.Dog
import com.a000webhostapp.dogspott.dogspottapp.utilities.Properties
import com.a000webhostapp.dogspott.dogspottapp.utilities.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {
    val feedAdapter = FeedAdapter()
    val disposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        setSupportActionBar(toolbar)
        list_feed.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FeedActivity)
            adapter = feedAdapter
        }
        swipe_refresh_layout.setOnRefreshListener {
            getFeed()
        }
        swipe_refresh_layout.isRefreshing = true
        getFeed()
    }

    fun getFeed() {
        val se = {swipe_refresh_layout.isRefreshing = false}
        if (State.isNetworkAvailable(this, main_view, this::getFeed, se))
            disposable.add(
                ConnectionAdapter.service
                    .getFeed(Properties.getProperty(this@FeedActivity, Properties.USER_KEY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onGetFeed){e -> ConnectionAdapter.handleError(e, main_view, se, this::getFeed)}
            )
    }

    fun onGetFeed(response: List<Dog>) {
        feedAdapter.updateItems(response)
        swipe_refresh_layout.isRefreshing = false
    }

    inner class FeedAdapter: RecyclerView.Adapter<FeedAdapter.ViewHolder>(){
        val EMPTY_LIST = 0
        val NORMAL = 1
        var feed: List<Dog> = ArrayList()

        fun updateItems(dogs: List<Dog>) {
            feed = dogs
            notifyDataSetChanged()
        }

        override fun getItemViewType(position: Int): Int {
            return if(feed.isEmpty())EMPTY_LIST else NORMAL
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            when(getItemViewType(position)){
                EMPTY_LIST -> holder.v.findViewById<TextView>(R.id.text_empty).text = getString(R.string.text_no_feed)
                NORMAL -> {
                    holder.dog = feed[position]
                    holder.init()
                }
            }
        }

        override fun getItemCount(): Int = if(feed.isEmpty())1 else feed.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = when(viewType){
                EMPTY_LIST -> LayoutInflater.from(parent.context).inflate(R.layout.viewholder_empty,parent,false)
                else -> LayoutInflater.from(parent.context).inflate(R.layout.item_feed,parent,false)
            }
            return ViewHolder(view)
        }

        inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v){
            var dog: Dog? = null
            fun init(){
                dog?.let{
                    v.findViewById<TextView>(R.id.text_name).text = it.name
                    GlideApp.with(this@FeedActivity)
                        .load(it.image)
                        .placeholder(R.drawable.placeholder_gray)
                        .into(v.findViewById(R.id.image_profile))

                }
            }
        }
    }
}
