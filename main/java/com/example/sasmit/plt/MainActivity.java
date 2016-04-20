package com.example.sasmit.plt;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText et = (EditText) findViewById(R.id.et);

        final TextView tv = (TextView) findViewById(R.id.tv);


        final Button button = (Button) findViewById(R.id.bt);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = et.getText().toString().trim();
                if(url.length() == 0)
                {
                    tv.setText("Invalid / Empty url entered:"+url);
                    return;
                }

                tv.setText(url);
                tv.append("\n");
                tv.append("-------------------------------------\n");
                String cmd[] = {"curl -o /dev/null -s -w %{time_namelookup} "+url,
                        "curl -o /dev/null -s -w %{time_connect} "+url,
                        "curl -o /dev/null -s -w %{time_pretransfer} "+url,
                        "curl -o /dev/null -s -w %{time_starttransfer} "+url,
                        "curl -o /dev/null -s -w %{size_download} "+url,
                        "curl -o /dev/null -s -w %{speed_download} "+url,
                        "curl -o /dev/null -s -w %{time_total} "+url};
                /*String cmd_lt = "curl -o /dev/null -s -w %{time_namelookup} "+et.getText().toString();
                String cmd_ct = "curl -o /dev/null -s -w %{time_connect} "+et.getText().toString();
                String cmd_pt = "curl -o /dev/null -s -w %{time_pretransfer} "+et.getText().toString();
                String cmd_st = "curl -o /dev/null -s -w %{time_starttransfer} "+et.getText().toString();
                String cmd_szd = "curl -o /dev/null -s -w %{size_download} "+et.getText().toString();
                String cmd_spd = "curl -o /dev/null -s -w %{speed_download} "+et.getText().toString();
                String cmd_tt = "curl -o /dev/null -s -w %{time_total} "+et.getText().toString();*/
                //time wget -pq --no-cache --delete-after www.growingcraft.com
                //String cmd = "curl -o /dev/null -s -w %{time_total} http://google.com";
                //String cmd="/bin/bash","-c","echo password| sudo -S ls"
                //tv.setText(url);
                //tv.append(url);
                int i=0;
                Process process=null;
                OutputStreamWriter osw = null;
                try {
                    for(i=0;i<cmd.length;i++)
                    {
                        process = Runtime.getRuntime().exec("su");
                        osw = new OutputStreamWriter(process.getOutputStream());
                        osw.write(cmd[i]);
                       /* osw.append(cmd_ct + "\n");
                        osw.append(cmd_pt + "\n");
                        osw.append(cmd_st + "\n");
                        osw.append(cmd_szd + "\n");
                        osw.append(cmd_spd + "\n");
                        osw.append(cmd_tt + "\n");*/
                        osw.flush();
                        osw.close();
                        //Process process = Runtime.getRuntime().exec(cmd);
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()));
                        int read;
                        char[] buffer = new char[4096];
                        StringBuffer output = new StringBuffer();
                        while ((read = reader.read(buffer)) > 0) {
                            output.append(buffer, 0, read);
                        }
                        reader.close();

                        // Waits for the command to finish.
                        process.waitFor();


                        String res = output.toString();
                        //tv.setText("PLT = " + res + " sec \n");
                        switch(i)
                        {
                            case 0:
                                tv.append("Lookup time: ");
                                tv.append(res);
                                tv.append(" sec\n");
                                break;
                            case 1:
                                tv.append("Connect time: ");
                                tv.append(res);
                                tv.append(" sec\n");
                                break;
                            case 2:
                                tv.append("Pretransfer time: ");
                                tv.append(res);
                                tv.append(" sec\n");
                                break;
                            case 3:
                                tv.append("Starttransfer time: ");
                                tv.append(res);
                                tv.append(" sec\n");
                                break;
                            case 4:
                                tv.append("Size download: ");
                                tv.append(res);
                                tv.append(" Bytes\n");
                                break;
                            case 5:
                                tv.append("Speed download: ");
                                tv.append(res);
                                tv.append(" Bytes/sec\n");
                                break;
                            case 6:
                                tv.append("Total time: ");
                                tv.append(res);
                                tv.append(" sec\n");
                                break;

                        }

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {

                    throw new RuntimeException(e);

                } finally {
                    if (osw != null) {
                        try {
                            osw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    if (process != null)
                        process.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               /* sbstdOut.append(ReadBufferedReader(new InputStreamReader(proc
                        .getInputStream())));
                sbstdErr.append(ReadBufferedReader(new InputStreamReader(proc
                        .getErrorStream())));
                if (proc.exitValue() != 0) {
                }*/
            }


        });
    }

}
