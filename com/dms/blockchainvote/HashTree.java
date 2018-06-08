package com.dms.blockchainvote;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ArrayList;

public class HashTree {
    private ArrayList<String> voteList;
    private ArrayList<String> hashTree;

    /**
     * Constructor of HashTree
     *
     * @param voteData Data for Voting.
     */
    public HashTree(List<String> voteData) {
        //initialize Variables
        this.voteList = new ArrayList<>();
        this.hashTree = new ArrayList<>();

        //copy data
        this.voteList.addAll(voteData);

        //calculate hashTree
        calcHashTree();
    }

    /**
     * get Voting Data
     *
     * @return Copy of Voting Data
     */
    public List<String> getData() {
        return (List<String>) voteList.clone();
    }

    private void calcHashTree() {
        ArrayList<String> oldHash = voteList;
        ArrayList<String> newHash = new ArrayList<>();
        //empty hashTree
        hashTree.clear();

        //Calc with just 2 vote
    }

    /**
     * Hash
     *
     * @return
     */
    public String getRootHash() {
        return hashTree.get(hashTree.size() - 1);
    }


}