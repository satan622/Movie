package com.example.dk.movie;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.net.URI;
import java.util.Date;

class MovieItemView extends LinearLayout {
    ImageView imageView;
    TextView title;
    TextView pubDate;
    TextView director;
    TextView actor;
    RatingBar userRating;


    public MovieItemView(Context context) {
        super(context);
        init(context);
    }

    public MovieItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item, this, true);

        title = (TextView)findViewById(R.id.title);
        pubDate = (TextView)findViewById(R.id.pudDate);
        director = (TextView)findViewById(R.id.director);
        actor = (TextView)findViewById(R.id.actor);
        userRating = (RatingBar)findViewById(R.id.userRating);
        imageView = (ImageView)findViewById(R.id.image);
    }

    public void setTitle(String t){
        title.setText(Html.fromHtml(t));
    }
    public void setDirector(String d){
        director.setText(d);
    }
    public void setActor(String a){
        actor.setText(a);
    }
    public void setPubDate(String pd){
        pubDate.setText(pd);
    }
    public void setUserRating(float rating){
        userRating.setRating(rating);
    }
    public void setImageView(Bitmap bm){
        imageView.setImageBitmap(bm);
    }

}
