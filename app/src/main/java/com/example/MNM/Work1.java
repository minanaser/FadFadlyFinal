package com.example.MNM;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;


/**
 * A simple {@link Fragment} subclass.
 */
public class Work1 extends Fragment {
    static final List<Double> list = new ArrayList<Double>();
View view;

    public Work1() {
        // Required empty public constructor
    }
    private Classifier mClassifier = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_work1, container, false);
        RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radios);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                FragmentTransaction fr=getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Work2());
                fr.commit();
                if(checkedId==R.id.first)
                {
                    list.add(3.0);
                }
                else if(checkedId==R.id.second){
                    list.add(2.0);
                }
                else if(checkedId==R.id.third){
                    list.add(1.0);
                }
                for (int i=0;i < SingleOrNotOutLayer.list.size();i++)
                {
                    Log.i("Value of emo element "+i, String.valueOf(SingleOrNotOutLayer.list.get(i)));

                }
                double m1=SingleOrNotOutLayer.list.get(0);
                double m2=SingleOrNotOutLayer.list.get(1);
                double m3=SingleOrNotOutLayer.list.get(2);
                double m4=SingleOrNotOutLayer.list.get(3);
                double m5=SingleOrNotOutLayer.list.get(4);
                double m6=SingleOrNotOutLayer.list.get(5);
                double m7=SingleOrNotOutLayer.list.get(6);
                double m8=SingleOrNotOutLayer.list.get(7);
                double m9=SingleOrNotOutLayer.list.get(8);
                double m10=SingleOrNotOutLayer.list.get(9);
                double m11=SingleOrNotOutLayer.list.get(10);
                double m12=SingleOrNotOutLayer.list.get(11);
                Random mRandom = new Random();
                Sample[] mSamples = new Sample[]{
                        new Sample( new double[]{m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12}), // should be in the setosa domain

                };
                StringBuilder sb = new StringBuilder("Samples:\n");
                for(Sample s : mSamples) {
                    sb.append(s.toString() + "\n");
                }
                AssetManager assetManager =getContext().getAssets() ;
                try {
                    mClassifier = (Classifier) weka.core.SerializationHelper.read(assetManager.open("emo3.model"));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    // Weka "catch'em all!"
                    e.printStackTrace();
                }
                Log.i("model emo load","Model emo Loaded");
                if(mClassifier==null){
                    Log.i("model not load","Model not Loaded");
                    return;
                }
                final Attribute attributeSepalLength = new Attribute("emo1");
                final Attribute attributeSepalWidth = new Attribute("emo2");
                final Attribute attributePetalLength = new Attribute("emo3");
                final Attribute attributePetalWidth = new Attribute("emo4");
                final Attribute attributePetalWidth2 = new Attribute("emo5");
                final Attribute attributePetalWidth3 = new Attribute("emo6");
                final Attribute attributePetalWidth4 = new Attribute("emo7");
                final Attribute attributePetalWidth5 = new Attribute("emo8");
                final Attribute attributePetalWidth6 = new Attribute("emo9");
                final Attribute attributePetalWidth7 = new Attribute("emo10");
                final Attribute attributePetalWidth8 = new Attribute("emo11");
                final Attribute attributePetalWidth9 = new Attribute("emo12");



                final List<String> classes = new ArrayList<String>() {
                    {
                        add("bad"); // cls nr 1
                        add("good"); // cls nr 2
                        add("excellent"); // cls nr 3
                    }
                };
                ArrayList<Attribute> attributeList = new ArrayList<Attribute>(2) {
                    {
                        add(attributeSepalLength);
                        add(attributeSepalWidth);
                        add(attributePetalLength);
                        add(attributePetalWidth);
                        add(attributePetalWidth2);
                        add(attributePetalWidth3);
                        add(attributePetalWidth4);
                        add(attributePetalWidth5);
                        add(attributePetalWidth6);
                        add(attributePetalWidth7);
                        add(attributePetalWidth8);
                        add(attributePetalWidth9);

                        Attribute attributeClass = new Attribute("target", classes);
                        add(attributeClass);
                    }
                };

                Instances dataUnpredicted = new Instances("TestInstances",
                        attributeList, 1);
                // last feature is target variable
                dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1);
                // create new instance: this one should fall into the setosa domain
                final Sample s = mSamples[mRandom.nextInt(mSamples.length)];
                DenseInstance newInstance = new DenseInstance(dataUnpredicted.numAttributes()) {
                    {
                        setValue(attributeSepalLength, s.features[0]);
                        setValue(attributeSepalWidth, s.features[1]);
                        setValue(attributePetalLength, s.features[2]);
                        setValue(attributePetalWidth, s.features[3]);
                        setValue(attributePetalWidth2, s.features[4]);
                        setValue(attributePetalWidth3, s.features[5]);
                        setValue(attributePetalWidth4, s.features[6]);
                        setValue(attributePetalWidth5, s.features[7]);
                        setValue(attributePetalWidth6, s.features[8]);
                        setValue(attributePetalWidth7, s.features[9]);
                        setValue(attributePetalWidth8, s.features[10]);
                        setValue(attributePetalWidth9, s.features[11]);
                    }
                };
                newInstance.setDataset(dataUnpredicted);


                try {
                    double result = mClassifier.classifyInstance(newInstance);
                    String className = classes.get(new Double(result).intValue());
                    String msg =  ", emo predicted: " + className ;
                    Self5.list.add(3,className);
                    Log.i("WEKA_TEST", msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });

        return view;
    }
    public class Sample {

        public double [] features;

        public Sample(double[] _features) {

            //  this.label = _label;
            this.features = _features;
        }

        @Override
        public String toString() {
            return

                    ", feat: " + Arrays.toString(features);
        }
    }
}