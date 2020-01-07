package com.jakfromspace.kotoandroid;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Coded by JAKfromSpace on 7/29/2018 for Koto.
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2, fab3, buttonSave;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    private Button signOutButton, signInButton, buttonInsert, buttonRemove;
    private EditText editTextInsert, editTextRemove;
    private TextView nameView, emailView, textIn, textEx;
    private ImageView profileImageView;

    private GoogleApiClient googleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int REQ_CODE = 9001;
    public static String kotoUsername = null, kotoUserEmail = null;
    public static float kotoIncome, kotoExpense;
    public ProgressBar cashBar;

    public static ArrayList<KotoItem> kotoItems;

    private RecyclerView recyclerView;
    public static RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainoverview);

        SharedPreferences sharedPrefs = getSharedPreferences("com.jakfromspace.koto.userprefs", MODE_PRIVATE);
        kotoUserEmail = sharedPrefs.getString("kotoUserEmail", "kotokototaka@email.net");
        kotoUsername = sharedPrefs.getString("kotoUsername", "Koto Taka Khan");
        kotoIncome = sharedPrefs.getFloat("kotoIncome", 1000f);
        kotoExpense = sharedPrefs.getFloat("kotoExpense", 0f);

        LoadItems();
        //createKotoItemList();
        buildRecycler();
        InitUI();

        //Google Acc Init
        signOutButton.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);

        SignIn(signInButton);
        UpdateMoneyBar();


        DataFetch dataFetch = new DataFetch();
        dataFetch.execute();

        //SaveItems(buttonSave);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UpdateMoneyBar();
    }

    private void InitUI(){
        //Floating Button Init
        fab = findViewById(R.id.fabMain);
        fab1 = findViewById(R.id.fabCam);
        fab2 = findViewById(R.id.fabGall);
        fab3 = findViewById(R.id.fabMan);

        //Floating Button Animation Assignment
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rote_for);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rote_back);

        //Google Login Inits
        signInButton = findViewById(R.id.buttonGoogleSignIn);
        signOutButton = findViewById(R.id.buttonLogOut);
        nameView = findViewById(R.id.nameTextView);
        emailView = findViewById(R.id.emailTextView);
        profileImageView = findViewById(R.id.userImageView);
        emailView.setText(kotoUserEmail);
        nameView.setText(kotoUsername);

        buttonInsert = findViewById(R.id.buttonInsert);
        buttonRemove = findViewById(R.id.buttonRemove);
        editTextInsert = findViewById(R.id.editTextInsert);
        editTextRemove = findViewById(R.id.editTextRemove);
        buttonSave = findViewById(R.id.buttonSave);

        cashBar = findViewById(R.id.cashBar);
        textEx = findViewById(R.id.textViewEx);
        textIn = findViewById(R.id.textViewIncome);
    }
    public void BuildDialogBox(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cash Bar");

        final EditText input = new EditText(this);
        input.setHintTextColor(Color.CYAN);
        input.setHint("Enter Your Current Pocket Money");
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("Oukay!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = input.getText().toString().replaceAll("[^\\d.]", "");
                if(!s.isEmpty()) {
                    kotoIncome = Float.parseFloat(s);
                    UpdateMoneyBar();
                    dialog.dismiss();
                }
                else MakeToast("Your Pocket Money needs to be a Number");
            }
        });
        builder.setNegativeButton("Nopes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    @SuppressLint("NewApi")
    public void UpdateMoneyBar(){
        float in = kotoIncome, out = 0;
        for(KotoItem k: kotoItems){
            out += k.getTextSubAmount();
        }
        kotoExpense = out;
        cashBar.setMax((int)in);
        cashBar.setProgress((int)out);
        textIn.setText(in+" bdt");
        textEx.setText(out+" bdt");

        SharedPreferences sharedPrefs = getSharedPreferences("com.jakfromspace.koto.userprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putFloat("kotoIncome", kotoIncome);
        editor.putFloat("kotoExpense", kotoExpense);
        editor.apply();
    }
    private void buildRecycler(){
        recyclerView = findViewById(R.id.recyclerview);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerAdapter(kotoItems);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClick(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                MakeToast(kotoItems.get(pos).getTextMain());
            }

            @Override
            public void onDeleteClick(int pos) {
                kotoItems.remove(pos);
                adapter.notifyItemRemoved(pos);
                UpdateMoneyBar();
            }

        });
    }

    public void GotoGalleryPick(View view) {
        AnimateFAB(view);
        startActivity(new Intent(MainActivity.this, GalleryPickActivity.class));
        //adapter.notifyDataSetChanged();
    }
    public void GoToCameraCapture(View view){
        AnimateFAB(view);
        startActivity(new Intent(MainActivity.this, CameraActivity.class));
        //adapter.notifyDataSetChanged();
    }
    public void GoToAddManually(View view) {
        AnimateFAB(view);
        startActivity(new Intent(MainActivity.this, LiveCameraActivity.class));
    }
    public void AnimateFAB(View view) {
        if(isFabOpen){
            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
        }
        else{
            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void SignIn(View v){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,REQ_CODE);
    }
    public void SignOut(View v){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                signInButton.setVisibility(View.VISIBLE);
                signOutButton.setVisibility(View.GONE);
                nameView.setText("Mr Koto Taka Khan");
                emailView.setText("kotokototaka@email.net");
                profileImageView.setImageResource(R.drawable.user_placeholder);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MainActivity: ", "onActivityResult");
        if(requestCode == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.i("MainActivity: ", "Req Code");
            if (result.isSuccess()){
                Log.i("MainActivity: ", "Sign In Works");
                GoogleSignInAccount account = result.getSignInAccount();
                kotoUsername = account.getDisplayName();
                kotoUserEmail = account.getEmail();
                String img_url = account.getPhotoUrl().toString();
                nameView.setText(kotoUsername);
                emailView.setText(kotoUserEmail);
                Glide.with(this).load(img_url).into(profileImageView);
                signInButton.setVisibility(View.GONE);
                signOutButton.setVisibility(View.VISIBLE);

                SharedPreferences sharedPrefs = getSharedPreferences("com.jakfromspace.koto.userprefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("kotoUserEmail", kotoUserEmail);
                editor.putString("kotoUsername", kotoUsername);
                editor.apply();
            }
            else{
                Log.i("MainActivity: ", "Not Signed in");
                signInButton.setVisibility(View.VISIBLE);
                signOutButton.setVisibility(View.GONE);
            }
        }
    }

    public void InsertItem(View view) {
        int position = Integer.parseInt(editTextInsert.getText().toString());
        //String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        kotoItems.add(position, new KotoItem("New item at "+position, (position*4+7)/2));
        adapter.notifyItemInserted(position);
    }
    public void RemoveItem(View view) {
        int position = Integer.parseInt(editTextRemove.getText().toString());
        kotoItems.remove(position);
        adapter.notifyItemRemoved(position);
    }
    public void SaveItems(View view) {
        SharedPreferences sharedPrefs = getSharedPreferences("com.jakfromspace.koto.transactions", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(kotoItems);
        Log.i("MainActivity","JSON of Transaction Objects:\n" +json+ "\n");
        editor.putString("kotoitems", json);
        editor.apply();
        MakeToast("List was Saved in Local Storage. Chill");
    }
    public void LoadItems(){
        SharedPreferences sharedPrefs = getSharedPreferences("com.jakfromspace.koto.transactions", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("kotoitems", null);
        Type type =new TypeToken<ArrayList<KotoItem>>(){}.getType();
        kotoItems = gson.fromJson(json, type);
        if(kotoItems == null){
            kotoItems = new ArrayList<>();
        }
    }

    public void MakeToast(String ts){
        Toast.makeText(this, ts, Toast.LENGTH_SHORT).show();
    }

}
