package com.example.kuufapp_project_lab.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuufapp_project_lab.model.Product;
import com.example.kuufapp_project_lab.R;
import com.example.kuufapp_project_lab.util.HelperData;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<Product> products;
    private ProductClickListener listener;

    public ProductAdapter(ArrayList<Product> products, ProductClickListener listener){
        this.products = products;
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProductClickListener listener;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvMinMax;

        public ViewHolder(final View view, ProductClickListener listener){
            super(view);
            this.listener = listener;
            tvName = view.findViewById(R.id.tv_product_name);
            tvPrice = view.findViewById(R.id.tv_product_price);
            tvMinMax = view.findViewById(R.id.tv_product_max_min);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onProductClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_product, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(products.get(position).getName());
        holder.tvPrice.setText(HelperData.getPriceString(products.get(position).getPrice()));
        String player = products.get(position).getMinPlayer() + " - " + products.get(position).getMaxPlayer();
        holder.tvMinMax.setText(player);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface ProductClickListener{
        void onProductClicked(int position);
    }
}
