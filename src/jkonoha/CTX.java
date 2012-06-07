package jkonoha;

import java.io.PrintStream;

public class CTX extends KObject {
	private static final boolean debug = true;
	
	public Konoha konoha;
	public CtxSugar ctxsugar = new CtxSugar();
	public ModSugar modsugar = new ModSugar();

	public long kfileid(String name, long def)
	{
		return this.konoha.kfileid(name, def);
	}

	public String S_file(long uline) {
		return this.konoha.S_file(uline);
	}

	public void SUGAR_P(PrintStream out, long uline, int pos, String fmt, Object...args) {
		out.printf(fmt, args);
	}	
	
	public void DBG_P(String fmt, Object...args) {
		if(debug) {
			StackTraceElement e = Thread.currentThread().getStackTrace()[2];
			System.out.printf("DEBUG(%s.%s:%d) ", e.getClassName(), e.getMethodName() ,e.getLineNumber());
			System.out.printf(fmt, args);
			System.out.println();
		}
	}
	
//	public static final int MOD_logger = 0;
////	public static final int MOD_gc = 1;
//	public static final int MOD_code = 2;
//	public static final int MOD_sugar = 3;
//	public static final int MOD_float = 11;
////	public static final int MOD_jit = 12;
//	public static final int MOD_iconv = 13;
//	public static final int MOD_IO = 14;
//	public static final int MOD_llvm = 15;
//	public static final int MOD_REGEX = 16;
	
//	public final KClass CT_Token() {
//		return ((KModSugar)this.kmodsugar()).cToken;
//	}
//	
//	public final KClass CT_Expr() {
//		return ((KModSugar)this.kmodsugar()).cExpr;
//	}
//	
//	public final KClass CT_Stmt() {
//		return ((KModSugar)this.kmodsugar()).cStmt;
//	}
//	
//	public final KClass CT_Block() {
//		return ((KModSugar)this.kmodsugar()).cBlock;
//	}
//	
//	public final KClass CT_KonohaSpace() {
//		return ((KModSugar)this.kmodsugar()).cKonohaSpace;
//	}
//	
//	public final KClass CT_Gamma() {
//		return ((KModSugar)this.kmodsugar()).cGamma;
//	}
//	
//	public final KClass CT_TokenArray() {
//		return ((KModSugar)this.kmodsugar()).cTokenArray;
//	}

}
