package com.dima.converter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class RealUrlClient implements UrlClient {

    public InputStream getResponse(String url) throws IOException {
        URL urlObj = new URL(url);
        URLConnection connection = urlObj.openConnection();
        return connection.getInputStream();
    }
}
