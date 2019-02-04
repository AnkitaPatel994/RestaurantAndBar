package com.electroweb.restaurantandbar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

class itemSearchListAdapter extends RecyclerView.Adapter<itemSearchListAdapter.Viewholder> implements Filterable {

    Context context;
    ArrayList<ListModel> itemListArray;
    ArrayList<ListModel> itemListFilter;

    public itemSearchListAdapter(Context context, ArrayList<ListModel> itemListArray) {

        this.context = context;
        this.itemListArray = itemListArray;
        itemListFilter = new ArrayList<>(itemListArray);
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist, parent, false);

        Viewholder viewHolder = new Viewholder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int i) {

        final String rs = context.getResources().getString(R.string.RS);

        DecimalFormat df = new DecimalFormat("0.00");
        final String itemId = itemListArray.get(i).getItemId();
        final String ItemCode = itemListArray.get(i).getItemCode();
        final String itemName = itemListArray.get(i).getItemName();
        final String itemPrice = df.format(Double.parseDouble(itemListArray.get(i).getMenuRate()));
        final String GSTRate = df.format(Double.parseDouble(itemListArray.get(i).getGSTRate()));

        holder.txtItem.setText(ItemCode +" - "+ itemName);
        holder.txtPrice.setText(rs+" "+itemPrice);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent i = new Intent(context,QtyActivity.class);
                i.putExtra("itemId",itemId);
                i.putExtra("ItemCode",ItemCode);
                i.putExtra("itemName",itemName);
                i.putExtra("itemPrice",itemPrice);
                i.putExtra("GSTRate",GSTRate);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemListArray.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtItem,txtPrice;
        ItemClickListener itemClickListener;
        public Viewholder(View itemView) {
            super(itemView);
            txtItem = (TextView) itemView.findViewById(R.id.txtItem);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<ListModel> filterdNames = new ArrayList<>();
            if(constraint == null || constraint.length() == 0)
            {
                filterdNames.addAll(itemListFilter);
            }
            else
            {
                String filterPatten = constraint.toString().toLowerCase().trim();
                for (ListModel name : itemListFilter) {
                    //if the existing elements contains the search input
                    if (name.getItemIdName().toLowerCase().contains(filterPatten)) {
                        //adding the element to filtered list
                        filterdNames.add(name);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterdNames;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            itemListArray.clear();
            itemListArray.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
