package me.planetguy.notspacechem;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private Button[][] grid = new Button[8][10];
    //private LinearLayout myLayout = null;
    private float x1, x2, y1, y2, dx, dy;
    private String direction;
    private GestureDetector gestureDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideNavigationBar();

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        //myLayout = findViewById(R.id.myLayout);

        // Get references to the buttons (the grid)
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 10; j++){
                String gridID = "grid_" + i + j;
                int resID = getResources().getIdentifier(gridID, "id", getPackageName());
                grid[i][j] = findViewById(resID);
                grid[i][j].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        if(gestureDetector.onTouchEvent(motionEvent)){
                            // Single Tap - cancel out what you have drawn
                            Log.d("CHECK_D", "cancel");
                            ((Button) view).setText(" ");
                            showMenu( view);

                        } else {
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
                                    // Move Right - draw right arrow
                                    direction = "right";
                                    Log.d("CHECK_D", direction);
                                    ((Button) view).setText(">");
                                }
                                else{
                                    // Move Left - draw left arrow
                                    direction = "left";
                                    Log.d("CHECK_D", direction);
                                    ((Button) view).setText("<");
                                }
                            } else {
                                if(dy>0){
                                    // Move Down - draw down arrow
                                    direction = "down";
                                    Log.d("CHECK_D", direction);
                                    ((Button) view).setText("v");
                                }
                                else{
                                    // Move Up - draw up arrow
                                    direction = "up";
                                    Log.d("CHECK_D", direction);
                                    ((Button) view).setText("^");
                                }

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


    private class SingleTapConfirm extends SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

    public void showMenu(View v)
    {
        PopupMenu popup = new PopupMenu(this,v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.button_menu, popup.getMenu());
        hideNavigationBar();
        popup.show();

    }
}
