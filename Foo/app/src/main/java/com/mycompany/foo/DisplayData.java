package com.mycompany.foo;

import android.app.ExpandableListActivity;
import android.support.v7.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayData extends AppCompatActivity {
    HashMap<String,List<String>> Category;
    List<String> List;
    ExpandableListView Exp_list;
    CategoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        Exp_list =(ExpandableListView) findViewById(R.id.exp_list);
        Category =  DataProvider.getInfo();
        List = new ArrayList<String>(Category.keySet());
        adapter = new CategoryAdapter(this,Category,List);

        Exp_list.setAdapter(adapter);

        Exp_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getBaseContext(), List.get(groupPosition) + "is Expanded", Toast.LENGTH_LONG).show();
            }
        });
        Exp_list.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getBaseContext(), List.get(groupPosition) + "is Collapse", Toast.LENGTH_LONG).show();
            }
        });

        Exp_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getBaseContext(),
                        Category.get(List.get(groupPosition)).get(childPosition)+" from Category "+
                                List.get(groupPosition)+ " is Selected ",Toast.LENGTH_LONG).show();
                return false;
            }
        });
      //  Intent intent = getIntent();
       // String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Create the text view
       // TextView textView = new TextView(this);
       // textView.setTextSize(40);
       // textView.setText(message);

        // Set the text view as the activity layout
       // setContentView(textView);
       // if (savedInstanceState == null) {
        //    getSupportFragmentManager().beginTransaction();
            //             .add(R.id.container, new PlaceholderFragment()).commit();
       // }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        //  @Override
        //public View onCreateView(LayoutInflater inflater, ViewGroup container,
        //                         Bundle savedInstanceState) {
        //    View rootView = inflater.inflate(R.layout.fragment_display_data,
        //           container, false);
        //    return rootView;
        //}
    }
}