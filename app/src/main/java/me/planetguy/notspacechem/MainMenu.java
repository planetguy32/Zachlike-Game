package me.planetguy.notspacechem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        hideNavigationBar();
    }

    protected void onResume() {
        super.onResume();
        hideNavigationBar();
        Button b = findViewById(R.id.button2);
        Button instruct = findViewById(R.id.button);
        final Context context = this;
        b.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent game = new Intent(context, MainActivity.class);
                startActivity(game);
            }
        });

        instruct.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent howToPlay = new Intent(context, Instructions.class);
                startActivity(howToPlay);
            }
        });
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
}
