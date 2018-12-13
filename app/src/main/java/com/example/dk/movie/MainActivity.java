package com.example.dk.movie;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    //뷰의 주소값을 담을 참조변수
    ListView list;

    //link 결과를 담을 ArrayList
    ArrayList<String> result_link_list;

    MovieAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list); //리스트 뷰 연결

        //결과 link ArrayList 객체 생성
        result_link_list = new ArrayList<>();

        list.setAdapter(movieAdapter); //어답터 설정

        ResultListListener listListener = new ResultListListener(); //검색 결과 리스트 뷰의 항목을 터치하면 반응하는 리스너
        list.setOnItemClickListener(listListener); //아이템 클릭 리스너 설정


        editText = (EditText)findViewById(R.id.editText);
        editText.setHint("검색어 입력");

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



    class MovieAdapter extends BaseAdapter{
        ArrayList<MovieItem> items = new ArrayList<>();

        //파라미터로 받은 아이템을 어댑터에 추가
        public void addItem(MovieItem item){
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MovieItemView view = null;
            if(convertView == null){
               view = new MovieItemView(getApplicationContext());
            }else{
                view = (MovieItemView)convertView;
            }

            MovieItem item = items.get(position);
            view.setTitle(item.getTitle());
            view.setPubDate(item.getPubDate());
            view.setDirector(item.getDirector());
            view.setActor(item.getActor());
            view.setUserRating(item.getUserRating());

            return view;
        }
    }





    //검색 결과 리스트 뷰의 항목을 터치하면 반응하는 리스너
    //OnItemClickListener 인터페이스를 구현한 리스너 클래스 생성
    private class ResultListListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //검색 결과의 position 번째 링크 주소를 가져온다.
            String site = result_link_list.get(position);

            //사이트를 띄운다.
            Uri uri = Uri.parse(site);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        }
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
                result_link_list.clear();

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


                movieAdapter  = new MovieAdapter();

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
                    NodeList link_list = item_tag.getElementsByTagName("link");

                    Element title_tag = (Element)title_list.item(0);
                    Element date_tag = (Element)date_list.item(0);
                    Element director_tag = (Element)director_list.item(0);
                    Element actor_tag = (Element)actor_list.item(0);
                    Element rating_tag = (Element)rating_list.item(0);
                    Element link_tag = (Element)link_list.item(0);

                    String title = title_tag.getTextContent();
                    String pubDate = date_tag.getTextContent();
                    String director = director_tag.getTextContent();
                    String actor = actor_tag.getTextContent();
                    float rating = Float.parseFloat(rating_tag.getTextContent());
                    String link = link_tag.getTextContent();
                    result_link_list.add(link);

                    movieAdapter.addItem(new MovieItem(title, pubDate, director, actor, rating/2));
                }

                //리스트뷰를 구성한다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                      ArrayAdapter<String> adapter = (ArrayAdapter<String>)list.getAdapter();
                        list.setAdapter(movieAdapter);
                        movieAdapter.notifyDataSetChanged();
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
