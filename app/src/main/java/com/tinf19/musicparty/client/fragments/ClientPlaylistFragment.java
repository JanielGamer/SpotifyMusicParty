package com.tinf19.musicparty.client.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tinf19.musicparty.R;
import com.tinf19.musicparty.music.Track;
import com.tinf19.musicparty.client.adapter.ClientPlaylistAdapter;
import com.tinf19.musicparty.util.DownloadImageTask;

import java.util.ArrayList;
import java.util.List;

public class ClientPlaylistFragment extends Fragment {

    private static final String TAG = ClientPlaylistFragment.class.getName();
    private ClientPlaylistAdapter clientPlaylistAdapter;
    private RecyclerView recyclerView;
    private ImageView currentSongCoverImageView;
    private TextView currentSongTitleTextView;
    private TextView currentSongArtistTextView;
    private Track currentTrack;

    public ClientPlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_playlist, container, false);

        recyclerView = view.findViewById(R.id.clientPlaylistRecyclerView);
        if(recyclerView != null) {
            clientPlaylistAdapter = new ClientPlaylistAdapter(new ArrayList<Track>());
            recyclerView.setAdapter(clientPlaylistAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            recyclerView.setLayoutManager(layoutManager);
        }

        currentSongTitleTextView = view.findViewById(R.id.currentSongTitleTextView);
        currentSongArtistTextView = view.findViewById(R.id.currentSongArtistTextView);
        currentSongCoverImageView = view.findViewById(R.id.currentSongCoverImageView);

        return view;
    }

    public void setCurrentPlaying(Track track) {
        if(track != null) {
            Log.d(TAG, track.getName());
            if (currentSongTitleTextView != null) {
                currentSongTitleTextView.setText(track.getName());
            }
            if (currentSongArtistTextView != null) {
                currentSongArtistTextView.setText(track.getArtist(0).getName());
            }
            if (currentSongCoverImageView != null) {
                String coverURL = "https://i.scdn.co/image/" + track.getCoverFull();
                new DownloadImageTask(currentSongCoverImageView).execute(coverURL);
            }
        }
    }

    public void showResult(List<Track> tracks) {
        if(clientPlaylistAdapter != null) {
            clientPlaylistAdapter.setDataset(tracks);
            clientPlaylistAdapter.notifyDataSetChanged();
        }
    }
}