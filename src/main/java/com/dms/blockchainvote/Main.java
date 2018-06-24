package com.dms.blockchainvote;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Node node = new Node();
        node.start();

        String str;
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                str = sc.nextLine();
                switch (str) {
                    case "vote":
                        node.vote(sc.nextLine());
                        break;
                    case "stat":
                        System.out.println(node.statVote());
                        break;
                    case "exit" :
                        return;
                    default:
                        System.out.println("check input");
                        break;
                }
            }
        }
    }
}
