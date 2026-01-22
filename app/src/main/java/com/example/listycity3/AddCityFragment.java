package com.example.listycity3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import androidx.fragment.app.FragmentManager;

public class AddCityFragment extends DialogFragment {
    private City cityToEdit;
    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City newCity, City oldCity);
    }
    private AddCityDialogListener listener;

    static AddCityFragment newInstance(City city) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        
        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + "must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle args = getArguments();

        boolean isEditing = false;
        if (args != null && args.getSerializable("city") != null) {
            cityToEdit = (City) args.getSerializable("city");
            isEditing = true;
            editCityName.setText(cityToEdit.getName());
            editProvinceName.setText(cityToEdit.getProvince());
        }

        final boolean editMode = isEditing;

        return builder
                .setView(view)
                .setTitle("Add or edit a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    City newCity = new City(cityName, provinceName);
                    if (editMode) {
                        listener.editCity(cityToEdit, newCity);
                    } else {
                        listener.addCity(newCity);
                    }
                })
                .create();
    }

}
