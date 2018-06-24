package com.dms.blockchainvote;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client extends Thread {
    private Socket mSocket;
    private BufferedReader mIn;
    private PrintWriter mOut;

    public Client(){
    }

    private void connect(String server){
        try {
            if(!server.contains(":")){
                return;
            }
            String[] addr = server.split(":");
            mSocket = new Socket(addr[0], Integer.parseInt(addr[1]));
            mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mOut = new PrintWriter(mSocket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void disconnect(){
        try{
            mSocket.close();
        } catch (IOException e){

        }
    }

    public void updateCurrentBlock(String currentBlock){
        String[] serverList = loadServerList();
        for(String server : serverList){
            connect(server);
            mOut.println("update");
            mOut.println(currentBlock);
            mOut.flush();
            disconnect();
        }
    }

    public Node requestBlock(String blockHash){
        String[] serverList = loadServerList();
        for(String server : serverList){
            connect(server);
            mOut.println("request");
            mOut.println(blockHash);
            mOut.flush();
            try {
                String result = mIn.readLine();
                if(!result.equals("null")){
                    Gson gson = new Gson();
                    return gson.fromJson(result, Node.class);
                }
            }catch (IOException e){

            }
        }
        return null;
    }

    public String checkBlock(){
        String[] serverList = loadServerList();
        for(String server: serverList){
            connect(server);
            mOut.println("check");
            mOut.flush();
        }

        return "";
    }

    private String[] loadServerList(){
        String[] serverlist;
        Gson gson = new Gson();
        Path path = FileSystems.getDefault().getPath("server.json");
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            serverlist = gson.fromJson(br, String[].class);
        } catch (IOException e) {
            serverlist = null;
        }
        return serverlist;
    }
}
