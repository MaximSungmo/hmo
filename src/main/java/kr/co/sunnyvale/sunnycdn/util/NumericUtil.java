package kr.co.sunnyvale.sunnycdn.util;

import java.util.Random;

public class NumericUtil {
	public static int randomLimit(int end) {
		return new Random().nextInt( 100 ) % (end + 1);
	}
}