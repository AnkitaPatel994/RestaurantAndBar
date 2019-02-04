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

class itemListAdapter extends RecyclerView.Adapter<itemListAdapter.Viewholder> {

    Context context;
    ArrayList<ListModel> itemListArray;
    View v;

    public itemListAdapter(Context context,ArrayList<ListModel> itemListArray) {

        this.context = context;
        this.itemListArray = itemListArray;

    }

    public Viewholder onCreateViewHolder(ViewGroup parent, int i) {

        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist, parent, false);

        Viewholder viewHolder = new Viewholder(v);
        return viewHolder;
    }

    public void onBindViewHolder(Viewholder holder, int i) {

        final String rs = context.getResources().getString(R.string.RS);

        DecimalFormat df = new DecimalFormat("0.00");
        final String itemId = itemListArray.get(i).getItemId();
        final String ItemCode = itemListArray.get(i).getItemCode();
        final String itemName = itemListArray.get(i).getItemName();
        final String itemPrice = df.format(Double.parseDouble(itemListArray.get(i).getMenuRate()));
        final String GSTRate = df.format(Double.parseDouble(itemListArray.get(i).getGSTRate()));

        holder.txtItem.setText(itemName);
        holder.txtPrice.setText(rs+" "+itemPrice);

        final String CatId = itemListArray.get(i).getCatId();

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context,QtyActivity.class);
                i.putExtra("itemId",itemId);
                i.putExtra("ItemCode",ItemCode);
                i.putExtra("itemName",itemName);
                i.putExtra("itemPrice",itemPrice);
                i.putExtra("GSTRate",GSTRate);
                i.putExtra("CatId",CatId);
                context.startActivity(i);

            }
        });

    }

    public int getItemCount() {
        return itemListArray.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView txtItem,txtPrice;

        public Viewholder(View itemView) {
            super(itemView);

            txtItem = (TextView) itemView.findViewById(R.id.txtItem);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);

        }
    }
}
