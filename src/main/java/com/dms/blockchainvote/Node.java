package com.dms.blockchainvote;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.List;

public class Node {
    private String currentBlock;
    private List<String> voteList;
    private static final int blockSize = 2;

    public Node(){
        currentBlock = "0";
        voteList = new ArrayList<>();
    }

    public void createBlock(){
        Block block = new Block(currentBlock, new Date(), voteList);
        currentBlock = block.getBlockHash();
    }

    public void vote(String voteData){
        voteList.add(voteData);
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
        Block block = Block.loadBlock(currentBlock);
        List<String> voteData = block.getVoteData();
        HashMap<String, Integer> statTable = new HashMap<>();
        String winner = "";

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
