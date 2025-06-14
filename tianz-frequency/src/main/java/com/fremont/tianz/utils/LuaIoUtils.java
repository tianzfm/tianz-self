package com.fremont.tianz.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Lua脚本文件读取工具类
 * @author: tianzfm
 * @create: 2025-06-14 11:22
 **/
@Slf4j
public class LuaIoUtils {
    private static final String LUA_DIR = "lua/";


    private LuaIoUtils() {

    }


    public static String readLua(String fileName) {
        String filaPath = LUA_DIR + fileName;
        ClassPathResource file = new ClassPathResource(filaPath);
        try {
            return StreamUtils.copyToString(file.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // 读取Lua脚本文件失败，记录错误日志
            log.error("Failed to read Lua script file: {}", filaPath, e);
            return "";
        }
    }
}