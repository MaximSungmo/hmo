package kr.co.sunnyvale.sunny.test;

import java.lang.reflect.Method;


public class ReflectTest {
	public static void main(String args[]) throws Exception{
		ReflectBen01 e = new ReflectBen01();
		Method setDeleteFlagMethod = e.getClass().getMethod("setFlag", new Class[]{Boolean.TYPE});
		setDeleteFlagMethod.invoke(e, new Object[]{new Boolean(false)});
		
		System.out.println(e.isFlag());
	}
}

class ReflectBen01{
	private boolean flag;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}