package com.example.fridgerec.activities.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridgerec.model.EntryItem;

import java.util.List;

public class InventoryItemsAdapter extends RecyclerView.Adapter<InventoryItemsAdapter.ViewHolder> {
  public static final String TAG = "InventoryItemsAdapter";

  private Context context;
  private List<EntryItem> inventoryItems;

  public InventoryItemsAdapter(Context context, List<EntryItem> inventoryItems) {
    this.context = context;
    this.inventoryItems = inventoryItems;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
