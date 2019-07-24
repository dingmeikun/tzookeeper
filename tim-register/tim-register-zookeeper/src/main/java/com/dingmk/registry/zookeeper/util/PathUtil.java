package com.dingmk.registry.zookeeper.util;

import com.dingmk.registry.common.Constants;
import com.dingmk.registry.common.CustomURL;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zt
 */
public final class PathUtil {

    public static final String DEFAULT_ROOT = "xy-register";

    public static String toUrlPath(String root, CustomURL url) {
        String server = null;
        try {
            server = URLEncoder.encode(url.getServer(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Constants.PATH_SEPARATOR
                + root
                + Constants.PATH_SEPARATOR
                + url.getServiceName()
                + Constants.PATH_SEPARATOR
                + server;
    }

    public static String decode(String path) {
        if (path == null || "".equals(path.trim())) {
            throw new IllegalArgumentException("path must not be null");
        }
        try {
            return URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<String> decode(List<String> children) {
        if (children == null || children.isEmpty()) {
            throw new IllegalArgumentException("children must not be null");
        }
        List<String> list = new ArrayList<String>();
        for(String child : children) {
            try {
                list.add(URLDecoder.decode(child, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return list;
    }

    public static String addPrefixPath(String path) {
        return Constants.PATH_SEPARATOR + path;
    }

    public static List<String> splitPath(String path) {
        if (path == null || "".equals(path.trim())) {
            throw new IllegalArgumentException("path must not be null");
        }

        if (path.startsWith(Constants.PATH_SEPARATOR)) {
            path = path.substring(1);
        }

        return Arrays.asList(path.split(Constants.PATH_SEPARATOR));
    }
}
