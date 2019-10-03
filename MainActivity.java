package com.example.princy.user_module;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.SharedPreferences;

import java.util.HashMap;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button btnitem1, btnupload ,btncap;
    Spinner cat1, cat2;
    ImageView cam1;
    EditText edtproduct, edtquan;
    SessionManager sessionManager;
    String phone ="";
    CheckBox checkitem;
    ImageView imageview;
    Bitmap bitmap;
    private static final int pic_id = 123;
    private static final int pic_id1 = 456;
    private static final int pic_id2 = 789;
    private int requestCode;
    private int resultCode;
    private Intent data;
    private String pictureFile;
    ProgressBar loading;
    final String URL="http://192.168.43.8:8080/upload";
    EditText pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnitem1 = findViewById(R.id.btnitem);
        btncap = findViewById(R.id.capture);
        btnupload = findViewById(R.id.upload);
        cat1 = findViewById(R.id.cat);
        cat2 = findViewById(R.id.cat1);
        imageview = findViewById(R.id.imageView);
        pd = findViewById(R.id.pd);
        int requestcode;

        edtproduct = findViewById(R.id.pd);
        edtquan = findViewById(R.id.edquantity);
        checkitem = findViewById(R.id.chksave);

//        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        String pictureFile = "ZOFTINO_"+timestamp;
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(pictureFile, ".jpg",storageDir);
//        pictureFilePath = image.getAbsolutePath();


        btncap.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Log.d("Capture", "Mandir vahi banayenge");
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);


            }
            }
        );
//        btnupload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("news", "Jai Shree Ram");
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void UploadImage(Bitmap bitmap,String phone) {
        Log.d("upload","upload var aala aapan" );
        getIp ip = new getIp();
        String del = ip.getIp();
        String ten = pd.getText().toString();
        String quan = edtquan.getText().toString();
        Log.d("bitmap","bitmap");
        Log.d("dfdfusdf", "dfksdfd "+ten);
        String image = getStringImage(bitmap);
        Log.d("dfdfusdf", "image leaa"+image);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String URL = ""+del+":8080/upload";

        //String URL = "http://192.168.0.103:8080/register";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product", ten);
            jsonObject.put("images",image);
            jsonObject.put("phone",phone);
            jsonObject.put("quantity",quan);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        final String requestBody = jsonObject.toString();
        Log.d("str_register","strREG is"+requestBody);

        connection.sendData(requestBody, requestQueue, URL, new connection.VolleyCallback(){
            @Override
            /*
            public void onSuccessResponse(String result) {
                Log.d("RESULT","RESULTS "+result);
                if (result.equals("1")) {
                    Toast.makeText(RegistrationActivity.this, result, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                //System.out.print("Bool" + result);
                 else {
                    Toast.makeText(RegistrationActivity.this, "Oops! Try Again.", Toast.LENGTH_SHORT).show();
                }
            }
            */


            public void onSuccessResponse(String result) {
                Log.d("RESULT","RESULTS dgfiugifgdi "+result);
                if (result.equals("1")) {
                    Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                    } else {
                    Toast.makeText(MainActivity.this, "Ooops! Try Again.", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(MainActivity.this,
                        "Volley needs attention" + error,
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("aamhi onactivity","aamhi onactivity");
        if (requestCode == 7 && resultCode == RESULT_OK && data != null) {
           // Uri filepath = data.getData();
            //try {
              //  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                final Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                imageview.setImageBitmap(bitmap);
                Log.d("bit","bit" +bitmap);

            btnupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sessionManager = new SessionManager(getApplicationContext());
                    sessionManager.checkLogin();
                    HashMap<String, String> user = sessionManager.getUserDetails();
                    phone = user.get(sessionManager.PHONE);
                    Log.d("news", "Jai Shree Ram");
                    UploadImage(bitmap,phone);
                }
            });
            //} catch (IOException e) {
              //  e.printStackTrace();
            //}

        }
    }

    public String getStringImage(Bitmap bm) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte, Base64.DEFAULT);
        return encode;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.MyProfile) {
            Intent in = new Intent(MainActivity.this, Registration.class);
            startActivity(in);
            // Handle the camera action
            //put intent here to which page you have to go on this corresponding menu click
        } else if (id == R.id.MYPost) {
            Intent an = new Intent(MainActivity.this, My_Post.class);
            startActivity(an);

        } else if (id == R.id.vouchers) {
            Intent ab = new Intent(MainActivity.this, My_Vouchers.class);
            startActivity(ab);

        }

         else if (id == R.id.addpost) {
            Intent ae = new Intent(MainActivity.this, MainActivity.class);
            startActivity(ae);

        } else if (id == R.id.help) {
            Intent ac = new Intent(MainActivity.this, help.class);
            startActivity(ac);

        } else if (id == R.id.about) {
            Intent ac = new Intent(MainActivity.this, About_us.class);
            startActivity(ac);

        } else if (id == R.id.log) {
            sessionManager.logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


}
