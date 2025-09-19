package com.example.listycitylab3;

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

public class AddCityFragment extends DialogFragment{

    interface AddCityDialogListener{
        void addCity(City city);
        void editCity(City city, String editCity, String editProvince);
    }
    private AddCityDialogListener listener;

    // listener provides reusability of fragment
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof  AddCityDialogListener){
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    public static AddCityFragment newInstance(City city){
        Bundle args = new Bundle();
        args.putSerializable("city", city);

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // customize dialog and bind the views
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        Bundle args = getArguments();

        // if a list item is selected, get the city and index
        if (args != null){
            City city= (City) args.getSerializable("city");
            int index =  args.getInt("index"); // position in list

            // getting city and province names and auto filling the dialogue box
            if (city != null){
                editCityName.setText(city.getName());
                editProvinceName.setText(city.getProvince());
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("Edit a city")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Save Edit", (dialog, which) -> {
                        String cityName = editCityName.getText().toString();
                        String provinceName = editProvinceName.getText().toString();
                        city.setName(cityName);
                        city.setProvince(provinceName);
                        listener.editCity(city, cityName, provinceName);
                    })
                    .create();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("Add a city")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String cityName = editCityName.getText().toString();
                        String provinceName = editProvinceName.getText().toString();
                        listener.addCity(new City(cityName, provinceName));
                    })
                    .create();
        }
    }
}
