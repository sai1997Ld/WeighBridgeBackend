package com.weighbridge.config;

import com.weighbridge.camera.services.FrameCaptureService;
import com.weighbridge.camera.services.WeighbridgeService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final FrameCaptureService frameCaptureService;
    private final WeighbridgeService weighbridgeService;

    public WebSocketConfig(FrameCaptureService frameCaptureService, WeighbridgeService weighbridgeService) {
        this.frameCaptureService = frameCaptureService;
        this.weighbridgeService = weighbridgeService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Register handlers for RTSP streams
        String[] rtspUrls = {
                "rtsp://admin:Techn0l0gy@172.16.20.90:554/cam/realmonitor?channel=1&subtype=0",
                "rtsp://admin:Techn0l0gy@172.16.20.90:554/cam/realmonitor?channel=1&subtype=0",
                "rtsp://admin:Techn0l0gy@172.16.20.90:554/cam/realmonitor?channel=1&subtype=0",
                "rtsp://admin:Techn0l0gy@172.16.20.90:554/cam/realmonitor?channel=1&subtype=0",
                "rtsp://admin:Techn0l0gy@172.16.20.90:554/cam/realmonitor?channel=1&subtype=0"
                // Add more RTSP URLs as needed
        };
        for (int i = 0; i < rtspUrls.length; i++) {
            registry.addHandler(new FrameWebSocketHandler(frameCaptureService, rtspUrls[i]), "/ws/frame" + (i + 1))
                    .setAllowedOrigins("*");
        }

        // Register handler for weighbridge data
        registry.addHandler(new WeighbridgeWebSocketHandler(weighbridgeService), "/ws/weight")
                .setAllowedOrigins("*");
    }
}
