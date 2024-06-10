package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RandomUserService service;
    private Button btnNewLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://randomuser.me/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(RandomUserService.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Erro ao carregar o mapa", Toast.LENGTH_SHORT).show();
        }

        btnNewLocation = findViewById(R.id.btn_new_location);
        btnNewLocation.setOnClickListener(v -> buscarLocalizacao());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        buscarLocalizacao();
    }

    private void buscarLocalizacao() {
        service.getRandomUser().enqueue(new Callback<RandomUserResponse>() {
            @Override
            public void onResponse(Call<RandomUserResponse> call, Response<RandomUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RandomUserResponse.Result result = response.body().results.get(0);
                    double latitude = Double.parseDouble(result.location.coordinates.latitude);
                    double longitude = Double.parseDouble(result.location.coordinates.longitude);
                    LatLng location = new LatLng(latitude, longitude);

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(location).title("Random User Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 0));
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao obter dados do usuário", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RandomUserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro na chamada da API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}