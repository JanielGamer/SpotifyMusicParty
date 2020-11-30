package com.tinf19.musicparty.server.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tinf19.musicparty.R;
import com.tinf19.musicparty.music.Track;
import com.tinf19.musicparty.server.HostService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HostPlaylistAdapter extends RecyclerView.Adapter<HostPlaylistAdapter.MyViewHolder> implements HostPlaylistItemMoveHelper.HostPlaylistItemMoveHelperCallback {

    private final HostPlaylistAdapterCallback hostPlaylistAdapterCallback;
    private List<Track> mdataset;
    private View view;

    public interface HostPlaylistAdapterCallback {
        void swapPlaylistItems(int from, int to);
        void removeItem(Track toRemove, int position, HostService.AfterCallback callback);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView songTitleTextView;
        private final TextView songArtistTextView;
        private final View rowView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rowView = itemView;
            songTitleTextView = itemView.findViewById(R.id.playlistSongTitleTextView);
            songArtistTextView = itemView.findViewById(R.id.playlistArtistNameTextView);
        }
    }

    public HostPlaylistAdapter(ArrayList<Track> mDataset, HostPlaylistAdapterCallback hostPlaylistAdapterCallback) {
        this.mdataset = mDataset;
        this.hostPlaylistAdapterCallback = hostPlaylistAdapterCallback;
    }



    //Android lifecycle methods

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_client_playlist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String title = mdataset.get(position).getName();
        String artist = mdataset.get(position).getArtist(0).getName();
        TextView songTitleTV = holder.songTitleTextView;
        if(songTitleTV != null) {
            songTitleTV.setText(title);
        }
        TextView artistTV = holder.songArtistTextView;
        if(artistTV != null) {
            artistTV.setText(artist);
        }
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if(fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mdataset, i, i+1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mdataset, i, i-1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        hostPlaylistAdapterCallback.swapPlaylistItems(fromPosition, toPosition);

    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.button_green));
    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onRowDeleted(int position) {
        hostPlaylistAdapterCallback.removeItem(mdataset.get(position), position, this::notifyDataSetChanged);
    }

    @Override
    public int getItemCount() {
        return mdataset.size();
    }



    public void setDataset(List<Track> mDataset) {
        this.mdataset = mDataset;
    }
}
