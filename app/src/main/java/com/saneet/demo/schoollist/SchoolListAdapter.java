package com.saneet.demo.schoollist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneet.demo.R;
import com.saneet.demo.models.SchoolPreviewModel;

import java.util.ArrayList;
import java.util.List;

public class SchoolListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SCHOOL = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private final List<SchoolPreviewModel> list = new ArrayList<>();
    private final RecyclerViewEventListener ownerFragmentListener;
    private boolean showLoadingItem = false;

    public SchoolListAdapter(RecyclerViewEventListener ownerFragmentListener) {
        this.ownerFragmentListener = ownerFragmentListener;
    }

    public SchoolPreviewModel getItemAtIndex(int index) {
        return list.get(index);
    }

    public void clearList() {
        list.clear();
        showLoadingItem = false;
        notifyDataSetChanged();
    }

    public void addItems(List<SchoolPreviewModel> newList, boolean moreItemsIncoming) {
        //TODO debug this
        if (showLoadingItem) {
            int loadingItemIndex = list.size();
            notifyItemRemoved(loadingItemIndex);
        }
        int previousLastItemIndex = list.size() - 1;
        int newItemsCount = newList.size();
        if (moreItemsIncoming) {
            newItemsCount += 1;
        }
        list.addAll(newList);
        notifyItemRangeInserted(previousLastItemIndex + 1, newItemsCount);
        showLoadingItem = moreItemsIncoming;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SCHOOL) {
            return new SchoolViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.school_item_layout, parent, false));
        } else {
            return new LoadingViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recyclerview_loading_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_SCHOOL) {
            ((SchoolViewHolder) holder)
                    .setValues(list.get(position),
                            ownerFragmentListener,
                            position);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.getItemViewType() == VIEW_TYPE_LOADING) {
            ownerFragmentListener.onScrolledToLoadItem();
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + (showLoadingItem ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < list.size())
            return VIEW_TYPE_SCHOOL;
        else
            return VIEW_TYPE_LOADING;
    }
}

class SchoolViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView area;
    private final TextView grades;

    public SchoolViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        area = itemView.findViewById(R.id.area);
        grades = itemView.findViewById(R.id.grades);
    }

    public void setValues(SchoolPreviewModel model,
                          RecyclerViewEventListener clickListener,
                          int position) {
        name.setText(model.getName());
        area.setText(model.getArea());
        grades.setText(String.format("%s%s",
                itemView.getContext().getString(R.string.grades_prefix), model.getGrades()));
        itemView.setOnClickListener(v -> clickListener.onRecyclerViewItemClick(position));
    }
}

class LoadingViewHolder extends RecyclerView.ViewHolder {
    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}


