package me.planetguy.notspacechem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{

    private Button[][] grid = new Button[8][10];
    private LinearLayout myLayout = null;
    float x1, x2, y1, y2, dx, dy;
    String direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideNavigationBar();

        myLayout = findViewById(R.id.myLayout);

        // Get references to the buttons (the grid)
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 10; j++){
                String gridID = "grid_" + i + j;
                int resID = getResources().getIdentifier(gridID, "id", getPackageName());
                grid[i][j] = findViewById(resID);
                //grid[i][j].setOnClickListener(this);
                grid[i][j].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                            x1 = motionEvent.getX();
                            y1 = motionEvent.getY();
                        } else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                            x2 = motionEvent.getX();
                            y2 = motionEvent.getY();
                            dx = x2-x1;
                            dy = y2-y1;
                        }

                        if(Math.abs(dx) > Math.abs(dy)) {
                            if(dx>0){
                                direction = "right";
                                Log.d("CHECK_D", direction);
                                ((Button) view).setText(">");
                            }
                            else{
                                direction = "left";
                                Log.d("CHECK_D", direction);
                                ((Button) view).setText("<");
                            }
                        } else {
                            if(dy>0){
                                direction = "down";
                                Log.d("CHECK_D", direction);
                                ((Button) view).setText("v");
                            }
                            else{
                                direction = "up";
                                Log.d("CHECK_D", direction);
                                ((Button) view).setText("^");
                            }

                        }

                        return true;
                    }
                });
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    private void hideNavigationBar(){
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onClick(View view) {
        if(!((Button) view).getText().toString().equals("")){
            return;
        }

        ((Button) view).setText("8=D~~");
    }
}
