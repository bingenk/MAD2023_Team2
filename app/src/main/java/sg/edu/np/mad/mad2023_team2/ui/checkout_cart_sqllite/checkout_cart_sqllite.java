package sg.edu.np.mad.mad2023_team2.ui.checkout_cart_sqllite;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DataBaseHelper;
import sg.edu.np.mad.mad2023_team2.ui.cart_sqllite_database.DatabaseManager;

///////example class to test out the database/////////

public class checkout_cart_sqllite extends AppCompatActivity {


    Button btn_add,btnViewAll;
    EditText et_name,et_age;
    Switch sw_activeCustomer;
    ListView lv_customerList;
    ImageView imageView;

    ArrayAdapter customerArrayAdapter;
    DataBaseHelper dataBaseHelper;


    Cart_item itemModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_cart_sqllite);
        imageView=findViewById(R.id.imageView);
        btn_add=findViewById(R.id.btn_add);
        btnViewAll=findViewById(R.id.btn_viewAll);
        et_age=findViewById(R.id.et_age);
        et_name=findViewById(R.id.et_name);
        sw_activeCustomer=findViewById(R.id.sw_active);
        lv_customerList=findViewById(R.id.lv_customerList);

       dataBaseHelper = DatabaseManager.getDataBaseHelper(checkout_cart_sqllite.this);
//        ShowCustomersOnListView(dataBaseHelper);





        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View v) {

                String url ="https://cf.bstatic.com/xdata/images/hotel/max1280x900/297611078.jpg?k=e26cc333800386469e170cfec12cc5cc261812df76497422688608b41d30f7f3&o=";



                Date checkinDate = new Date(); // Set the check-in date to the current date
                Date checkoutDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);


                try {



                    itemModel=new Cart_item(-1,"Holiday Inn","Nostra ligula, commodo hac metus scelerisque nullam luctus ultrices. Quam at et etiam purus lacinia placerat condimentum eros dapibus imperdiet in. Iaculis aliquam integer integer."
                            , "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00,url,checkinDate, checkoutDate);



//                                Toast.makeText(MainActivity.this, itemModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){

//                                Toast.makeText(MainActivity.this, "Error creating customer", Toast.LENGTH_SHORT).show();
                    try {
                        itemModel=new Cart_item(-2,"Holiday Inn","Nostra ligula, commodo hac metus scelerisque nullam luctus ultrices. Quam at et etiam purus lacinia placerat condimentum eros dapibus imperdiet in. Iaculis aliquam integer integer."
                                , "5-Star Hotel", "Himenaeos aliquet sem ac fusce diam et viverra ad",234.00, 1.00, 1.00,url,checkinDate,checkoutDate);


                    } catch (Exception ex) {

                        throw new RuntimeException(ex);
                    }

                }



                 dataBaseHelper = new DataBaseHelper(checkout_cart_sqllite.this);


                boolean success = dataBaseHelper.addOne(itemModel);
                Toast.makeText(checkout_cart_sqllite.this, "success"+success, Toast.LENGTH_SHORT).show();
                ShowCustomersOnListView(dataBaseHelper);

            }


        });

        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dataBaseHelper = DatabaseManager.getDataBaseHelper(checkout_cart_sqllite.this);

                ShowCustomersOnListView(dataBaseHelper);


            }
        });



        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cart_item clickedCustomer= (Cart_item) parent.getItemAtPosition(position);

                dataBaseHelper.deleteOne(clickedCustomer);

                ShowCustomersOnListView(dataBaseHelper);

//            Toast.makeText(MainActivity.this, "Deleted"+clickedCustomer.toString(), Toast.LENGTH_SHORT).show();
            }
        });





    }



    private void ShowCustomersOnListView(DataBaseHelper dataBaseHelper) {
        checkout_cart_details details=this.dataBaseHelper.getEveryone();
        customerArrayAdapter=new ArrayAdapter<Cart_item>(checkout_cart_sqllite.this, android.R.layout.simple_list_item_1, details.getAllcartitems());
        lv_customerList.setAdapter(customerArrayAdapter);
        ArrayList<Cart_item> cartitem1=details.getAllcartitems();
        setimagetoiv(cartitem1);
        Toast.makeText(this, (details.getTotalprice()).toString(), Toast.LENGTH_SHORT).show();
    }


    private void setimagetoiv(ArrayList<Cart_item> x){
        try{      Picasso.with(this).load(x.get(0).getImage()).into(imageView);
        }
        catch (Exception e){
            Toast.makeText(this, "no image to be uploaded", Toast.LENGTH_SHORT).show();
        }

    }



}


//////////////////////EXTRA CODE///////////////////////////////////




//     public byte[] recoverImageFromUrl(URL url) throws Exception {
//
//         ByteArrayOutputStream output = new ByteArrayOutputStream();
//
//         try (InputStream inputStream = url.openStream()) {
//             int n = 0;
//             byte [] buffer = new byte[ 1024 ];
//             while (-1 != (n = inputStream.read(buffer))) {
//                 output.write(buffer, 0, n);
//             }
//         }
//
//         return output.toByteArray();
//     }



//     private byte[] downloadUrl(URL toDownload) {
//         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//         try {
//             byte[] chunk = new byte[4096];
//             int bytesRead;
//             InputStream stream = toDownload.openStream();
//
//             while ((bytesRead = stream.read(chunk)) > 0) {
//                 outputStream.write(chunk, 0, bytesRead);
//             }
//
//         } catch (IOException e) {
//             e.printStackTrace();
//             return null;
//         }
//
//         return outputStream.toByteArray();
//     }





//     private void ShowCustomerOnListView(DataBaseHelper dataBaseHelper2) {
//         customerArrayAdapter = new ArrayAdapter<Cart_item>(MainActivity.this,android.R.layout.simple_list_item_1, dataBaseHelper2.getEveryone());
//         lv_customerList.setAdapter(customerArrayAdapter);
//     }



// Proceed with further operations using the object
//                            byte[] imageByteArray = object.getImageByteArray();
// Use the byte array as needed
// For example, you can store it in a SQLite database


//                URL url = null;
//                try {
//                    url = new URL("https://cf.bstatic.com/xdata/images/hotel/max1280x900/297611078.jpg?k=e26cc333800386469e170cfec12cc5cc261812df76497422688608b41d30f7f3&o=");
//                } catch (MalformedURLException e) {
//                    Log.d("whatthefuck", "onClick:1 ");
//                    throw new RuntimeException(e);
//
//                }

//
//
//     public static byte[] downloadFile(URL url)
//     {
//         try {
//             URLConnection conn = url.openConnection();
//             conn.setConnectTimeout(5000);
//             conn.setReadTimeout(5000);
//             conn.connect();
//
//             ByteArrayOutputStream baos = new ByteArrayOutputStream();
//             IOUtils.copy(conn.getInputStream(), baos);
//
//             return baos.toByteArray();
//         }
//         catch (IOException e)
//         {
//
//             throw new RuntimeException(e);
//         }
//
//
//
//     }





//     public Date convertstringtodate(String date){
//         Date checkinDate = null;
//         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//         try {
//             checkinDate = dateFormat.parse(date);
//             return checkinDate;
//
//         } catch (ParseException e) {
//             e.printStackTrace();
//             return null;
//         }
//
//     }