package com.example.simpleChat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServer {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(8888);
        List<ChatThread> list = Collections.synchronizedList(new ArrayList<>()); // list객체에 동시에 접근해 생기는 오류를 방지

        while (true){ // 클라이언트의 접속을 계속 받는다.
            Socket socket = serverSocket.accept(); // 클라이언트 접속
            ChatThread chatClient = new ChatThread(socket, list);
            chatClient.start(); // Thread 실행
        }// while




    } // main
}