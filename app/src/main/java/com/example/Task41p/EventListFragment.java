package com.example.Task41p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventListFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;
    private AppDatabase db;
    private EditText titleInput;
    private EditText categoryInput;
    private EditText locationInput;
    private EditText dateInput;
    private Button addButton, updateButton, deleteButton;
    private Event selectedEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        titleInput = view.findViewById(R.id.titleInput);
        categoryInput = view.findViewById(R.id.categoryInput);
        locationInput = view.findViewById(R.id.locationInput);
        dateInput = view.findViewById(R.id.dateInput);
        addButton = view.findViewById(R.id.addButton);
        updateButton = view.findViewById(R.id.updateButton);
        deleteButton = view.findViewById(R.id.deleteButton);

        eventList = new ArrayList<>();
        adapter = new EventAdapter(eventList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        db = Room.databaseBuilder(getContext(), AppDatabase.class, "event-database").allowMainThreadQueries().build();
        loadEvents();

        addButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String category = categoryInput.getText().toString();
            String location = locationInput.getText().toString();
            String date = dateInput.getText().toString();

            if (title.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }

            if (date.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isPastDate(date)) {
                Toast.makeText(getContext(), "Please enter a right date (yyyy-MM-dd)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!title.isEmpty()) {
                if (selectedEvent == null) {
                    db.eventDao().insert(new Event(title, category, location, date));
                    titleInput.setText("");
                    categoryInput.setText("");
                    locationInput.setText("");
                    dateInput.setText("");
                    loadEvents();
                } else {
                    selectedEvent = null;
                    titleInput.setText("");
                    categoryInput.setText("");
                    locationInput.setText("");
                    dateInput.setText("");
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    addButton.setText("Add");
                }
            }
        });

        updateButton.setOnClickListener(v -> {
            if (selectedEvent != null) {
                String title = titleInput.getText().toString();
                String category = categoryInput.getText().toString();
                String location = locationInput.getText().toString();
                String date = dateInput.getText().toString();

                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (date.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isPastDate(date)) {
                    Toast.makeText(getContext(), "Please enter a right date (yyyy-MM-dd)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!title.isEmpty()) {
                    selectedEvent.setTitle(title);
                    selectedEvent.setCategory(category);
                    selectedEvent.setLocation(location);
                    selectedEvent.setDate(date);
                    db.eventDao().update(selectedEvent);
                    titleInput.setText("");
                    categoryInput.setText("");
                    locationInput.setText("");
                    dateInput.setText("");
                    selectedEvent = null;
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    addButton.setText("Add");
                    loadEvents();
                }
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (selectedEvent != null) {
                db.eventDao().delete(selectedEvent);
                titleInput.setText("");
                categoryInput.setText("");
                locationInput.setText("");
                dateInput.setText("");
                selectedEvent = null;
                updateButton.setEnabled(false);
                deleteButton.setEnabled(false);
                addButton.setText("Add");
                loadEvents();
            }
        });

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        return view;
    }

    private void loadEvents() {
        eventList = db.eventDao().getAllEvents();
        adapter.updateList(eventList);
    }

    public void onItemClicked(Event event) {
        selectedEvent = event;
        titleInput.setText(event.getTitle());
        categoryInput.setText(event.getCategory());
        locationInput.setText(event.getLocation());
        dateInput.setText(event.getDate());
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
        addButton.setText("Cancel");
    }
    private boolean isPastDate(String inputDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {
            Date enteredDate = sdf.parse(inputDate);
            Date today = sdf.parse(sdf.format(new Date()));
            return enteredDate.before(today);
        } catch (Exception e) {
            return true;
        }
    }

}
