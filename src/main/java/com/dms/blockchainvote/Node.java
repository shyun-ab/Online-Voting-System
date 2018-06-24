package com.dms.blockchainvote;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.List;

public class Node extends Thread{
    private String currentBlock;
    private KeyMaker maker;
    private List<String> voteList;
    private List<String> publicKeys;
    private static final int blockSize = 2;

    private Client client = new Client();


    public Node(){
        currentBlock = "0";
        voteList = new ArrayList<>();
        publicKeys = new ArrayList<>();
    }

    @Override
    public void run(){
        ServerSocket mServerSocket;
        Socket mSocket;
        BufferedReader mIn;
        PrintWriter mOut;
        try {
            mServerSocket = new ServerSocket(9001);
            while(true) {
                mSocket = mServerSocket.accept();

                mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                mOut = new PrintWriter(mSocket.getOutputStream());

                //socket function.
                String data;
                while ((data = mIn.readLine()) != null) {
                    switch (data) {
                        case "update":
                            currentBlock = mIn.readLine();
                            break;
                        case "request":
                            String requestBlockHash = mIn.readLine();
                            System.out.println("requsetblock : " + requestBlockHash);
                            Block requestBlock = Block.loadBlock(requestBlockHash);
                            if(requestBlock == null){
                                mOut.println("null");
                            }else {
                                Gson gson = new Gson();
                                mOut.println(gson.toJson(requestBlock));
                            }
                            mOut.flush();
                            break;
                        case "check" :
                            mOut.println(this.currentBlock);
                            mOut.flush();
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void createBlock(){
        Block block = new Block(currentBlock, new Date(), voteList, publicKeys);
        currentBlock = block.getBlockHash();
        voteList = new ArrayList<>();
        publicKeys = new ArrayList<>();
        client.updateCurrentBlock(currentBlock);
    }

    public void vote(String voteData){
    	maker = new KeyMaker();
    	String encodedVoteData = maker.encode(voteData);
    	String sPublicKey = maker.getSPublicKey();
        voteList.add(encodedVoteData);
        publicKeys.add(sPublicKey);
        if(voteList.size() >= blockSize){
            createBlock();
        }
    }

    /**
     * get Winner of this vote.
     * @return winner as String;
     */
    public String statVote(){
        if(!voteList.isEmpty()){
            createBlock();
        }
        KeyLoader loader;
        Block block = Block.loadBlock(currentBlock);
        List<String> encodedVoteData = block.getVoteData();
        List<String> sPublicKeys = block.getSPublicKeys();
        List<String> voteData = new ArrayList<>();
        HashMap<String, Integer> statTable = new HashMap<>();
        String winner = "";
        
        for(int i = 0; i < encodedVoteData.size(); i++) {
        	loader = new KeyLoader(sPublicKeys.get(i));
        	voteData.add(loader.decode(encodedVoteData.get(i)));
        }

        for(String data : voteData){
            if(statTable.containsKey(data)){
               statTable.put(data, statTable.get(data) +1);
            }else{
                statTable.put(data, 1);
            }
        }

        int max = 0;
        for(Entry<String, Integer> cadi : statTable.entrySet()){
            if(cadi.getValue() > max){
                winner = cadi.getKey();
                max = cadi.getValue();
            }
        }

        return winner;
    }

    public boolean hasValidBlock(){
        Block block = Block.loadBlock(currentBlock);
        return block.isValid();
    }
}
