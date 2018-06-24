package com.dms.blockchainvote;

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
                        System.out.println(node.statVote());
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
