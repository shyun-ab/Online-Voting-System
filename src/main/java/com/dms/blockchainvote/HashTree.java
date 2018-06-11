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

    /**
     * Calculate HashTree with HashFunction
     */
    private void calcHashTree() {
        ArrayList<String> oldHash = new ArrayList<>();
        ArrayList<String> newHash = new ArrayList<>();
        //empty hashTree
        hashTree.clear();


        int leafTreeLength = 1;
        while (leafTreeLength < voteList.size()) {
            leafTreeLength *= 2;
        }

        //Calculate
        for (int i = 0; i < leafTreeLength; i++) {
            if (i < voteList.size()) {
                oldHash.add(Hash.hashSHA256(voteList.get(i)));
            } else {
                oldHash.add("");
            }
        }
        hashTree.addAll(oldHash);

        while (leafTreeLength > 1) {
            for (int i = 0; i < leafTreeLength; i += 2) {
                newHash.add(Hash.hashSHA256(oldHash.get(i), oldHash.get(i + 1)));
            }

            hashTree.addAll(newHash);
            oldHash = newHash;
            newHash = new ArrayList<>();
            leafTreeLength /= 2;
        }
    }

    /**
     * get Hash of Tree Root
     *
     * @return hash of tree root
     */
    public String getRootHash() {
        return hashTree.get(hashTree.size() - 1);
    }
}