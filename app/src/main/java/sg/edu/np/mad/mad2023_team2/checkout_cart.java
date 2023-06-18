package sg.edu.np.mad.mad2023_team2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class checkout_cart extends AppCompatActivity {

    RecyclerView rv;

    checkout_recyclerAdapter recyclerAdapter;
    List<String> checkout_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkout_cart=new ArrayList<>();


        rv=findViewById(R.id.rv_checkout);

        recyclerAdapter=new checkout_recyclerAdapter(checkout_cart);
        // you can also set the layout in the xml file using the layout manager attribute
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setAdapter(recyclerAdapter);
//To add dividers to between the views
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL
        );
        rv.addItemDecoration(dividerItemDecoration
        );

        checkout_cart.add("Iron man");
        checkout_cart.add("Iron man");
        checkout_cart.add("Iron man");
        checkout_cart.add("Iron man");
        checkout_cart.add("Iron man");
        checkout_cart.add("Iron man");


    }
}