package com.alvarezaaronai.sed;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends AsyncTask<Void, Void, Void> {

    private static Socket s;

    @Override
    protected Void doInBackground(Void... Void) {
        try{
            s = new Socket("3.12.241.68", 8800);
            Send("hello");
        }catch(UnknownHostException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void Send(String message){
        try {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println(message);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
