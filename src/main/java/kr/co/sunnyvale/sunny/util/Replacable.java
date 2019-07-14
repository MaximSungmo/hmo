package kr.co.sunnyvale.sunny.util;

public interface Replacable {
    String regexp();
    
    String replace(String findToken);
}
