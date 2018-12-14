package com.example.dk.movie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    ArrayList<MovieItem> items;

    //Adapter 초기화 및 생성자로 받은 데이터기반으로 Adapter 내 데이터 세팅
    public RecyclerAdapter(ArrayList<MovieItem> items) {
        this.items = items;
    }

    //ViewHolder가 초기화 될 때 혹은 ViewHolder를 초기화 할 때 실행되는 메서드
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Context를 부모로 부터 받아와서
        Context context = parent.getContext() ;
        //받은 Context를 기반으로 LayoutInflater를 생성
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //생성된 LayoutInflater로 어떤 Layout을 가져와서 어떻게 View를 그릴지 결정
        View studentView = layoutInflater.inflate(R.layout.list_item, parent, false);
        //View 생성 후, 이 View를 관리하기위한 ViewHolder를 생성
        ViewHolder viewHolder = new ViewHolder(studentView);
        //생성된 ViewHolder를 OnBindViewHolder로 넘겨줌
        return viewHolder;
    }

    //RecyclerView의 Row 하나하나를 구현하기위해 Bind 될 때
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //RecyclerView에 들어갈 데이터(items)를 기반으로 row 생성시 해당 row의 MovieItem 가져온다.
        MovieItem movieItem = items.get(position);

        //넘겨받은 ViewHolder의 Layout에 있는 View들을 설정
        ImageView imageView = viewHolder.imageView;
        TextView title = viewHolder.title;
        TextView pubDate = viewHolder.pubDate;
        TextView director = viewHolder.director;
        TextView actor = viewHolder.actor;
        RatingBar userRating = viewHolder.userRating;

        imageView.setImageBitmap(movieItem.getImage());
        title.setText(Html.fromHtml(movieItem.getTitle()));
        pubDate.setText(movieItem.getPubDate());
        pubDate.setText(movieItem.getPubDate());
        director.setText(movieItem.getDirector());
        actor.setText(movieItem.getActor());
        userRating.setRating(movieItem.getUserRating());
    }

    //items에 들어있는 movieItem 개수 counting
    @Override
    public int getItemCount() {
        return items.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView title;
    public TextView pubDate;
    public TextView director;
    public TextView actor;
    public RatingBar userRating;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        title = (TextView)itemView.findViewById(R.id.title);
        pubDate = (TextView)itemView.findViewById(R.id.pudDate);
        director = (TextView)itemView.findViewById(R.id.director);
        actor = (TextView)itemView.findViewById(R.id.actor);
        userRating = (RatingBar)itemView.findViewById(R.id.userRating);
        imageView = (ImageView)itemView.findViewById(R.id.image);
    }
}
