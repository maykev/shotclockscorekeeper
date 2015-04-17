package software.spartacus.com.shotclockscorekeeper;

import software.spartacus.com.shotclockscorekeeper.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateMatchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_match);
        getActionBar().hide();

        Button buttonStartMatch = (Button)findViewById(R.id.buttonStartMatch);
        buttonStartMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMatch();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void startMatch() {
        EditText editTextPlayer1Name = (EditText)findViewById(R.id.editTextPlayer1Name);
        EditText editTextPlayer2Name = (EditText)findViewById(R.id.editTextPlayer2Name);
        EditText editTextPlayer1GamesRequired = (EditText)findViewById(R.id.editTextPlayer1GamesRequired);
        EditText editTextPlayer2GamesRequired = (EditText)findViewById(R.id.editTextPlayer2GamesRequired);

        Intent intent = new Intent(this, PlayMatchActivity.class);
        intent.putExtra("player1Name", editTextPlayer1Name.getText().toString());
        intent.putExtra("player2Name", editTextPlayer2Name.getText().toString());
        intent.putExtra("player1GamesRequired", editTextPlayer1GamesRequired.getText().toString());
        intent.putExtra("player2GamesRequired", editTextPlayer2GamesRequired.getText().toString());
        startActivity(intent);
    }
}
