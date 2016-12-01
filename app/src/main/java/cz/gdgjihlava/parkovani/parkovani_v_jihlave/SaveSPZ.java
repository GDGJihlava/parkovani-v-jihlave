package cz.gdgjihlava.parkovani.parkovani_v_jihlave;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SaveSPZ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_spz);


        SharedPreferences preferences = getSharedPreferences("Prefs", MODE_PRIVATE);
        String Spz = preferences.getString("Spz", "DEFAULT");





        final EditText editText = (EditText)findViewById(R.id.editText);

        if(!Spz.equals("DEFAULT")){
            editText.setText(Spz);
        }


        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SaveSPZ.this, "uloženo", Toast.LENGTH_SHORT).show();
                String Spz = editText.getText().toString();

                SharedPreferences.Editor preferences = getSharedPreferences("Prefs", MODE_PRIVATE).edit();
                preferences.putString("Spz", Spz);
                preferences.commit();

                editText.setText(Spz);



            }
        });
    }
}
