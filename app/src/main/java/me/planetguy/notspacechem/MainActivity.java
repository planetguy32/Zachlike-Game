package me.planetguy.notspacechem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import me.planetguy.notspacechem.simulation.Atom;
import me.planetguy.notspacechem.simulation.Board;
import me.planetguy.notspacechem.simulation.BoardSnapshot;
import me.planetguy.notspacechem.simulation.Direction;
import me.planetguy.notspacechem.simulation.Point;
import me.planetguy.notspacechem.simulation.Puzzle;
import me.planetguy.notspacechem.simulation.Symbol;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private Button[][] grid = new Button[8][10];
    //private LinearLayout myLayout = null;
    private float x1, x2, y1, y2, dx, dy;
    private String direction;
    private GestureDetector gestureDetector;
    private Board board;

    private BoardSnapshot runningSnapshot;

    private DisplayMode currentDisplayMode=DisplayMode.RED;

    private enum DisplayMode{
        RED,
        BLUE,
        BACKGROUND,
        SIM
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public String getLabelInSimMode(int x, int y){
        boolean[] areWalkersPresent=new boolean[runningSnapshot.walkerPos.length];
        for(int ch=0; ch<areWalkersPresent.length; ch++){
            areWalkersPresent[ch]=runningSnapshot.walkerPos[ch].equals(new Point(x, y));
        }

        Atom atomHere=runningSnapshot.atoms.get(new Point(x, y));

        String atomString=(atomHere == null) ? "" : atomHere.toString().toLowerCase();

        String result="";

        if(areWalkersPresent[0])
            result+=showWalker(0, '(', ')', atomString, areWalkersPresent);
        if(areWalkersPresent[1])
            result+=showWalker(1, '[', ']', atomString, areWalkersPresent);

        if (!(runningSnapshot.walkerIsCarrying[0] || runningSnapshot.walkerIsCarrying[1])) {
            result +=" "+atomString;
        }
        return result;
    }

    private String showWalker(int index, char open, char close, String atom, boolean[] areWalkersPresent){
        if(runningSnapshot.walkerIsCarrying[index]){
            return  (open + atom +" " + show(runningSnapshot.walkerHeadings[0]))+ close;
        } else {
            return new StringBuilder()
                    .append(open)
                    .append(show(runningSnapshot.walkerHeadings[0]))
                    .append(close).toString();
        }
    }

    private char show(Direction arrow){
        if(arrow != null)
            return new char[]{'>', 'v', '<', '^'}[arrow.ordinal()];
        else
            return ' ';
    }

    public void redraw(){
        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid.length; j++){
                if(currentDisplayMode == DisplayMode.RED || currentDisplayMode==DisplayMode.BLUE){
                    int index=(currentDisplayMode==DisplayMode.RED) ? 0 : 1;
                    Direction arrow = board.arrows[j][i][index];
                    char arrowChar=show(arrow);

                    Symbol sym=board.symbols[j][i][index];

                    String symbolStr="";
                    if(sym!=null){
                        symbolStr=sym.name().toLowerCase();
                    }

                    grid[i][j].setText(arrowChar + " " + symbolStr);
                } else if(currentDisplayMode == DisplayMode.SIM){
                    grid[i][j].setText(getLabelInSimMode(j, i));
                }
            }
        }
    }

    public boolean chooseNewDrawMode(MenuItem item){
        switch(item.getItemId()){
            case R.id.optbar_red:
                currentDisplayMode=DisplayMode.RED;
                return true;
            case R.id.optbar_blue:
                currentDisplayMode=DisplayMode.BLUE;
                return true;
            case R.id.optbar_bg:
                currentDisplayMode=DisplayMode.BACKGROUND;
                return true;
            case R.id.optbar_step:
                if(runningSnapshot == null){
                    runningSnapshot=new BoardSnapshot(board);
                }
                runningSnapshot.step();
                currentDisplayMode=DisplayMode.SIM;
                return true;
            case R.id.optbar_stop:
                runningSnapshot=null;
                currentDisplayMode=DisplayMode.RED;
                return true;
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        chooseNewDrawMode(item);
        redraw();
        return true;
    }

    public void setArrow(int finalI, int finalJ, Direction dir){
        //No editing mid-run
        if(runningSnapshot!=null)
            return;

        if(currentDisplayMode == DisplayMode.RED){
            board.arrows[finalJ][finalI][0]=dir;
        } else if(currentDisplayMode == DisplayMode.BLUE){
            board.arrows[finalJ][finalI][1]=dir;
        }

        redraw();
    }

    public void setSymbol(int x, int y, Symbol dir){
        //No editing mid-run
        if(runningSnapshot!=null)
            return;

        if(currentDisplayMode == DisplayMode.RED){
            board.symbols[x][y][0]=dir;
        } else if(currentDisplayMode == DisplayMode.BLUE){
            board.symbols[x][y][1]=dir;
        }

        redraw();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hideNavigationBar();

        Puzzle puzzle=new Puzzle();

        //TODO get a puzzle

        board=new Board(puzzle);

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        //myLayout = findViewById(R.id.myLayout);

        // Get references to the buttons (the grid)
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 10; j++){
                String gridID = "grid_" + i + j;
                int resID = getResources().getIdentifier(gridID, "id", getPackageName());
                grid[i][j] = findViewById(resID);
                final int finalI = i;
                final int finalJ = j;
                grid[i][j].setTag(new Point(j, i));
                grid[i][j].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        if(gestureDetector.onTouchEvent(motionEvent)){
                            // Single Tap - cancel out what you have drawn
                            Log.d("CHECK_D", "cancel");
                            //((Button) view).setText(" ");
                            //hideNavigationBar();
                            showMenu(view, ((Button) view));

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
                                    setArrow(finalI, finalJ, Direction.RIGHT);
                                }
                                else{
                                    setArrow(finalI, finalJ, Direction.LEFT);
                                }
                            } else {
                                if(dy>0){
                                    setArrow(finalI, finalJ, Direction.DOWN);
                                }
                                else{
                                    setArrow(finalI, finalJ, Direction.UP);
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
        //hideNavigationBar();
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

    public void showMenu(final View v, final Button b)
    {
            final PopupMenu popup = new PopupMenu(this,v);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //Toast.makeText(MainActivity.this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                    Point tag=(Point) b.getTag();

                    Log.d("combine", tag+"");


                    if(item.getTitle().equals("Start")){
                        int channel;
                        if(currentDisplayMode==DisplayMode.RED)
                            channel=0;
                        else if(currentDisplayMode==DisplayMode.BLUE)
                            channel=1;
                        else
                            return false;
                        b.setText("Start");
                        for(int x=0; x<board.symbols.length; x++){
                            for(int y=0; y<board.symbols[0].length; y++) {
                                if(board.symbols[x][y][channel] == Symbol.START)
                                    board.symbols[x][y][channel] = null;
                            }
                        }
                        setSymbol(tag.x(), tag.y(), Symbol.START);
                    } else if(item.getTitle().equals("In")){
                        setSymbol(tag.x(), tag.y(), Symbol.IN);
                    } else if(item.getTitle().equals("Out")){
                        setSymbol(tag.x(), tag.y(), Symbol.OUT);
                    } else if(item.getTitle().equals("Grab")){
                        setSymbol(tag.x(), tag.y(), Symbol.GRAB);
                    } else if(item.getTitle().equals("Drop")){
                        setSymbol(tag.x(), tag.y(), Symbol.DROP);
                    } else if(item.getTitle().equals("Bond")){
                        setSymbol(tag.x(), tag.y(), Symbol.BOND);
                    } else if(item.getTitle().equals("Unbond")){
                        setSymbol(tag.x(), tag.y(), Symbol.UNBOND);
                    } else if(item.getTitle().equals("Remove")){
                        int channel;
                        if(currentDisplayMode==DisplayMode.RED)
                            channel=0;
                        else if(currentDisplayMode==DisplayMode.BLUE)
                            channel=1;
                        else
                            return false;
                        board.symbols[tag.x()][tag.y()][channel]=null;
                        board.arrows[tag.x()][tag.y()][channel]=null;
                    }
                    redraw();
                    return true;
                }
            });

            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.button_menu, popup.getMenu());
            //hideNavigationBar();
            popup.show();

    }
}