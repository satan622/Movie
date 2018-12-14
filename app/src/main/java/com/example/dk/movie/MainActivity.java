package com.example.dk.movie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    //검색어 입력창 참조변수
    EditText editText;

    //RecyclerView
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    //MovieItem을 담을 결과 리스트
    ArrayList<MovieItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        list = findViewById(R.id.list); //리스트 뷰 연결
        recyclerView = (RecyclerView)findViewById(R.id.list);

        //MovieItem을 담을 결과 ArrayList 생성
        items = new ArrayList<>();

        recyclerView.setAdapter(recyclerAdapter); //어댑터 설정

        //터치 이벤트 발생 시 누르고 뗄 때 한번만 인식하도록 하기위한 제스처디텍터
        final GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                //손으로 터치한 곳의 좌표를 토대로 해당 Item의 View를 가져옴
                View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                //터치한 곳의 View가 RecyclerView 안의 아이템이고 그 아이템의 View가 null이 아닌 Item의 View를 가져왔고
                //gestureDetector에서 한번만 누르면 true를 넘기게 구현했으므로 true가 넘어왔다면
                //터치된 곳의 position과 해당 위치의 데이터를 가져온다.
                if(childView != null && gestureDetector.onTouchEvent(motionEvent)){

                    int position = recyclerView.getChildAdapterPosition(childView);

                    MovieItem currentItem = items.get(position);
                    System.out.println(currentItem.getTitle());

                    //검색 결과의 position 번째 링크 주소를 가져온다.
                    String site = currentItem.getLink();

                    //사이트를 띄운다.
                    Uri uri = Uri.parse(site);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        };

        recyclerView.addOnItemTouchListener(onItemTouchListener); //리사이클러뷰에 터치 리스너 등록

        //검색 텍스트 입력 창
        editText = (EditText)findViewById(R.id.editText);
        editText.setHint("검색어 입력");

        //검색 버튼
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();

                NetworkThread thread=new NetworkThread(input);
                thread.start();
            }
        });
    }

    //데이터를 받아오는 스레드
    private class NetworkThread extends Thread {
        String keyword; //검색 키워드

        //네이버 오픈 API사용을 위한 client ID와 secret값
        String client_id = "hVnSd61hSFALdPXS2Wss";
        String client_secret = "sDr9UAd8Mi";


        public NetworkThread(String keyword) {
            this.keyword = keyword;
        }

        @Override
        public void run() {
            try{
                items.clear(); //검색할 때 마다 검색 결과 초기화

                //검색어 인코딩
                keyword = URLEncoder.encode(keyword, "UTF-8");
                //접속 주소
                String site = "https://openapi.naver.com/v1/search/movie.xml?query=" + keyword;

                //접속
                URL url = new URL(site);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                //요청 방식과 client_id, client_secret 값을 설정
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-Naver-Client-Id", client_id);
                conn.setRequestProperty("X-Naver-Client-Secret", client_secret);

                //데이터를 읽어온다.
                InputStream is = conn.getInputStream();
                //DOM 파서 생성
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(is);

                //최상위 루트태그를 가져온다.
                Element root = document.getDocumentElement();
                //item 태그 객체들을 가져온다.
                NodeList item_list = root.getElementsByTagName("item");


//                movieAdapter  = new MovieAdapter();
                recyclerAdapter = new RecyclerAdapter(items);

                //태그 개수만큼 반복
                for(int i=0; i<item_list.getLength(); i++){
                    //i번째 태그 객체를 가져온다.
                    Element item_tag = (Element)item_list.item(i);

                    // item 태그 내의 title 과 link 를 가져온다.
                    NodeList title_list = item_tag.getElementsByTagName("title");
                    NodeList date_list = item_tag.getElementsByTagName("pubDate");
                    NodeList director_list = item_tag.getElementsByTagName("director");
                    NodeList actor_list = item_tag.getElementsByTagName("actor");
                    NodeList rating_list = item_tag.getElementsByTagName("userRating");
                    NodeList image_list = item_tag.getElementsByTagName("image");
                    NodeList link_list = item_tag.getElementsByTagName("link");

                    Element title_tag = (Element)title_list.item(0);
                    Element date_tag = (Element)date_list.item(0);
                    Element director_tag = (Element)director_list.item(0);
                    Element actor_tag = (Element)actor_list.item(0);
                    Element rating_tag = (Element)rating_list.item(0);
                    Element image_tag = (Element)image_list.item(0);
                    Element link_tag = (Element)link_list.item(0);

                    String title = title_tag.getTextContent();
                    String pubDate = date_tag.getTextContent();
                    String director = director_tag.getTextContent();
                    String actor = actor_tag.getTextContent();
                    float rating = Float.parseFloat(rating_tag.getTextContent());
                    String image = image_tag.getTextContent();
                    String link = link_tag.getTextContent();

                    //검색결과 항목에 이미지가 있으면 섬네일 등록, 없으면 공백으로 처리
                    try {
                        URL imageUrl = new URL(image);

                        //Web에서 이미지를 가져온 뒤 ImageView에 지정할 Bitmap을 생성
                        HttpURLConnection urlConn = (HttpURLConnection)imageUrl.openConnection();
                        urlConn.setDoInput(true); //서버로부터 응답 수신
                        urlConn.connect();

                        InputStream imgIs = urlConn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(imgIs);

                        items.add(new MovieItem(title, pubDate, director, actor, rating/2, bitmap, link));

                    }catch (Exception e){
                        System.out.println("이미지URL 에러");
                        items.add(new MovieItem(title, pubDate, director, actor, rating/2, link));
                    }
                }

                //리사이클러뷰를 구성
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(recyclerAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
