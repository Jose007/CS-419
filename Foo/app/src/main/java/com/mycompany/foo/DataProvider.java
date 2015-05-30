package com.mycompany.foo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jose on 5/29/2015.
 */
public class DataProvider {
    public static HashMap<String,List<String>> getInfo(){
        HashMap<String,List<String>> category_details = new  HashMap<String,List<String>>( );
        List<String> App = new ArrayList<String>();
        App.add("Stove");
        App.add("Microwave");
        App.add("Toaster");
        App.add("Frig");

        List<String> Garden = new ArrayList<String>();
        Garden.add("Plow");
        Garden.add("Hose");
        Garden.add("Rake");
        Garden.add("Shovel");

        List<String> Children =new ArrayList<String>();
        Children.add("Stroller");
        Children.add("Crib");
        Children.add("Car Seats");
        Children.add("B");

        List<String> Home = new ArrayList<String>();
        Home.add("A");
        Home.add("B");
        Home.add("C");
        Home.add("D");

        List<String> Auto = new ArrayList<String>();
        Auto.add("A");
        Auto.add("B");
        Auto.add("C");
        Auto.add("D");

        category_details.put("App",App);
        category_details.put("Garden",Garden);
        category_details.put("Children",Children);
        category_details.put("Home",Home);
        category_details.put("Auto",Auto);
       // List<String>

        return category_details;
    }
}
