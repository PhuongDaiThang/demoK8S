package com.demok8s.wss;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TerminalHandler extends TextWebSocketHandler {
    private static final String VMUsername = "anhnd";
    private static final String VMhost = "192.168.42.115";
    private static final String VMpassword = "1234";

    private final Map<String, Session> sshSessions = new ConcurrentHashMap<>();
    private final Map<String, ChannelShell> channels = new ConcurrentHashMap<>();
    private final Map<String, Thread> outputReaders = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("✅ Client connected: " + session.getId() + ". Attempting SSH connection...");
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(VMUsername, VMhost, 22);
            sshSession.setPassword(VMpassword);
            sshSession.setConfig("StrictHostKeyChecking", "no");
            sshSession.connect(30000);
            System.out.println("✅ SSH Session connected for: " + session.getId());

            ChannelShell channel = (ChannelShell) sshSession.openChannel("shell");
            channel.setPtyType("xterm-256color");
            channel.connect(3000);
            System.out.println("✅ Shell channel connected for: " + session.getId());

            Thread outputReader = new Thread(() -> {
                try (InputStream in = channel.getInputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while (channel.isConnected() && session.isOpen() && (bytesRead = in.read(buffer)) != -1) {
                        session.sendMessage(new TextMessage(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8)));
                    }
                } catch (Exception e) {}
                finally { cleanup(session.getId()); }
            });
            outputReader.start();

            OutputStream out = channel.getOutputStream();
            String command = "stty echo\n";
            out.write(command.getBytes(StandardCharsets.UTF_8));
            out.flush();
            System.out.println("✅ Sent 'stty' command to configure shell.");

            sshSessions.put(session.getId(), sshSession);
            channels.put(session.getId(), channel);
            outputReaders.put(session.getId(), outputReader);

        } catch (Exception e) {
            e.printStackTrace();
            try { session.close(CloseStatus.SERVER_ERROR.withReason(e.getMessage())); } catch (Exception ignored) {}
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            ChannelShell channel = channels.get(session.getId());
            if (channel != null && channel.isConnected()) {
                OutputStream out = channel.getOutputStream();
                out.write(message.getPayload().getBytes(StandardCharsets.UTF_8));
                out.flush();
            }
        } catch (Exception e) {
            System.err.println("🚨 Error handling message for session " + session.getId() + ": " + e.getMessage());
            cleanup(session.getId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("❌ Client disconnected: " + session.getId() + " | Status: " + status);
        cleanup(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.err.println("🚨 Transport error for session " + session.getId() + ": " + exception.getMessage());
        cleanup(session.getId());
    }

    private void cleanup(String sessionId) {
        System.out.println("🧹 Cleaning up session: " + sessionId);
        Thread reader = outputReaders.remove(sessionId);
        if (reader != null) reader.interrupt();
        ChannelShell channel = channels.remove(sessionId);
        if (channel != null) channel.disconnect();
        Session sshSession = sshSessions.remove(sessionId);
        if (sshSession != null) sshSession.disconnect();
        System.out.println("✅ Cleanup complete for session: " + sessionId);
    }
}