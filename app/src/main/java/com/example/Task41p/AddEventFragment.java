package com.example.Task41p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEventFragment extends Fragment {

    private EditText titleInput;
    private EditText categoryInput;
    private EditText locationInput;
    private EditText dateInput;
    private Button addButton;
    private AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        titleInput = view.findViewById(R.id.titleInput);
        categoryInput = view.findViewById(R.id.categoryInput);
        locationInput = view.findViewById(R.id.locationInput);
        dateInput = view.findViewById(R.id.dateInput);
        addButton = view.findViewById(R.id.addButton);

        db = Room.databaseBuilder(getContext(), AppDatabase.class, "event-database")
                .allowMainThreadQueries()
                .build();

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

            db.eventDao().insert(new Event(title, category, location, date));

            titleInput.setText("");
            categoryInput.setText("");
            locationInput.setText("");
            dateInput.setText("");

        });

        return view;
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