package pubnub.estudos.com.jogodavelhaiot;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class MainActivity extends AppCompatActivity  {

    private int posX, posY = 0;

    @BindView(R.id.txt00)
    TextView txt00;
    @BindView(R.id.txt01)
    TextView txt01;
    @BindView(R.id.txt02)
    TextView txt02;
    @BindView(R.id.txt10)
    TextView txt10;
    @BindView(R.id.txt11)
    TextView txt11;
    @BindView(R.id.txt12)
    TextView txt12;
    @BindView(R.id.txt20)
    TextView txt20;
    @BindView(R.id.txt21)
    TextView txt21;
    @BindView(R.id.txt22)
    TextView txt22;

    private long lastEvent = 0;

    public int[][]  cells = new int[3][3];
    //0: não usada
    //1: android
    //2: arduino

    public boolean androidWillPlay = true;

    private PubNub pubnub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-51e68992-3ba4-11e8-8394-86efddfa61f5");
        pnConfiguration.setPublishKey("pub-c-0e59b032-e680-4ea2-a1a6-53b77fd88bec");

        pubnub = new PubNub(pnConfiguration);
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                String type = message.getMessage().getAsJsonObject().get("type").getAsString();
                if (type.equals("move")) {
                    double x = message.getMessage().getAsJsonObject().get("x").getAsDouble();
                    double y = message.getMessage().getAsJsonObject().get("y").getAsDouble();

                    if (System.currentTimeMillis() - lastEvent > 1000) {

                        boolean change = false;
                        if (x < -0.4) {
                            if (posX > 0) {
                                change = true;
                                posX--;
                            }
                        } else if (x > 0.4) {
                            if (posX < 2) {
                                change = true;
                                posX++;
                            }
                        }

                        if (y < -0.4) {
                            if (posY > 0) {
                                change = true;
                                posY--;
                            }
                        } else if (y > 0.4) {
                            if (posY < 2) {
                                change = true;
                                posY++;
                            }
                        }

                        if (change) {
                            txt00.post(new Runnable() {
                                @Override
                                public void run() {
                                    repaintCells();
                                }
                            });
                            lastEvent = System.currentTimeMillis();
                        }

                    }
                } else {
                    if (cells[posX][posY] == 0 && !androidWillPlay){
                        androidWillPlay = true;
                        cells[posX][posY] = 2;
                        checkFinishGame();
                        txt00.post(new Runnable() {
                            @Override
                            public void run() {
                                paintTextInCell();
                            }
                        });
                    }
                }

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        pubnub.subscribe().channels(Arrays.asList("awesomeChannel")).execute();

        repaintCells();

        pubnub.publish().channel("resetGame").message("reset").async(callback);

    }

    public void paintTextInCell(){
        switch (posX){
            case 0:
                switch (posY){
                    case 0:
                        txt00.setText("O");
                        break;
                    case 1:
                        txt01.setText("O");
                        break;
                    case 2:
                        txt02.setText("O");
                        break;
                }
                break;
            case 1:
                switch (posY){
                    case 0:
                        txt10.setText("O");
                        break;
                    case 1:
                        txt11.setText("O");
                        break;
                    case 2:
                        txt12.setText("O");
                        break;
                }
                break;
            case 2:
                switch (posY){
                    case 0:
                        txt20.setText("O");
                        break;
                    case 1:
                        txt21.setText("O");
                        break;
                    case 2:
                        txt22.setText("O");
                        break;
                }
                break;
        }
    }

    public void repaintCells(){
        int cinza = Color.rgb(235, 235, 235);

        txt00.setBackgroundColor(Color.WHITE);
        txt01.setBackgroundColor(Color.WHITE);
        txt02.setBackgroundColor(Color.WHITE);
        txt10.setBackgroundColor(Color.WHITE);
        txt11.setBackgroundColor(Color.WHITE);
        txt12.setBackgroundColor(Color.WHITE);
        txt20.setBackgroundColor(Color.WHITE);
        txt21.setBackgroundColor(Color.WHITE);
        txt22.setBackgroundColor(Color.WHITE);

        switch (posX){
            case 0:
                switch (posY){
                    case 0:
                        txt00.setBackgroundColor(cinza);
                        break;
                    case 1:
                        txt01.setBackgroundColor(cinza);
                        break;
                    case 2:
                        txt02.setBackgroundColor(cinza);
                        break;
                }
                break;
            case 1:
                switch (posY){
                    case 0:
                        txt10.setBackgroundColor(cinza);
                        break;
                    case 1:
                        txt11.setBackgroundColor(cinza);
                        break;
                    case 2:
                        txt12.setBackgroundColor(cinza);
                        break;
                }
                break;
            case 2:
                switch (posY){
                    case 0:
                        txt20.setBackgroundColor(cinza);
                        break;
                    case 1:
                        txt21.setBackgroundColor(cinza);
                        break;
                    case 2:
                        txt22.setBackgroundColor(cinza);
                        break;
                }
                break;
        }
    }

    public void checkTouchInCell(TextView txt, int x, int y){
        if (androidWillPlay && cells[x][y] == 0){
            cells[x][y] = 1;
            txt.setText("X");
            androidWillPlay = false;
            checkFinishGame();
        }
    }

    @OnTouch(R.id.txt00)
    public boolean touch00(){
        checkTouchInCell(txt00, 0, 0);
        return true;
    }

    @OnTouch(R.id.txt01)
    public boolean touch01(){
        checkTouchInCell(txt01, 0, 1);
        return true;
    }

    @OnTouch(R.id.txt02)
    public boolean touch02(){
        checkTouchInCell(txt02, 0, 2);
        return true;
    }

    @OnTouch(R.id.txt10)
    public boolean touch10(){
        checkTouchInCell(txt10, 1, 0);
        return true;
    }

    @OnTouch(R.id.txt11)
    public boolean touch11(){
        checkTouchInCell(txt11, 1, 1);
        return true;
    }

    @OnTouch(R.id.txt12)
    public boolean touch12(){
        checkTouchInCell(txt12, 1, 2);
        return true;
    }

    @OnTouch(R.id.txt20)
    public boolean touch20(){
        checkTouchInCell(txt20, 2, 0);
        return true;
    }

    @OnTouch(R.id.txt21)
    public boolean touch21(){
        checkTouchInCell(txt21, 2, 1);
        return true;
    }

    @OnTouch(R.id.txt22)
    public boolean touch22(){
        checkTouchInCell(txt22, 2, 2);
        return true;
    }

    public void checkFinishGame(){
        if (cells[0][0] != 0 && cells[0][0] == cells[0][1] && cells[0][1] == cells[0][2]){
            //jogo da velha na primeira linha hoizontal
            pubnub.publish().channel("finishGame").message((cells[0][0] == 1 ? "Android" : "Arduino") + " vencedor")
                    .async(callback);
        } else if (cells[1][0] != 0 && cells[1][0] == cells[1][1] && cells[1][1] == cells[1][2]){
            //jogo da velha na segunda linha horizontal
            pubnub.publish().channel("finishGame").message((cells[1][0] == 1 ? "Android" : "Arduino") + " vencedor")
                    .async(callback);
        } else if (cells[2][0] != 0 && cells[2][0] == cells[2][1] && cells[2][1] == cells[2][2]){
            //jogo da velha na terceira linha horizontal
            pubnub.publish().channel("finishGame").message((cells[2][0] == 1 ? "Android" : "Arduino") + " vencedor")
                    .async(callback);
        } else if (cells[0][0] != 0 && cells[0][0] == cells[1][0] && cells[1][0] == cells[2][0]){
            //jogo da velha na primeira linha vertical
            pubnub.publish().channel("finishGame").message((cells[0][0] == 1 ? "Android" : "Arduino") + " vencedor")
                    .async(callback);
        } else if (cells[0][1] != 0 && cells[0][1] == cells[1][1] && cells[1][1] == cells[2][1]){
            //jogo da velha na segunda linha vertical
            pubnub.publish().channel("finishGame").message((cells[0][1] == 1 ? "Android" : "Arduino") + " vencedor")
                    .async(callback);
        } else if (cells[0][2] != 0 && cells[0][2] == cells[1][2] && cells[1][1] == cells[2][2]){
            //jogo da velha na terceira linha vertical
            pubnub.publish().channel("finishGame").message((cells[0][2] == 1 ? "Android" : "Arduino") + " vencedor")
                    .async(callback);
        } else if (cells[0][0] != 0 && cells[0][0] == cells[1][1] && cells[1][1] == cells[2][2]){
            //jogo da velha na coluna de lado \
            pubnub.publish().channel("finishGame").message((cells[0][0] == 1 ? "Android" : "Arduino") + " vencedor")
                    .async(callback);
        } else if (cells[0][2] != 0 && cells[0][2] == cells[1][1] && cells[1][1] == cells[2][0]){
            //jogo da velha na coluna de lado /
            pubnub.publish().channel("finishGame").message((cells[0][2] == 1 ? "Android" : "Arduino") + " vencedor")
                    .async(callback);
        }
    }

    PNCallback callback = new PNCallback() {
        @Override
        public void onResponse(Object result, PNStatus status) {

        }
    };

    @OnClick(R.id.btnReset)
    public void reset(){
        posX = posY = 0;

        txt00.setText("");
        txt01.setText("");
        txt02.setText("");
        txt10.setText("");
        txt11.setText("");
        txt12.setText("");
        txt20.setText("");
        txt21.setText("");
        txt22.setText("");

        lastEvent = 0;

        cells = new int[3][3];
        androidWillPlay = true;

        repaintCells();

        pubnub.publish().channel("resetGame").message("reset").async(callback);
    }


}
