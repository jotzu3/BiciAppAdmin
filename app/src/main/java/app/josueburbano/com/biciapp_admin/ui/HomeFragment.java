package app.josueburbano.com.biciapp_admin.ui;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.josueburbano.com.biciapp_admin.R;
import app.josueburbano.com.biciapp_admin.datos.MyCustomAdapter;
import app.josueburbano.com.biciapp_admin.datos.modelos.Estacion;
import app.josueburbano.com.biciapp_admin.ui.ViewModels.EstacionViewModel;
import app.josueburbano.com.biciapp_admin.ui.ViewModels.EstacionViewModelFactory;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    List<Estacion> fetchedEstaciones;
    public EstacionViewModel viewModel;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel= ViewModelProviders.of(getActivity(), new EstacionViewModelFactory())
                .get(EstacionViewModel.class);
        viewModel.ObtenerEstaciones();
        viewModel.ObservarEstaciones().observe(getActivity(), new Observer<List<Estacion>>() {
            @Override
            public void onChanged(@Nullable List<Estacion> estaciones) {
                if(estaciones != null){
                    fetchedEstaciones = estaciones;
                    List<String> stringsList = new ArrayList<String>(fetchedEstaciones.size());
                    List<String> addInfList = new ArrayList<String>(fetchedEstaciones.size());
                    List<String> idsList = new ArrayList<String>(fetchedEstaciones.size());
                    for (Estacion estacion : fetchedEstaciones) {
                        stringsList.add(estacion.toString());
                        addInfList.add(estacion.addInfo());
                        idsList.add(estacion.getId());

                    }
                    //instantiate custom adapter
                    MyCustomAdapter adapter = new MyCustomAdapter(stringsList, addInfList, idsList, getActivity(),getActivity());

                    //handle listview and assign adapter
                    ListView lView = (ListView)getActivity().findViewById(R.id.lstView_reservas);
                    lView.setAdapter(adapter);
                }

            }
        });

    }




}