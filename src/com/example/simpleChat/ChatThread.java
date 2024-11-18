package com.example.simpleChat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatThread extends Thread{
    private String name;
    private BufferedReader br;
    private PrintWriter pw;
    private Socket socket;
    List<ChatThread> list; // 다른 클라이언트들에게 보낼 자기 자신의 정보를 담을 공유 객체 생성

    public ChatThread(Socket socket, List<ChatThread> list) throws Exception{
        this.socket = socket;
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 읽어오기
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())); // 쓰기
        this.br = br;
        this.pw = pw;
        this.name = br.readLine(); // 첫 한줄을 읽어서 이름으로 저장한다.
        this.list = list; // 공유객체를 받아서 연결
        this.list.add(this); // 공유객체에 자신(Client)을 담는다.
    }

    // 들어온 메시지를 쓰기
    public void sendMessage(String msg){
        pw.println(msg);
        pw.flush();
    }

    // broadcast
    // ChatThread는 사용자가 보낸 메시지를 읽어들여서
    // 접속된 모든 클라이언트에게 메시지를 보낸다.
    private void broadcast(String msg, boolean includeMe){
        List<ChatThread> chatThreads = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i++) {
            chatThreads.add(list.get(i));
        }

        try {
            for (int i = 0; i < chatThreads.size(); i++) {
                ChatThread ct = chatThreads.get(i);
                if(!includeMe){ // 나를 포함하지 않는다.
                    if(ct == this){
                        continue;
                    }
                }
                ct.sendMessage(msg);
            }
        }catch (Exception ex){
            System.out.println("///");
        }
    }

    @Override
    public void run() {
        try {
            // 나를 제외한 모든 사용자에게 "00님이 연결되었습니다."
            // 현재 ChatThread를 제외하고 보낸다.
            broadcast(name + "님이 연결되었습니다.", false);

            String line = null;
            while ((line = br.readLine()) != null){
                if("/quit".equals(line)){
                    break;
                }
                // 나를 포함한 ChatThread에게 메시지를 보낸다.(채팅내역)
                broadcast(name + " : " + line,true);
            }

            // 내가 연결을 끊었을 때(while문을 벗어났을때)
            broadcast(name + "님이 연결이 끊어졌습니다.", false);
            this.list.remove(this); // 연결이 끊어지면 자기자신을 제거

            try {
                br.close();
                pw.close();
                socket.close();
            }catch (Exception ex){
            }

        }catch (Exception ex){
        }
    }

}
