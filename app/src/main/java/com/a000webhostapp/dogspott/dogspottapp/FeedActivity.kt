package com.a000webhostapp.dogspott.dogspottapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a000webhostapp.dogspott.dogspottapp.retro.ConnectionAdapter
import com.a000webhostapp.dogspott.dogspottapp.retro.models.Dog
import com.a000webhostapp.dogspott.dogspottapp.retro.models.SimpleResponse
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

    fun like(dog: Dog, v: View) {
        val previus = dog.likes
        dog.likes = dog.likes?.toInt()?.plus(1)?.toString() ?: "0"
        v.findViewById<TextView>(R.id.lblLikes).text = dog.likes
        val se = {v.findViewById<TextView>(R.id.lblLikes).text = previus}
        if (State.isNetworkAvailable(this, main_view, {like(dog,v)}, se))
            disposable.add(
                ConnectionAdapter.service
                    .like(Properties.getProperty(this, Properties.USER_KEY), dog.idDog!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onLike){e -> ConnectionAdapter.handleError(e, main_view, se){like(dog,v)}}
            )
    }

    fun onLike(reponse: SimpleResponse) {

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
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_gray)
                        .into(v.findViewById(R.id.image_profile))
                    v.findViewById<ImageView>(R.id.image_profile).setOnClickListener {
                        val intent = Intent(this@FeedActivity, DetailsActivity::class.java)
                        intent.putExtra(DetailsActivity.DOG_ID, dog!!.idDog!!)
                        startActivity(intent)
                    }
                    v.findViewById<TextView>(R.id.lblLikes).text = it.likes
                    v.findViewById<ImageButton>(R.id.btnLike).setOnClickListener { like(dog!!, v) }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_feed, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_logout) {
            AlertDialog.Builder(this)
                .setTitle(R.string.action_logout)
                .setMessage(R.string.text_confirm_logout)
                .setPositiveButton(R.string.action_aceptar) { _, _ ->
                    Properties.clearProperties(this)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                .setNegativeButton(R.string.action_cancelar, null)
                .show()
        }
        return super.onOptionsItemSelected(item)
    }
}
