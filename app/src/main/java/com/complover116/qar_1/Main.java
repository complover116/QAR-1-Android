package com.complover116.qar_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.nio.ByteBuffer;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

public class Main extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, RealTimeMessageReceivedListener, RoomUpdateListener, RoomStatusUpdateListener {
    public GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 9001;
    final static int RC_WAITING_ROOM = 10002;
    final static int RC_SELECT_PLAYERS = 10000;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;
    private boolean mConnected = false;
    ArrayList<RemoteClient> clients = new ArrayList<RemoteClient>();
    String mRoomId = null;
    Room room = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurGame.mClass = this;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                        // add other APIs and scopes here as needed
                .build();
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private RoomConfig.Builder makeBasicRoomConfigBuilder() {
        RoomConfig.Builder builder = RoomConfig.builder(this);
        builder.setMessageReceivedListener(this);
        builder.setRoomStatusUpdateListener(this);
        // ...add other listeners as needed...

        return builder;
    }
    private void engageMPScreen() {
        setContentView(R.layout.engaging_multiplayer);
    }
    private void mainScreen() {
        setContentView(R.layout.activity_main);
        updateConnectedState();
    }
    public void createGame(View view) {
        engageMPScreen();
        Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 3);
        startActivityForResult(intent, RC_SELECT_PLAYERS);
    }
    public void startQuickGame(View view) {
        engageMPScreen();
        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).
        Bundle am = RoomConfig.createAutoMatchCriteria(1, 3, 0);

        // build the room config:
        RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
        roomConfigBuilder.setAutoMatchCriteria(am);
        RoomConfig roomConfig = roomConfigBuilder.build();

        // create room:
        Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);

        // prevent screen from sleeping during handshake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // go to game screen
    }
    public void sendKey(int key, boolean state) {
        byte out[] = new byte[256];
        if(state) {
            out[0] = 100;
        } else {
            out[0] = 101;
        }
        ByteBuffer.wrap(out, 1, 255).putInt(key);
        Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, out,
                mRoomId, CurGame.hostID);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        leaveRoom();
        mGoogleApiClient.disconnect();
    }
    // Sets the flag to keep this screen on. It's recommended to do that during
    // the
    // handshake when setting up a game, because if the screen turns off, the
    // game will be
    // cancelled.
    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Clears the flag that keeps the screen on.
    void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    // Leave the room.
    void leaveRoom() {
        stopKeepingScreenOn();
        if (mRoomId != null) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
            mRoomId = null;
        } else {

        }
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

    public void play(View view) {
        Intent intent = new Intent(this, Game.class);
        new Thread(new TickerThread(this)).start();
        startActivity(intent);
    }
    public void switchConnectedState(boolean connected) {
        findViewById(R.id.connectbutton).setEnabled(!connected);
        findViewById(R.id.disconnectbutton).setEnabled(connected);
        findViewById(R.id.automatchbutton).setEnabled(connected);
        mConnected = connected;
    }
    public void updateConnectedState() {
        findViewById(R.id.connectbutton).setEnabled(!mConnected);
        findViewById(R.id.disconnectbutton).setEnabled(mConnected);
        findViewById(R.id.automatchbutton).setEnabled(mConnected);
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        switchConnectedState(true);
        if (connectionHint != null) {
            Invitation inv =
                    connectionHint.getParcelable(Multiplayer.EXTRA_INVITATION);

            if (inv != null) {
                // accept invitation
                RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
                roomConfigBuilder.setInvitationIdToAccept(inv.getInvitationId());
                Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());

                // prevent screen from sleeping during handshake
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                // go to game screen
                engageMPScreen();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_failure);
            }
        }
        if (requestCode == RC_WAITING_ROOM) {
            if (resultCode == RESULT_OK) {
                startGame();
            } else {
                Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_LONG).show();
                mainScreen();
            }
        }
        if (requestCode == RC_SELECT_PLAYERS) {
        if (resultCode != Activity.RESULT_OK) {
            // user canceled
            return;
        }

        // get the invitee list
        Bundle extras = data.getExtras();
        final ArrayList<String> invitees =
                data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

        // get auto-match criteria
        Bundle autoMatchCriteria = null;
        int minAutoMatchPlayers =
                data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers =
                data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

        if (minAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                    minAutoMatchPlayers, maxAutoMatchPlayers, 0);
        } else {
            autoMatchCriteria = null;
        }

        // create the room and specify a variant if appropriate
        RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
        roomConfigBuilder.addPlayersToInvite(invitees);
        if (autoMatchCriteria != null) {
            roomConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        }
        RoomConfig roomConfig = roomConfigBuilder.build();
        Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);

        // prevent screen from sleeping during handshake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    }

    private void startGame() {

        //DETERMINE THE HOST
        ArrayList<String> ids = room.getParticipantIds();
        java.util.Collections.sort(ids, Collator.getInstance());
        CurGame.hostID = ids.get(0);
        CurGame.color = 1;
        for(String p:room.getParticipantIds()) {
            if (room.getParticipantId(Games.Players.getCurrentPlayer(mGoogleApiClient).getPlayerId()) == p) {

                break;
            }
            CurGame.color ++;
        }
        if(CurGame.hostID == room.getParticipantId(Games.Players.getCurrentPlayer(mGoogleApiClient).getPlayerId())) {
            Toast.makeText(getApplicationContext(), "I get to be the host! Cool!", Toast.LENGTH_LONG).show();
            CurGame.isServer = true;
            new Thread(new TickerThread(this)).start();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_game);
        } else {
            Toast.makeText(getApplicationContext(), CurGame.hostID+" gets to be the host.", Toast.LENGTH_LONG).show();
            CurGame.isServer = false;
            new Thread(new TickerThread(this)).start();
            byte out[] = new byte[32];
            out[0] = 124;
            Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, out,
                    mRoomId, CurGame.hostID);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_game);
        }
    }

    // Call when the sign-in button is clicked
    public void signInClicked(View view) {
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    // Call when the sign-out button is clicked
    public void signOutclicked(View view) {
        mSignInClicked = false;
        switchConnectedState(false);
        Games.signOut(mGoogleApiClient);
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, "There was an issue with sign-in, please try again later.")) {
                mResolvingConnectionFailure = false;
            }
        }

        // Put code here to display the sign-in button
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        //Wat do?
        byte in[] = realTimeMessage.getMessageData();
        if(CurGame.isServer) {
            //System.out.println(realTimeMessage.toString());
            if (in[0] == 124) {
                clients.add(new RemoteClient(realTimeMessage.getSenderParticipantId(),
                        in[1]));
            }
            if (in[0] == 100) {
                for (int i = 0; i < CurGame.lvl.players.size(); i++)
                    CurGame.lvl.players.get(i).keyPressed(ByteBuffer.wrap(in, 1, 255).getInt());
            }
            if (in[0] == 101) {
                for (int i = 0; i < CurGame.lvl.players.size(); i++)
                    CurGame.lvl.players.get(i).keyReleased(ByteBuffer.wrap(in, 1, 255).getInt());
            }
        } else {
            switch(in[0]) {
                case 1:
                    ByteBuffer data = ByteBuffer.wrap(in, 2, 30);
                    CurGame.lvl.players.get(in[1]).update(data);
                    break;
                case 2:
                    ByteBuffer data2 = ByteBuffer.wrap(in, 1, 127);
                    //for(int i = 0; i < 256; i ++) System.out.print(in[i]+":");
                    CurGame.lvl.TADs.add(new Projectile(data2));
                    break;
                case 3:
                    switch(in[1]) {
                        case 1:
                            CurGame.lvl.loadMap(Map.map1);
                            break;
                        case 2:
                            CurGame.lvl.loadMap(Map.map2);
                            break;
                    }
                    break;
            }
        }
    }
    public void sendData() {
        for(byte i = 0; i < 4; i ++){
            byte out[] = new byte[32];
            out[0] = 1;
            out[1] = i;
            ByteBuffer data = ByteBuffer.wrap(out, 2, 30);
            CurGame.lvl.players.get(i).downdate(data);
            if(mGoogleApiClient.isConnected()) {
                Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, out,
                        mRoomId);
            }
        }
    }
    @Override
    public void onRoomCreated(int i, Room room) {
        if (i != GamesStatusCodes.STATUS_OK) {
            // let screen go to sleep
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // show error message, return to main screen.
        }
        // get waiting room intent
        Intent intent = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, Integer.MAX_VALUE);
        startActivityForResult(intent, RC_WAITING_ROOM);
        this.room = room;
    }

    @Override
    public void onJoinedRoom(int i, Room room) {
        // get waiting room intent
        Intent intent = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, Integer.MAX_VALUE);
        startActivityForResult(intent, RC_WAITING_ROOM);
        this.room = room;
    }

    @Override
    public void onLeftRoom(int i, String s) {

    }

    @Override
    public void onRoomConnected(int i, Room room) {
        this.room = room;
    }

    @Override
    public void onRoomConnecting(Room room) {
        this.room = room;
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        this.room = room;
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> strings) {
        this.room = room;
    }

    @Override
    public void onPeerDeclined(Room room, List<String> strings) {
        this.room = room;
    }

    @Override
    public void onPeerJoined(Room room, List<String> strings) {
        this.room = room;
    }

    @Override
    public void onPeerLeft(Room room, List<String> strings) {
        this.room = room;
    }

    @Override
    public void onConnectedToRoom(Room room) {
        mRoomId = room.getRoomId();
        this.room = room;

    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        // leave the room
        Games.RealTimeMultiplayer.leave(mGoogleApiClient, null, mRoomId);

        // clear the flag that keeps the screen on
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toast.makeText(getApplicationContext(), "Connection error, leaving the room...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPeersConnected(Room room, List<String> strings) {
        this.room = room;
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> strings) {
        this.room = room;
    }

    @Override
    public void onP2PConnected(String s) {

    }

    @Override
    public void onP2PDisconnected(String s) {

    }
}
