package com.lehoangduy.smartworld.Activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.lehoangduy.smartworld.Adapter.MyExpandableListAdapter;
import com.lehoangduy.smartworld.Adapter.ViewPageAdapter;
import com.lehoangduy.smartworld.HomePage.PageFour;
import com.lehoangduy.smartworld.HomePage.PageOne;
import com.lehoangduy.smartworld.HomePage.PageThree;
import com.lehoangduy.smartworld.HomePage.PageTwo;
import com.lehoangduy.smartworld.Model.ChildRow;
import com.lehoangduy.smartworld.Model.ParentRow;
import com.lehoangduy.smartworld.Model.SQLite;
import com.lehoangduy.smartworld.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {
    String urlAddress="http://hostfree321.esy.es/search.php";
    public static SQLite db;
    SearchView sv;
    ListView lv;
    ImageView noDataImg;
    ListView lvSearch;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPageAdapter viewPageAdapter;
    private int[] tabIcons = {
            R.drawable.home
            ,R.drawable.spmoi
            ,R.drawable.carts
            ,R.drawable.news
    };

    private SearchManager searchManager;
    private android.widget.SearchView searchView;
    private MyExpandableListAdapter listAdapter;
    private ExpandableListView myList;
    private ArrayList<ParentRow> parentList = new ArrayList<ParentRow>();
    private ArrayList<ParentRow> showTheseParentList = new ArrayList<ParentRow>();
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        db = new SQLite(this, "sw.sqlite", null, 1);
        db.QueryData("CREATE TABLE IF NOT EXISTS GioHang_Table(MaSP INTEGER, TenSP VARCHAR, SoLuong INTEGER, Email VARCHAR, Gia INTEGER, Hinh VARCHAR, Tong INTEGER)");
        //db.QueryData("DELETE FROM GioHang_Table");
        //db.QueryData("DROP TABLE GioHang_Table");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPage);


        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }


        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        viewPageAdapter.addFragment(new PageOne(), "HOME");
        viewPageAdapter.addFragment(new PageTwo(), "NEW");
        viewPageAdapter.addFragment(new PageThree(), "CART");
        viewPageAdapter.addFragment(new PageFour(), "NEWS");

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Bundle extras = getIntent().getExtras();
        int position = 0;
        if(extras != null) {
            position = extras.getInt("viewpager_position");
            viewPager.setCurrentItem(position);
        }
        setupTabIcons();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    private void loadData(){
            ArrayList<ChildRow> childRows = new ArrayList<ChildRow>();
            ParentRow parentRow = null;

            childRows.add(new ChildRow(R.mipmap.ic_launcher_reload
                    ,"Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
            childRows.add(new ChildRow(R.mipmap.ic_launcher_reload
                    , "Sit Fido, sit."));
            parentRow = new ParentRow("First Group", childRows);
            parentList.add(parentRow);

            childRows = new ArrayList<ChildRow>();
            childRows.add(new ChildRow(R.mipmap.ic_launcher_reload
                    , "Fido is the name of my dog."));
            childRows.add(new ChildRow(R.mipmap.ic_launcher_reload
                    , "Add two plus two."));
            parentRow = new ParentRow("Second Group", childRows);
            parentList.add(parentRow);


    }

    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            myList.expandGroup(i);
        } //end for (int i = 0; i < count; i++)
    }

    private void displayList() {
        loadData();

        myList = (ExpandableListView) findViewById(R.id.expandableListView_search);
        listAdapter = new MyExpandableListAdapter(MainActivity.this, parentList);

        myList.setAdapter(listAdapter);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_noibat) {

            Intent intent = new Intent(MainActivity.this, NoiBatActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gift) {

            Intent intent = new Intent(MainActivity.this, KhuyenMaiActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_phone) {
            Intent intent = new Intent(MainActivity.this, DanhMucSanPham.class);
            startActivity(intent);

        } else if (id == R.id.nav_phukien) {

            Intent intent = new Intent(MainActivity.this, PhuKienActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_canhan) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lienhe) {

        } else if (id == R.id.nav_maps) {

            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_baohanh) {

        } else if (id == R.id.nav_thoat) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Xác nhận");
            alertDialogBuilder.setIcon(R.mipmap.ic_launcher_exit);
            alertDialogBuilder
                    .setMessage("Bạn chắc chắn muốn thoát ứng dụng?")
                    .setCancelable(false)
                    .setPositiveButton("Có",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("EXIT", true);
                                    startActivity(intent);
                                    finish();
                                }
                            })

                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupTabIcons() {
        for(int i = 0; i <= 3; i++){
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu, menu);
//        searchItem = menu.findItem(R.id.search_view);
//        searchView = (android.widget.SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setSearchableInfo
//                (searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
//        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                listAdapter.filterData(query);
//                expandAll();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                listAdapter.filterData(newText);
//                expandAll();
//                return false;
//            }
//        });
//        searchView.requestFocus();
//        return true;
//
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            if(searchView != null){
//                searchView.clearFocus();
//            }else {
//                new AsyncFetch(query).execute();
//            }
//        }
//    }
//    private class AsyncFetch extends AsyncTask<String, String, String> {
//
//        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
//        HttpURLConnection conn;
//        URL url = null;
//        String searchQuery;
//
//        public AsyncFetch(String searchQuery) {
//            this.searchQuery = searchQuery;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading...");
//            pdLoading.setCancelable(false);
//            pdLoading.show();
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//
//                // Enter URL address where your php file resides
//                url = new URL("http://hostfree321.esy.es/search.php");
//
//            } catch (MalformedURLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                return e.toString();
//            }
//            try {
//
//                // Setup HttpURLConnection class to send and receive data from php and mysql
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(20000);
//                conn.setConnectTimeout(20000);
//                conn.setRequestMethod("POST");
//
//                // setDoInput and setDoOutput to true as we send and recieve data
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                // add parameter to our above url
//                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", searchQuery);
//                String query = builder.build().getEncodedQuery();
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                writer.write(query);
//                writer.flush();
//                writer.close();
//                os.close();
//                conn.connect();
//
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//                return e1.toString();
//            }
//
//            try {
//
//                int response_code = conn.getResponseCode();
//
//                // Check if successful connection made
//                if (response_code == HttpURLConnection.HTTP_OK) {
//
//                    // Read data sent from server
//                    InputStream input = conn.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//                    StringBuilder result = new StringBuilder();
//                    String line;
//
//                    while ((line = reader.readLine()) != null) {
//                        result.append(line);
//                    }
//
//                    // Pass data to onPostExecute method
//                    return (result.toString());
//
//                } else {
//                    return ("Connection error");
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return e.toString();
//            } finally {
//                conn.disconnect();
//            }
//
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            //this method will be running on UI thread
//            pdLoading.dismiss();
//            List<SanPham> data = new ArrayList<>();
//
//            pdLoading.dismiss();
//            if (result.equals("no rows")) {
//                Toast.makeText(MainActivity.this, "No Results found for entered query", Toast.LENGTH_LONG).show();
//            } else {
//
//                try {
//
//                    JSONArray jArray = new JSONArray(result);
//
//                    // Extract data from json and store into ArrayList as class objects
//                    for (int i = 0; i < jArray.length(); i++) {
//                        JSONObject json_data = jArray.getJSONObject(i);
//                        SanPham sanPham = new SanPham();
//                        sanPham.MaSanPham = json_data.getInt("MaSanPham");
//                        sanPham.TenSanPham = json_data.getString("TenSanPham");
//                        sanPham.GiaSanPham = json_data.getInt("GiaSanPham");
//                        sanPham.ChiTietSanPham = json_data.getString("ChiTietSanPham");
//                        sanPham.HinhSanPham = json_data.getString("HinhAnh");
//                        data.add(sanPham);
//                    }
//
//                    // Setup and Handover data to recyclerview
//
//                    lvSearch = (ListView) findViewById(R.id.lvSearch);
//                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, data);
//                    lvSearch.setAdapter(adapter);
//
//                } catch (JSONException e) {
//                    // You to understand what actually error is and handle it appropriately
//                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//        }
//    }
}
