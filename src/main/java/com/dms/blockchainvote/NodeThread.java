package com.dms.blockchainvote;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NodeThread extends Thread{
    private Socket socket;
    private Node node;

    public NodeThread(Socket socket, Node node){
        this.socket = socket;
        this.node = node;
    }

    public void run(){
        try {
            BufferedReader mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter mOut = new PrintWriter(socket.getOutputStream());

            //socket function.
            String data;
            while ((data = mIn.readLine()) != null) {
                switch (data) {
                    case "update":
                        node.setCurrentBlock(mIn.readLine());
                        Block.loadBlock(node.getCurrentBlock());
                        break;
                    case "request":
                        String requestBlockHash = mIn.readLine();
                        Block requestBlock = Block.loadLocalBlock(requestBlockHash);
                        if (requestBlock == null) {
                            mOut.println("null");
                        } else {
                            Gson gson = new Gson();
                            mOut.println(gson.toJson(requestBlock));
                        }
                        mOut.flush();
                        break;
                    case "check":
                        mOut.println(node.getCurrentBlock());
                        mOut.flush();
                        break;
                    default:
                        break;
                }
            }

            socket.close();
        }catch (IOException e){

        }
    }
}
