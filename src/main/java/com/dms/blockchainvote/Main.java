package com.dms.blockchainvote;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Node node = new Node(Integer.parseInt(args[0]));
        node.start();

        String str;
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("Command > ");
                str = sc.nextLine();
                switch (str) {
                    case "vote":
                        System.out.print("Vote to  > ");
                        node.vote(sc.nextLine());
                        break;
                    case "stat":
                        HashMap<String, Integer> stat = node.statVote();
                        System.out.println("=====Result=====");
                        for(Map.Entry<String, Integer> entry : stat.entrySet()){
                            System.out.println(entry.getKey() + "-" + entry.getValue());
                        }
                        System.out.println("================");
                        break;
                    case "exit" :
                        System.out.println("Exit");
                        System.exit(-1);
                        return;
                    default:
                        System.out.println("check input");
                        break;
                }
            }
        }
    }
}
