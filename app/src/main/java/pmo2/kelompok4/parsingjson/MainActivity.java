package pmo2.kelompok4.parsingjson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String URL_JSON = "https://gist.githubusercontent.com/aws1994/f583d54e5af8e56173492d3f60dd5ebf/raw/c7796ba51d5a0d37fc756cf0fd14e54434c547bc/anime.json";
    private JsonArrayRequest jsonArrayRequest;
    private RequestQueue requestQueue;
    private List<AnimeModel> animeModelList = new ArrayList<>();
    private RecyclerView rv_konten;

    private Button btn_pindah;
    private TextView et_judul;
    private ListView lv_konten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_konten   = (RecyclerView)    findViewById(R.id.rv_konten);
        btn_pindah  = (Button)          findViewById(R.id.btn_pindah);
        et_judul    = (TextView)        findViewById(R.id.et_judul);
        lv_konten   = (ListView)        findViewById(R.id.lv_konten);
        lv_konten.setVisibility(View.GONE);

        //parsing JSON Online
        ambil_dataJSON_online();

        //parsing JSON Local
        ambil_dataJSON_local();

        btn_pindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_pindah.getText().equals("PARSING LOCAL")){
                    btn_pindah.setText("PARSING ONLINE");
                    et_judul.setText("JSON Parsing Local");
                    rv_konten.setVisibility(View.GONE);
                    lv_konten.setVisibility(View.VISIBLE);
                } else {
                    btn_pindah.setText("PARSING LOCAL");
                    et_judul.setText("JSON Parsing Online");
                    rv_konten.setVisibility(View.VISIBLE);
                    lv_konten.setVisibility(View.GONE);
                }
            }
        });
    }

    private void ambil_dataJSON_online(){
        jsonArrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try{
                        jsonObject = response.getJSONObject(i);
                        AnimeModel animeModel = new AnimeModel();
                        animeModel.setName(jsonObject.getString("name"));
                        animeModel.setCategorie(jsonObject.getString("categorie"));
                        animeModel.setDescription(jsonObject.getString("description"));
                        animeModel.setEpisode(jsonObject.getString("episode"));
                        animeModel.setImg(jsonObject.getString("img"));
                        animeModel.setRating(jsonObject.getString("Rating"));
                        animeModel.setStudio(jsonObject.getString("studio"));
                        animeModelList.add(animeModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(MainActivity.this, animeModelList );
                rv_konten.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rv_konten.setAdapter(recyclerAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "CIEE ERORR :P" , Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    private void ambil_dataJSON_local(){
        String isiJSON;
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        try{
            InputStream inputStream = getAssets().open("anggota.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            isiJSON = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(isiJSON);

            for (int i = 0; i <jsonArray.length() ; i++) {
                HashMap<String, String> user = new HashMap<>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                user.put("nama", jsonObject.getString("nama"));
                user.put("nim", jsonObject.getString("nim"));
                user.put("kelas", jsonObject.getString("kelas"));

                userList.add(user);
            }

            //membuat adapter -> karena untuk menampilkan data pada listview membutuhkan adapter
            ListAdapter listAdapter = new SimpleAdapter(MainActivity.this,  //konteks -> MainActivity.this
                    userList, //datanya
                    R.layout.anggota_item, //layoutnya
                    new String[]{"nama", "nim", "kelas"}, //dari
                    new int[]{R.id.tv_nama, R.id.tv_nim, R.id.tv_kelas}); //id pada layout
            lv_konten.setAdapter(listAdapter);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
