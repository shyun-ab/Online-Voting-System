package com.dms.blockchainvote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Node {
    private String currentBlock;
    private List<String> voteList;

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
        if(voteList.size() >= 2){
            createBlock();
            voteList = new ArrayList<>();
        }
    }
}
