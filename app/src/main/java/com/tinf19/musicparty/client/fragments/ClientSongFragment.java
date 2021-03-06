package com.tinf19.musicparty.client.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.Telephony;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.tinf19.musicparty.server.HostService;
import com.tinf19.musicparty.util.DownloadImageTask;
import com.tinf19.musicparty.R;
import com.tinf19.musicparty.music.Track;


/**
 * Fragment where the client can see the playlist
 */
public class ClientSongFragment extends Fragment {

    private static final String TAG = ClientSongFragment.class.getName();
    public ClientSongCallback clientSongCallback;
    /**
     * Boolean to decide if the fragment is started or not
     */
    private boolean started;
    private ImageView songCover;
    private TextView songTitle;
    private TextView songArtist;
    private TextView songAlbum;
    private TextView connectedToParty;
    private LinearLayout openVotingButtonLinearLayout;

    public interface ClientSongCallback {
        void exitConnection();
        void showPlaylist();
        void openVotingFragment();
        HostService.PartyType getPartyType();
        Track getNowPlaying();
    }

    /**
     * Constructor to set the callback
     * @param clientSongCallback Communication callback for {@link com.tinf19.musicparty.client.ClientActivity}
     */
    public ClientSongFragment(ClientSongCallback clientSongCallback) { this.clientSongCallback = clientSongCallback; }

    /**
     * Empty-Constructor which is necessary in fragments
     */
    public ClientSongFragment() { }



    //Android lifecycle methods

    @Override
    public void onStart() {
        super.onStart();
        started = true;
        //TODO: Bug nach fragment wechsel
        Log.d(TAG, "textview starts rotating");
        if(songTitle != null) songTitle.setSelected(true);
        if(songArtist != null) songArtist.setSelected(true);
        if(songAlbum != null) songAlbum.setSelected(true);
        if(openVotingButtonLinearLayout != null) {
            Log.d(TAG, clientSongCallback.getPartyType().toString());
            if (clientSongCallback.getPartyType().equals(HostService.PartyType.VoteParty))
                openVotingButtonLinearLayout.setVisibility(View.VISIBLE);
            else
                openVotingButtonLinearLayout.setVisibility(View.GONE);
        }
        Track track = clientSongCallback.getNowPlaying();
        if(track != null)
            showSongs(track);
    }

    @Override
    public void onStop() {
        super.onStop();
        started = false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ClientSongCallback)
            clientSongCallback = (ClientSongCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_client_song, container, false);

        songCover = rootView.findViewById(R.id.songCoverImageView);
        songTitle = rootView.findViewById(R.id.songtitleTextView);
        songArtist = rootView.findViewById(R.id.artistTextView);
        songAlbum = rootView.findViewById(R.id.albumTextView);
        connectedToParty = rootView.findViewById(R.id.connectedTo);
        openVotingButtonLinearLayout = rootView.findViewById(R.id.openVotingButtonLinearLayout);

        ImageButton exitButton = rootView.findViewById(R.id.exitButton);
        if(exitButton != null) exitButton.setOnClickListener(v -> clientSongCallback.exitConnection());

        ImageButton playlistButton = rootView.findViewById(R.id.playlistButtonImageButton);
        if(playlistButton != null) playlistButton.setOnClickListener(v -> new Thread(() -> clientSongCallback.showPlaylist()).start());

        ImageButton votingButton = rootView.findViewById(R.id.votingButtonClientImageButton);
        if(votingButton != null) votingButton.setOnClickListener(v -> new Thread( () -> clientSongCallback.openVotingFragment()).start());

        return rootView;
    }


    /**
     * @return Get the current state of the fragment
     */
    public boolean getStarted() { return started; }


    /**
     * Set currently playing track in the views
     * @param track currently playing track
     */
    public void showSongs(Track track) {
        if(track != null && songTitle.getHeight() > 150) {
            Log.d(TAG, "welcome message gets changed to first song");
            songTitle.setSingleLine(true);
            songTitle.setHeight(150);
        }
        if(songCover != null) {
            String coverURL = "https://i.scdn.co/image/"+track.getCoverFull();
            new DownloadImageTask(songCover).execute(coverURL);
        }
        if(songTitle != null) songTitle.setText(track.getName());
        if(songArtist != null) songArtist.setText(track.getArtist(0).getName());
        if(songAlbum != null) songAlbum.setText(track.getAlbum());
    }


    /**
     * Set current party name in the connectedToParty view
     * @param name
     */
    public void setPartyName(String name) {
        String conTo = getString(R.string.text_connectedTo) + " ";
        String partyName = conTo + name;
        if(connectedToParty != null && !name.equals("")) {
            connectedToParty.setText(partyName, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable)connectedToParty.getText();
            int start = conTo.length();
            int end = start + name.length();
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * Show the button to open the {@link com.tinf19.musicparty.fragments.VotingFragment} if the
     * party type was changed to Voting-Party. Otherwise the button gets hidden.
     * @param partyType Current party type
     */
    public void toggleVotingButton(HostService.PartyType partyType) {
        if(openVotingButtonLinearLayout != null) {
            if (partyType.equals(HostService.PartyType.VoteParty))
                openVotingButtonLinearLayout.setVisibility(View.VISIBLE);
            else
                openVotingButtonLinearLayout.setVisibility(View.GONE);
        }
    }
}