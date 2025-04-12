package com.example.bestoffer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.Console;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.example.bestoffer.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Activity activity;
    Button search,leftPage,rightPage;
    ArrayList<String> naziv,stanje, cena,pics,links;
    SearchView searchText;
    private static String srch="Disk";
    private  static int max;
    private static int CurrentPage=1;
    TextView page;
    String current="";
    //private ActivityMainBinding binding;
    MainActivity saved;
    RecyclerView rv;

    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    ItemAdapter customAdapter;
    OfferAdapter customAdapter1;
    Scraper scraper;

    public Activity getActivity() {
        return activity;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;

        navigationView=findViewById(R.id.nav_view);
        drawerLayout=findViewById(R.id.my_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.openD,R.string.closeD);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv_item);
        //search=findViewById(R.id.search);
        searchText=findViewById(R.id.searchView);
        page=findViewById(R.id.page);
        leftPage=findViewById(R.id.LeftPage);
        rightPage=findViewById(R.id.RightPage);
       /* naziv = new ArrayList<String>();
        cena = new ArrayList<String>();
        stanje = new ArrayList<String>();
        pics = new ArrayList<String>();
        links = new ArrayList<String>();
        customAdapter = new ItemAdapter(this, naziv, stanje, cena, pics, links, MainActivity.this);*/


         Intent intent=getIntent();

        if(getIntent().getStringExtra("clicked")!=null) {
            String receive = String.valueOf(intent.getExtras().getString("clicked"));
            if (getIntent().getExtras().keySet().contains("clicked")) {
                getWebsite(receive,CurrentPage,2);
            }
        } else{  getWebsite(srch,CurrentPage,1);}

       /* search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kod za prelazak na novu aktivnost - add

                CurrentPage=1;
                srch=searchText.;
                getWebsite(srch,CurrentPage,1);

            }
        });*/
        leftPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kod za prelazak na novu aktivnost - add

                if(CurrentPage-1>=1) {CurrentPage--;
               getWebsite(srch,CurrentPage,1);}
            }
        });
        rightPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kod za prelazak na novu aktivnost - add

                if(CurrentPage+1<=max){ CurrentPage++;
                    getWebsite(srch,CurrentPage,1);}
            }
        });
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                 CurrentPage=1;
                 srch=String.valueOf(query);
                getWebsite(srch,CurrentPage,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        }

        );

    }

@Override
public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
        super.onBackPressed();}
}

private  void getWebsite(String text,int number,int type){



   new Thread(new Runnable() {

            @Override
            public void run() {
                naziv = new ArrayList<String>();
                cena = new ArrayList<String>();
                stanje = new ArrayList<String>();
                pics = new ArrayList<String>();
                links = new ArrayList<String>();
                int br;
                Document doc = null;

                if(type==1) {
                    try {
                        doc = Jsoup.connect("https://www.eponuda.com/uporedicene/" + String.valueOf(number) + "?ep=" + text)
                                .get();

                        Elements e = doc.getElementsByClass("product b-paging-product b-paging-product--vertical");
                        br = 0;
                        for (Element el : e) {
                            br++;
                            break;
                        }
                        ;
                        if (br > 0) {
                            //text.append((String)doc.text());

                            Elements pages = doc.getElementsByClass("paginationControl d40top");
                            if(pages.get(0).getElementsByTag("a").size()!=0) {
                                int lastindex = pages.get(0).getElementsByTag("a").size() - 2;
                                max = Integer.parseInt(pages.get(0).getElementsByTag("a").get(lastindex).text());
                            }
                            else max=1;
                            Elements myin = doc.getElementsByClass("product b-paging-product b-paging-product--vertical");


                            for (Element el : myin) {

                                    pics.add(el.getAllElements().get(1).getAllElements().get(1).getAllElements().get(1).getAllElements().get(1).getAllElements().get(1).attr("srcset"));
                                naziv.add(String.valueOf(el.children().get(0).children().get(0).children().get(1).children().text())); //naziv.add(String.valueOf(el.getAllElements().get(1).getAllElements().get(1).getAllElements().get(2).getAllElements().tagName("h3")));
                                cena.add(String.valueOf(el.children().get(1).children().get(0).text()));
                                stanje.add(String.valueOf(el.children().get(1).children().get(1).text()));
                                links.add("https://www.eponuda.com" + String.valueOf(el.children().get(0).children().get(0).children().get(0).attr("href")));
                                System.out.println("https://www.eponuda.com" + String.valueOf(el.children().get(0).children().get(0).children().get(0).attr("href")));



                            }

                            // startActivityFromMainThread();
                        }
                    } catch (IOException e) {
                    }

                }

                if(type==2){
                    try{
                        doc = Jsoup.connect(text).get();
                        //text.append((String)doc.text());
                        Elements offers = doc.getElementsByClass("c-table__row");




                        for(Element e:offers) {
                            if(e.className().equals("c-table__row")) {
                                System.out.println(e.children().get(0).children().get(0).children().get(0).children().get(0).children().get(0).children().get(0).attr("srcset"));
                               // System.out.println("https://www.eponuda.com" + String.valueOf(e.children().get(4).children().get(0).attr("href")));
                                System.out.println(String.valueOf(e.children().get(2).children().get(0).text()));
                                System.out.println(String.valueOf(e.children().get(3).children().get(0).text()));
                                pics.add(e.children().get(0).children().get(0).children().get(0).children().get(0).children().get(0).children().get(0).attr("srcset"));
                                //naziv.add(String.valueOf(el.getAllElements().get(1).getAllElements().get(1).getAllElements().get(2).getAllElements().tagName("h3")));
                                cena.add(String.valueOf(e.children().get(2).children().get(0).text()));
                                stanje.add(String.valueOf(e.children().get(3).children().get(0).text()));
                                links.add("https://www.eponuda.com" + String.valueOf(e.children().get(4).children().get(0).attr("href")));
                            }

                        }

                        // startActivityFromMainThread();
                    }
                    catch(IOException e){
                    }

                }




                 runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       if(type==1) {
                           page.setText(String.valueOf(String.valueOf(CurrentPage)));
                           customAdapter = new ItemAdapter(MainActivity.this, naziv, stanje, cena, pics, links, MainActivity.this);
                           rv.setAdapter(customAdapter);
                           rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                           invalidateOptionsMenu();

                                   }
                        if(type==2) {
                            page.setText(String.valueOf(String.valueOf(CurrentPage)));
                            customAdapter1 = new OfferAdapter(MainActivity.this,  pics, stanje, cena, links, MainActivity.this);
                            rv.setAdapter(customAdapter1);
                            rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            invalidateOptionsMenu();

                        }


                      //  MainActivity.this.notifyAll();
                    }
                });



            }
        }).start();
}

    /*public void startActivityFromMainThread(){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (MainActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
                finish();
            }
        });
    }*/

}