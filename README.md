<h1>simpleChat</h1>

1단계. 채팅 프로그램

1. java 패키지명.ChatClient 닉네임 [enter]
- 서버에 접속을 한다.
- 클라이언트는 닉네임을 서버에게 전송한다.
- ex> 홍길동[enter]
2. 접속한 모든 사용자에게 "홍길동님이 접속하였습니다."
3. 채팅을 입력한 후 [enter]를 입력하면 모든 사용자(나 포함)에게 내 메시지가 전달된다.
4. /quit를 입력하면 연결이 끊어진다. 연결이 끊어질 때 모든 사용자에게 "홍길동님이 연결을 끊었습니다."라는 메시지 출력한다.
5. 강제로 연결을 끊었을 때 (ex>프로그램 강제종료)모든 사용자에게 "홍길동님이 연결을 끊었습니다."라는 메시지 출력한다.


<h2>프로그램 구조</h2>
<b>서버 입장</b>

- 사용자가 접속할 때마다 ChatThread를 만든다. 
- ChatThread는 클라이언트와 통신을 하는 용도. 
ex>클라이언트가 10개면 ChatThread도 10개 생성.
- 클라이언트 접속이 끊어지면 하나의 ChatThread도 종료된다.
- ChatThread는 사용자가 보내준 chat메시지를 현재 접속한 모든 사용자에게 보낸다.
- ChatThread는 현재 접속한 모든 사용자 연결 정보 or 출력객체를 알아야 한다.
