package com.lumiamuyu.shopping.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
    private static Properties properties=new Properties();

    static {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/**
 * 读取配置文件中的ImageHost
 * */
    public static String readByKey(String key){
        return properties.getProperty(key);
    }
}

