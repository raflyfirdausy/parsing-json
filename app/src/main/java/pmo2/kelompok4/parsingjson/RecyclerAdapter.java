package pmo2.kelompok4.parsingjson;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<AnimeModel> animeModelList;

    public RecyclerAdapter(Context context, List<AnimeModel> animeModelList) {
        this.context = context;
        this.animeModelList = animeModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.anime_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_namaAnimeItem.setText(animeModelList.get(i).getName());
        myViewHolder.tv_kategoriItem.setText(animeModelList.get(i).getCategorie());
        myViewHolder.tv_ratingItem.setText("Rating : " + animeModelList.get(i).getRating());
        myViewHolder.tv_studioItem.setText(animeModelList.get(i).getStudio());

        //load gambar dari url ke imageview pakai volley
        Glide.with(context)
                .load(animeModelList.get(i).getImg())
                .apply(new RequestOptions().centerCrop())
                .into(myViewHolder.iv_gambarItem);

        //membuat aksi ketika di klik itemnya
        final int posisi = i;
        myViewHolder.linearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(animeModelList.get(posisi).getName(), animeModelList.get(posisi).getDescription());
            }
        });
    }

    @Override
    public int getItemCount() {
        return animeModelList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_gambarItem;
        TextView tv_namaAnimeItem;
        TextView tv_kategoriItem;
        TextView tv_ratingItem;
        TextView tv_studioItem;
        LinearLayout linearItem;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_gambarItem       = itemView.findViewById(R.id.iv_gambarItem);
            tv_namaAnimeItem    = itemView.findViewById(R.id.tv_namaAnimeItem);
            tv_kategoriItem     = itemView.findViewById(R.id.tv_kategoriItem);
            tv_ratingItem       = itemView.findViewById(R.id.tv_ratingItem);
            tv_studioItem       = itemView.findViewById(R.id.tv_studioItem);
            linearItem          = itemView.findViewById(R.id.linearItem);
        }
    }

    private void showDialog(String title, String deskripsi){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        builder.setTitle(title)
                .setMessage(deskripsi)
                .setPositiveButton("OK", null)
                .show();
    }
}
