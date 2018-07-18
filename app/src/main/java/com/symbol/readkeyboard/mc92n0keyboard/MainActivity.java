package com.symbol.readkeyboard.mc92n0keyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvKeyboard = (TextView) findViewById(R.id.txtKeyboard);
        try{
            String result = runShellCommand("cat /sys/symbol-keypad/keypad-0/properties/name");
            if (!result.isEmpty())
            {
                tvKeyboard.setText(result);

                File myFile = new File("/sdcard/keyconfig.txt");
                myFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
                myOutWriter.append(result);
                myOutWriter.close();
                fOut.close();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String runShellCommand(String command) throws Exception {

        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStreamReader reader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader);
            int numRead;
            char[] buffer = new char[5000];
            StringBuffer commandOutput = new StringBuffer();
            while ((numRead = bufferedReader.read(buffer)) > 0) {
                commandOutput.append(buffer, 0, numRead);
            }
            bufferedReader.close();
            process.waitFor();

            return commandOutput.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
