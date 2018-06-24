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
    private static Socket mSocket;
    private static BufferedReader mIn;
    private static PrintWriter mOut;

    private static void connect(String server){
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

    private static void disconnect(){
        try{
            mSocket.close();
        } catch (IOException e){

        }
    }

    public static void updateCurrentBlock(String currentBlock){
        String[] serverList = loadServerList();
        for(String server : serverList){
            connect(server);
            mOut.println("update");
            mOut.println(currentBlock);
            mOut.flush();
            disconnect();
        }
    }

    public static Block requestBlock(String blockHash){
        String[] serverList = loadServerList();
        for(String server : serverList){
            connect(server);
            mOut.println("request");
            mOut.println(blockHash);
            mOut.flush();
            try {
                String result = mIn.readLine();
                disconnect();
                if(!result.equals("null")){
                    Gson gson = new Gson();
                    Block block = gson.fromJson(result, Block.class);
                    block.saveBlock();
                    return block;
                }
            }catch (IOException e){

            }
        }
        return null;
    }

    public static String checkBlock(){
        String[] serverList = loadServerList();
        for(String server: serverList){
            connect(server);
            mOut.println("check");
            mOut.flush();
            disconnect();
        }

        return "";
    }

    private static String[] loadServerList(){
        String[] serverList;
        Gson gson = new Gson();
        Path path = FileSystems.getDefault().getPath("server.json");
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            serverList = gson.fromJson(br, String[].class);
        } catch (IOException e) {
            serverList = null;
        }
        return serverList;
    }
}
