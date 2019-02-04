package com.electroweb.restaurantandbar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

class listAdapter extends RecyclerView.Adapter<listAdapter.Viewholder> {

    Context context;
    ArrayList<HashMap<String, String>> listArray;
    View v;
    DecimalFormat df;

    static ArrayList<String> ITEMIDArray = new ArrayList<>();
    static ArrayList<String> ITEMCODEArray = new ArrayList<>();
    static ArrayList<String> ITEMNAMEArray = new ArrayList<>();
    static ArrayList<String> RATEArray = new ArrayList<>();
    static ArrayList<String> QTYArray = new ArrayList<>();
    static ArrayList<String> AMOUNTArray = new ArrayList<>();
    static ArrayList<String> GSTRATEArray = new ArrayList<>();

    public listAdapter(Context context, ArrayList<HashMap<String, String>> listArray) {
        this.context = context;
        this.listArray = listArray;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int i) {
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemorderlist, parent, false);

        Viewholder viewHolder = new Viewholder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int i) {

        df = new DecimalFormat("0.00");
        String rs = context.getResources().getString(R.string.RS);
        final String id = listArray.get(i).get("ID");
         String itemid = listArray.get(i).get("ITEMID");
         String itemcode = listArray.get(i).get("ITEMCODE");
        final String itemname = listArray.get(i).get("ITEMNAME");
        final String rate = listArray.get(i).get("RATE");
        final String qty = listArray.get(i).get("QTY");
        final String amount = listArray.get(i).get("AMOUNT");
         String gstrate = listArray.get(i).get("GSTRATE");

        holder.tvOIName.setText(itemname);
        holder.tvOQty.setText(qty);
        holder.tvOPrice.setText(rs+" "+df.format(Double.parseDouble(amount)));

        ITEMIDArray.add(itemid);
        ITEMCODEArray.add(itemcode);
        ITEMNAMEArray.add(itemname);
        RATEArray.add(rate);
        QTYArray.add(qty);
        AMOUNTArray.add(amount);
        GSTRATEArray.add(gstrate);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,UDActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("itemname",itemname);
                intent.putExtra("rate",rate);
                intent.putExtra("qty",qty);
                intent.putExtra("amount",amount);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listArray.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView tvOQty,tvOIName,tvOPrice;

        public Viewholder(View itemView) {
            super(itemView);

            tvOQty = (TextView)itemView.findViewById(R.id.tvOQty);
            tvOIName = (TextView)itemView.findViewById(R.id.tvOIName);
            tvOPrice = (TextView)itemView.findViewById(R.id.tvOPrice);

        }
    }
}
