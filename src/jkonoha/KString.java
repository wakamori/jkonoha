package jkonoha;


public class KString extends KObject {
	
	private final String text;
	
	public KString(String s) {
		this.text = s;
	}
	
	public static int toInt(String self) {
		return Integer.parseInt(self);
	}
	
	public static String opADD(String self, String s) {
		return self + s;
	}
	
	public static KString box(String s) {
		return new KString(s);
	}
	
	public String unbox() {
		return this.text;
	}
	
}