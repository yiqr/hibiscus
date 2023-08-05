package org.yiqr.hibiscus.server.system.config;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * @author rose
 * @create 2023/7/19 22:55
 */
@Component
public class CustomizationBean implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

   /**
    * 解决Undertow提示
    * UT026009: XNIO worker was not set on WebSocketDeploymentInfo, the default worker will be used
    * UT026010: Buffer pool was not set on WebSocketDeploymentInfo, the default pool will be used
    * 实际不解决不影响使用
    *
    * @param factory UndertowServletWebServerFactory
    */
   @Override
   public void customize(UndertowServletWebServerFactory factory) {
       factory.addDeploymentInfoCustomizers(deploymentInfo -> {
           WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
           webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(false, 1024));
           deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
       });
   }
}