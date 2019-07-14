package kr.co.sunnyvale.sunny.util;

public interface ThrowableCauseExtractor {
    Throwable extractCause(Throwable throwable);
}
