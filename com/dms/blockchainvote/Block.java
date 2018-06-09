package com.dms.blockchainvote;

import java.util.Date;
import java.util.List;


public class Block {

    private String currentHash;
    private String prevBlockHash;
    private Date time;
    private HashTree voteData;

    /**
     * Constructor of Block
     * @param prevBlockHash Hash of previous Block
     * @param time Block generate time
     * @param voteData list of vote data
     */
    public Block(String prevBlockHash, Date time, List<String> voteData){
        this.prevBlockHash = prevBlockHash;
        this.time = time;
        this.voteData = new HashTree(voteData);
        this.currentHash = calcBlockHash();
    }

    /**
     * isValid Block Hash Values
     * @return validity of Block
     */
    public boolean isValid(){
        if(currentHash.equals(calcBlockHash())){
            //check prevBlock
            return true;
        }
        return false;
    }

    /**
     * Calculate Block Hash
     * @return Block Hash value as String
     */
    private String calcBlockHash(){
        String block = prevBlockHash + time.toString() + voteData.getRootHash();
        currentHash = Hash.hashSHA256(block);
        return currentHash;
    }

    public HashTree getVoteData(){
        return voteData;
    }
}
