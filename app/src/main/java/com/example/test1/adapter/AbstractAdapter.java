package com.example.test1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Abstract Adapter Class
public abstract class AbstractAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> dataList;

    public AbstractAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(@NonNull VH holder, int position);

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateData(List<T> newData) {
        this.dataList = newData;
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return dataList.get(position);
    }

    public List<T> getDataList() {
        return dataList;
    }
}
