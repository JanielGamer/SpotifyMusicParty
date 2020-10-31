package com.example.musicparty;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicparty.music.Artist;
import com.example.musicparty.music.Track;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.List;


public class PartyAcRecycAdapter extends RecyclerView.Adapter<PartyAcRecycAdapter.ViewHolder> {

    public interface SongCallback{
        void returnSong(int i);
    }

    SongCallback songCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView songTitleTextView;
        public TextView artistNameTextView;
        public ImageView songCoverImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            songTitleTextView = (TextView) itemView.findViewById(R.id.songTitle);
            artistNameTextView = (TextView) itemView.findViewById(R.id.artistName);
            songCoverImageView = (ImageView) itemView.findViewById(R.id.songCover);
        }
    }

    private List<Track> mDataset;
    public PartyAcRecycAdapter(List<Track> test, SongCallback songCallback) {
        this.songCallback = songCallback;
        mDataset = test;
    }

    public void setmDataset(List<Track> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public PartyAcRecycAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View textView = inflater.inflate(R.layout.partyacrecyclerview_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(textView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = mDataset.get(position).getName();
        String artist = mDataset.get(position).getArtist(0).getName();
        String cover = mDataset.get(position).getCover();
        TextView textView = holder.songTitleTextView;
        textView.setText(name);
        TextView textView1 = holder.artistNameTextView;
        textView1.setText(artist);
        ImageView imageView = holder.songCoverImageView;
        new DownloadImageTask(imageView).execute(cover);
        holder.itemView.setOnClickListener(v -> {
            songCallback.returnSong(position);
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Log.d("Urldisplay", urldisplay);
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                Log.d("mIcon", mIcon11.toString());
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}
