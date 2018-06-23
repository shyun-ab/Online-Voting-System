package com.dms.blockchainvote;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.List;

public class Node {
    private String currentBlock;
    private KeyMaker maker;
    private List<String> voteList;
    private List<String> publicKeys;
    private static final int blockSize = 2;

    public Node(){
        currentBlock = "0";
        maker = new KeyMaker();
        voteList = new ArrayList<>();
        publicKeys = new ArrayList<>();
    }

    public void createBlock(){
        Block block = new Block(currentBlock, new Date(), voteList, publicKeys);
        currentBlock = block.getBlockHash();
    }

    public void vote(String voteData){
    	String encodedVoteData = maker.encode(voteData);
    	String sPublicKey = maker.getSPublicKey();
        voteList.add(encodedVoteData);
        publicKeys.add(sPublicKey);
        if(voteList.size() >= blockSize){
            createBlock();
            voteList = new ArrayList<>();
        }
    }

    /**
     * get Winner of this vote.
     * @return winner as String;
     */
    public String statVote(){
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
