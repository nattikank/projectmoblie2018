package com.example.admin.projectmobile;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Prograp extends AppCompatActivity {
private TextView usr,Name,Tel,addgrap;
private String namena;
ImageView proimg;
LinearLayout photo,edit,qrcode;
private String id,pass,add,_tel,Pro,name_,_Use;
private Button btn,insert;
private ImageView up,img;
private Uri imgUri;
private Button btnupanddown;
private StorageReference mStorageRef;
private ImageView menu;
private String locateImg,sel_up,qxy,_status,__type = "";
private Button qr;
private Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_photographer);
        btn = findViewById(R.id.insert);
        usr = findViewById(R.id.photoName);
        Name = findViewById(R.id.name2);
        Tel = findViewById(R.id.photoTel);
        proimg = findViewById(R.id.proimg);
        edit = findViewById(R.id.c_edit);
        insert = findViewById(R.id.insert);
        menu = findViewById(R.id.imageView6);
        addgrap = findViewById(R.id.address);
        qrcode = findViewById(R.id.qrcode);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        load();
        Bundle bd = getIntent().getExtras();
        Intent xd = getIntent();
        if(bd != null){
            Toast.makeText(getApplicationContext(),bd.getString("id"),Toast.LENGTH_LONG).show();
            id = bd.getString("id");
            pass = bd.getString("pass");
            namena = bd.getString("Name");
            add = bd.getString("add");
            _status = bd.getString("status");
            if(xd.hasExtra("lock")) {
                if (bd.getString("lock").equalsIgnoreCase("true")) {
                    edit.setEnabled(false);
                    btn.setVisibility(View.GONE);
                    edit.setBackground(getResources().getDrawable(R.color.smoke));
                    pass = bd.getString("pass");
                    _status = bd.getString("status");
                }
            }else{

            }

        }
        photo = findViewById(R.id.c_photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),postpic.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        edit = findViewById(R.id.c_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Editpro.class);
                intent.putExtra("pass",pass);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Prograp.this);
                LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.uploadphoto,null);
                builder.setView(view);
                up = view.findViewById(R.id.imageView3);
                sp = view.findViewById(R.id.spinselect);
                ArrayAdapter<String> array = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.type));
                sp.setAdapter(array);
                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0){
                            return;
                        }else{
                            __type = parent.getItemAtPosition(position).toString();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });
                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sel_up = "photo";
                        locateImg = "photo";
                        Select_photo();
                    }
                });
                btnupanddown = view.findViewById(R.id.btnupimgpost);
                btnupanddown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(__type.equalsIgnoreCase("")){
                            Toast.makeText(view.getContext(),"กรุณาเลือกประเภทรูป",Toast.LENGTH_SHORT).show();
                        }else{
                            Upload();
                        }
                    }
                });
                builder.show();
            }
        });
        if(_status.equalsIgnoreCase("Member")){
            menu.setEnabled(false);
        }else{
            menu.setEnabled(true);
        }

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Prograp.this,menu);
                popupMenu.getMenuInflater().inflate(R.menu.main,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.nav_edit:{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Prograp.this);
                                LayoutInflater layoutInflater = getLayoutInflater();
                                View viewna = layoutInflater.inflate(R.layout.edit_detail_user,null);
                                builder.setView(viewna);
                                final EditText nameed = viewna.findViewById(R.id.nameed);
                                final EditText teled = viewna.findViewById(R.id.teled);
                                final EditText added = viewna.findViewById(R.id.added);
                                nameed.setText(namena);
                                teled.setText(Tel.getText());
                                added.setText(add);

                                builder.setPositiveButton("แก้ไข", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EditPro(nameed.getText().toString(),teled.getText().toString(),added.getText().toString());
                                        Name.setText(nameed.getText());
                                        Tel.setText(teled.getText());
                                        addgrap.setText(added.getText());
                                    }
                                });
                                builder.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                                return true;
                            }
                            case R.id.nav_qr:{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Prograp.this);
                                LayoutInflater layoutInflater = getLayoutInflater();
                                View viewna = layoutInflater.inflate(R.layout.showqrcode,null);
                                builder.setView(viewna);
                                ImageView ngokun = viewna.findViewById(R.id.imageView5);
                                Glide.with(viewna.getContext()).load(qxy).into(ngokun);
                                builder.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                                return true;
                            }
                            default:{
                                return false;
                            }
                        }
                    }
                });
                popupMenu.show();
            }
        });


        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_status.equalsIgnoreCase("Photographer")) {
                    if (qxy.equalsIgnoreCase("")) {
                        AlertDialog.Builder builderxyz = new AlertDialog.Builder(Prograp.this);
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View viewna = layoutInflater.inflate(R.layout.upqr, null);
                        builderxyz.setView(viewna);
                        qr = viewna.findViewById(R.id.Upload);
                        qr.setEnabled(false);
                        img = viewna.findViewById(R.id.imageView4);
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                locateImg = "qrcode";
                                sel_up = "qr";
                                Select_photo();
                            }
                        });
                        qr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Upload();
                            }
                        });
                        builderxyz.show();
                    } else {
                        Toast.makeText(getApplicationContext(), "คุณลงทะเบียน qr code ไว้แล้ว", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Prograp.this);
                    LayoutInflater layoutInflater = getLayoutInflater();
                    View viewna = layoutInflater.inflate(R.layout.showqrcode,null);
                    builder.setView(viewna);
                    ImageView ngokun = viewna.findViewById(R.id.imageView5);
                    Glide.with(viewna.getContext()).load(qxy).into(ngokun);
                    builder.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }


        });
    }

    private void Select_photo() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK && data != null && data != null) {
            imgUri = data.getData();
            if (imgUri != null && locateImg.equalsIgnoreCase("qrcode")) {
                qr.setEnabled(true);
                Toast.makeText(getApplicationContext(),sel_up,Toast.LENGTH_SHORT).show();
            }
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                if(locateImg.equalsIgnoreCase("photo")){
                    up.setImageBitmap(bm);
                    locateImg = "";
                }else if(locateImg.equalsIgnoreCase("qrcode")){
                    img.setImageBitmap(bm);
                    locateImg = "";
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void Upload(){
        if (imgUri != null) {
            //Get the storage reference
            final StorageReference ref = mStorageRef.child("profile" + System.currentTimeMillis() + "." + getImageExt(imgUri));
            //Add file to reference
            ref.putFile(imgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Toast.makeText(getApplicationContext(),"Upload Complete",Toast.LENGTH_LONG).show();
                        if(sel_up.equalsIgnoreCase("photo")) {
                            Insert(downloadUri.toString(),__type);
                        }else if(sel_up.equalsIgnoreCase("qr")){
                            Qr(downloadUri.toString());
                        }
                    } else {
                        Toast.makeText(Prograp.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    private void Insert(final String img,final String type){
        String _url = "http://10.51.5.222/insertphoto.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            if(response.equalsIgnoreCase("Success")){
                Intent i = new Intent(getApplicationContext(),postpic.class);
                i.putExtra("id",id);
                startActivity(i);
            }else{

            }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("url",img);
                param.put("user_id",id);
                param.put("type",type);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void EditPro(final String name,final String tel,final String add){
        String _url = "http://10.51.5.222/updateprofile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("Success")){
                    finish();
                    startActivity(getIntent());
                }else{

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("name",name);
                param.put("tel",tel);
                param.put("address",add);
                param.put("id",id);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void load(){
        String url = "http://10.51.5.222/load.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i = 0;i<array.length();i++){
                        JSONObject photo = array.getJSONObject(i);
                        _Use = photo.getString("username");
                        name_ = photo.getString("Name");
                        _tel = photo.getString("tel");
                        Pro = photo.getString("image");
                        pass = photo.getString("pass");
                        add = photo.getString("address");
                        qxy = photo.getString("qr");
                    }
                    usr.setText(_Use);
                    Name.setText(name_);
                    Tel.setText(_tel);
                    addgrap.setText(add);
                    Glide.with(getApplicationContext()).load(Pro).into(proimg);
                    Toast.makeText(getApplicationContext(),qxy,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("id",id);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Qr(final String qrcode){
        String url = "http://10.51.5.222/upqr.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Upload Qr code เรียบร้อย",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("id",id);
                param.put("qr",qrcode);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}