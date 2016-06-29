package com.dima.converter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

@Component
public class RealUrlClient implements UrlClient {

    private static final Logger logger = LoggerFactory.getLogger(RealUrlClient.class);

    @Value("${proxy.host:}")
    private String proxyHost;

    @Value("${proxy.port:-1}")
    private int proxyPort;

    public InputStream getResponse(String url) throws IOException {
        logger.debug("Sending GET request to " + url);
        URL urlObj = new URL(url);

        URLConnection connection;
        if (isProxyConfigured()) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = urlObj.openConnection(proxy);
        } else {
            connection = urlObj.openConnection();
        }
        return connection.getInputStream();
    }

    private boolean isProxyConfigured(){
        return proxyHost != null && !proxyHost.isEmpty() && proxyPort != -1;
    }
}
