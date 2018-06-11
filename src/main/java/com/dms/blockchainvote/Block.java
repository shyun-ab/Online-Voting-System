package com.dms.blockchainvote;

import java.util.Date;
import java.util.List;


public class Block {

    private String blockHash;
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
        this.blockHash = calcBlockHash();
    }

    /**
     * isValid Block Hash Values
     * @return validity of Block
     */
    public boolean isValid(){
        if(blockHash.equals(calcBlockHash())){
            //check prevBlock
            return true;
        }
        return false;
    }

    /**
     * Calculate Block Hash
     */
    private String calcBlockHash(){
        String block = prevBlockHash + time.toString() + voteData.getRootHash();
        return Hash.hashSHA256(block);
    }

    public HashTree getVoteData(){
        return voteData;
    }
    public String getBlockHash() {
        return blockHash;
    }
}
