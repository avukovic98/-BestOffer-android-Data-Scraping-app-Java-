package com.example.bestoffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder>{

    // kontekst
    Context context;

    // nizovi za naslove i autore
    ArrayList<String> logos, stanje, cena,links;


    Activity activity;



    // konstruktor klase OfferAdapter
    public OfferAdapter(Context context, ArrayList logos, ArrayList stanje, ArrayList cena, ArrayList links, Activity activity) {
        this.context = context;
        this.logos = logos;
		this.stanje = stanje;
        this.cena = cena;
        this.links = links;
        this.activity = activity;
        
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.offer_card, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(activity).load(logos.get(position)).override(1000,1000).fitCenter().into(holder.prodLogos);
		holder.prodStanje.setText(String.valueOf(stanje.get(position)));
        holder.prodCena.setText(String.valueOf(cena.get(position)));
   


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.get(position)));
                activity.startActivity(browserIntent);

        }});
    }

    @Override
    public int getItemCount() {
        return logos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView prodStanje, prodCena;
        ImageView prodLogos;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View offerView) {
            super(offerView);

            // ovo su tekuci clanovi pri kreiranju RV iz liste autori i naslovi
            prodLogos = offerView.findViewById(R.id.logo);
            prodStanje = offerView.findViewById(R.id.offer_stanje);
            prodCena=offerView.findViewById(R.id.offer_cena);
            mainLayout = offerView.findViewById(R.id.offerLayout);
        }
    }
}
