package com.dms.blockchainvote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;


public class Block {

    private String blockHash;
    private String prevBlockHash;
    private Date time;
    private HashTree voteData;

    /**
     * Constructor of Block
     *
     * @param prevBlockHash Hash of previous Block
     * @param time          Block generate time
     * @param voteData      list of vote data
     */
    public Block(String prevBlockHash, Date time, List<String> voteData) {
        this.prevBlockHash = prevBlockHash;
        this.time = time;
        this.voteData = new HashTree(voteData);
        this.blockHash = calcBlockHash();
        this.saveBlock();

        System.out.println("===== New Block Generated =====");
        System.out.println("Block Hash : " + blockHash);
        System.out.println("Previous Block Hash : " + this.prevBlockHash);
        System.out.println("Created Time : " + this.time);
        System.out.println("Data Root Hash : " + this.voteData.getRootHash());
        System.out.println("================================");
    }

    /**
     * Load Block from blockHash
     *
     * @param blockHash blockHash to be loaded
     * @return Loaded Block
     */
    public static Block loadBlock(String blockHash) {
        Block block;
        Gson gson = new Gson();
        Path path = FileSystems.getDefault().getPath("block/" + blockHash);
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            block = gson.fromJson(br, Block.class);
        } catch (IOException e) {
            block = null;
        }
        return block;
    }

    public void saveBlock(){
        Path path = FileSystems.getDefault().getPath("block/" + blockHash);

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(this));
        } catch (IOException e) {
            //check.
        }
    }

    /**
     * isValid Block Hash Values
     *
     * @return validity of Block
     */
    public boolean isValid() {
        if (blockHash.equals(calcBlockHash())) {
            if(prevBlockHash.equals("0")){
                return true;
            }
            return Block.loadBlock(prevBlockHash).isValid();
        }
        return false;
    }

    /**
     * Calculate Block Hash
     */
    private String calcBlockHash() {
        String block = prevBlockHash + time.toString() + voteData.getRootHash();
        return Hash.hashSHA256(block);
    }

    public HashTree getVoteData() {
        return voteData;
    }

    public String getBlockHash() {
        return blockHash;
    }
}
