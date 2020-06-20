package com.example.MNM;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleOrNotOutLayer extends Fragment {
    static final List<Double> list = new ArrayList<Double>();
View view;
    public SingleOrNotOutLayer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_single_or_not_out_layer, container, false);



        RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radios);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected


           if(checkedId==R.id.single)
                   // Log.d(TAG, "A");
               { FragmentTransaction fr=getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new SingleLoveOutLayer());
                fr.commit();}
                else if(checkedId==R.id.married)
                  //  Log.d(TAG, "B");
                { FragmentTransaction fr=getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container,new Married1());
                    fr.commit();
              //  list.add(0,-1.0);list.add(1,-1.0);list.add(2,-1.0);list.add(3,-1.0);list.add(4,-1.0);list.add(5,-1.0);
list.add(-1.0);                    list.add(-1.0);list.add(-1.0);    list.add(-1.0);  list.add(-1.0); list.add(-1.0);



                }
            }
        });

        return view;
    }

}
