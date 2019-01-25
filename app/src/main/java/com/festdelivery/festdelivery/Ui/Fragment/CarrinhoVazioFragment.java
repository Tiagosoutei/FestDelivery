package com.festdelivery.festdelivery.Ui.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.festdelivery.festdelivery.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarrinhoVazioFragment extends Fragment {


    public CarrinhoVazioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carrinho_vazio, container, false);
    }

}
