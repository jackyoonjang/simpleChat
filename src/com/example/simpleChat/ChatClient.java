package com.example.simpleChat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) throws Exception{
        if(args.length!=1){ // args에 이름이 담겨 있지 않을 경우(최초실행) 사용법 출력
            System.out.println("사용법 : java com.example.simpleChat.ChatClient 닉네임");
            return;
        }
        String name = args[0]; // 닉네임을 담을 객체

        Socket socket = new Socket("127.0.0.1",8888); // 접속할 서버와 포트번호
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())); // 쓰기(서버에 보낼 메시지)

        // 서버에 닉네임 전송
        pw.println(name);
        pw.flush();

        // 백그라운드로 서버가 보내준 메시지를 읽어들여서 화면에 출력한다.
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 읽어오기(서버에서 보낸 메시지)
        InputThread inputThread = new InputThread(br);
        inputThread.start();

        // 클라이언트는 읽어들인 메시지를 서버에게 전송한다.
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        try{
            String line = null;
            while((line = keyboard.readLine()) != null){ // 자신이 작성한 메시지를 null이 아닐때 까지 읽는다.
                if("/quit".equals(line)) {
                    pw.println(line);
                    pw.flush();
                    break; // while문을 빠져나간다.
                }
                pw.println(line);
                pw.flush();
            }
        }catch (Exception ex){
//            System.out.println("...");
        }finally {

            try {
                br.close();
            }catch (Exception ex){

            }

            try {
                pw.close();
            }catch (Exception ex){

            }

            try {
                socket.close();
            }catch (Exception ex){

            }

        }


    }
}

class InputThread extends Thread{
    // 서버가 보낸 다른 클라이언트들의 메시지를 받아온다.
    BufferedReader br;
    public InputThread(BufferedReader br){
        this.br = br;
    }

    // 백그라운드에서 무한루프로 계속 읽어온다.
    @Override
    public void run() {
        try{
            String line = null;
            while((line = br.readLine()) != null){ // 다른 클라이언트가 보낸 메시지를 null이 아닐때 까지 읽는다.
                System.out.println(line); // 다른 클라이언트가 보낸 메시지를 출력.
            }
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println("...");
        }

    }
}