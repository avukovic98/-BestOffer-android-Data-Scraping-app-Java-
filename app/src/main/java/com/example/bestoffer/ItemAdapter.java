package com.example.bestoffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>{

    // kontekst
    Context context;

    // nizovi za naslove i autore
    ArrayList<String>  naziv,stanje, cena,pics,links;


    Activity activity;




    // konstruktor klase ItemAdapter
    public ItemAdapter(Context context, ArrayList naziv, ArrayList stanje, ArrayList cena,ArrayList pics,ArrayList links,Activity activity) {
        this.context = context;
        this.naziv = naziv;
        this.stanje = stanje;
        this.activity = activity;
        this.cena = cena;
        this.pics = pics;
        this.links=links;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.prodNaziv.setText(naziv.get(position));
        holder.prodStanje.setText(String.valueOf(stanje.get(position)));
        holder.prodCena.setText(String.valueOf(cena.get(position)));
        Glide.with(activity).load(pics.get(position)).fitCenter().into(holder.prodSlika);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //activity.finishAndRemoveTask();
                activity.finish();
                Intent intent= new Intent(activity,MainActivity.class);
                System.out.println(String.valueOf(links.get(position)));
                intent.putExtra("clicked",String.valueOf(links.get(position)));
                activity.startActivity(intent);



                // links.get(position)
        }});
    }

    @Override
    public int getItemCount() {
        return naziv.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView prodNaziv, prodCena,prodStanje;
        ImageView prodSlika;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // ovo su tekuci clanovi pri kreiranju RV iz liste autori i naslovi
            prodNaziv = itemView.findViewById(R.id.naziv);
            prodStanje=itemView.findViewById(R.id.stanje);
            prodSlika = itemView.findViewById(R.id.ItemImageView);
            prodCena = itemView.findViewById(R.id.cena);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
