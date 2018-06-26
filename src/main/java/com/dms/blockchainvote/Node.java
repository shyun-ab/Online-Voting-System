package com.dms.blockchainvote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Node extends Thread{
    private String currentBlock;
    private KeyMaker maker;
    private List<String> voteList;
    private List<String> publicKeys;
    private int port;
    private static final int blockSize = 2;

    public Node(int port){
        currentBlock = "0";
        voteList = new ArrayList<>();
        publicKeys = new ArrayList<>();
        this.port = port;
    }

    @Override
    public void run(){
        ServerSocket mServerSocket;
        Socket mSocket;
        BufferedReader mIn;
        PrintWriter mOut;
        try {
            mServerSocket = new ServerSocket(port);
            while(true) {
                mSocket = mServerSocket.accept();
                NodeThread nodeThread = new NodeThread(mSocket, this);
                nodeThread.start();
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
        Client.updateCurrentBlock(currentBlock);
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
    public HashMap<String, Integer> statVote(){
        if(!voteList.isEmpty()){
            createBlock();
        }
        KeyLoader loader;
        Block block = Block.loadBlock(Client.checkBlock(currentBlock));
        List<String> encodedVoteData = block.getVoteData();
        List<String> sPublicKeys = block.getSPublicKeys();
        List<String> voteData = new ArrayList<>();
        HashMap<String, Integer> statTable = new HashMap<>();
        
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

        return statTable;
    }

    public boolean hasValidBlock(){
        Block block = Block.loadBlock(currentBlock);
        return block.isValid();
    }

    public String getCurrentBlock(){
        return currentBlock;
    }

    public void setCurrentBlock(String currentBlock){
        this.currentBlock = currentBlock;
    }
}
