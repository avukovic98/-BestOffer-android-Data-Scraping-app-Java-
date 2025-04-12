package com.example.bestoffer;

import android.app.Activity;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Scraper implements   Runnable{
    int type;
    ArrayList<String> naziv,stanje, cena,pics,links;
    int currentPage;
    public static int max=1;
    TextView page;
    Activity activity;
    ItemAdapter customAdapter;
    OfferAdapter customAdapter1;
    RecyclerView rv;
    String text;
    private boolean stop=true;
    Thread process;
    String processName;

    public Scraper(ArrayList naziv,ArrayList stanje, ArrayList cena,ArrayList pics, ArrayList links,TextView page,Activity activity,ItemAdapter customAdapter,RecyclerView rv){
        this.naziv=naziv;
        this.stanje=stanje;
        this.cena=cena;
        this.pics=pics;
        this.links=links;
        this.page=page;
        this.activity=activity;
        this.customAdapter=customAdapter;
        this.rv=rv;
        processName="Scraping";
        process=new Thread(this,processName);

    }
    void setCurrentPage(int currentPage){
        this.currentPage=currentPage;
    }
    void setType(int type){
        this.type=type;
    }
    int getMax(int max){
        return max;
    }
    void setText(String text){
        this.text=text;
    }
    void start(){
        process.start();
    }
    void restart(){
        processName="Scraping";
        process=new Thread(this,processName);
        process.start();
    }
    void stop(){
        process.destroy();
    }

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
                    doc = Jsoup.connect("https://www.eponuda.com/uporedicene/" + String.valueOf(currentPage) + "?ep=" + text)
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




            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type==1) {
                        page.setText(String.valueOf(String.valueOf(currentPage)));
                        customAdapter = new ItemAdapter(activity, naziv, stanje, cena, pics, links, activity);
                        rv.setAdapter(customAdapter);
                        rv.setLayoutManager(new LinearLayoutManager(activity));
                        activity.invalidateOptionsMenu();
                    }
                    if(type==2) {
                        page.setText(String.valueOf(String.valueOf(currentPage)));
                        customAdapter1 = new OfferAdapter(activity,  pics, stanje, cena, links, activity);
                        rv.setAdapter(customAdapter1);
                        rv.setLayoutManager(new LinearLayoutManager(activity));
                        activity.invalidateOptionsMenu();
                    }


                    //  MainActivity.this.notifyAll();
                }
            });







        }

}
