package com.example.Task41p;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> eventList;
    private EventListFragment fragment;

    public EventAdapter(List<Event> eventList, EventListFragment fragment) {
        this.eventList = eventList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        String displayText = event.getTitle() + " - " + event.getDate();
        holder.textView.setText(displayText);
        holder.itemView.setOnClickListener(v -> {
            fragment.onItemClicked(event);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateList(List<Event> newList){
        eventList = newList;
        notifyDataSetChanged();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}