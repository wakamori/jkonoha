package konoha;

import java.util.*;
import jkonoha.*;
import jkonoha.ast.*;

public class ClassGlue implements KonohaPackageInitializer {
	
	private final Syntax newSyntax = new Syntax("new") {
		@Override
		public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
			assert(s == c);
			Token tkNEW = tls.get(s);
			if(s + 2 < tls.size()) {
				Token tk1 = tls.get(s+1);
				Token tk2 = tls.get(s+2);
				if(tk1.kw.equals(KW.Type) && tk2.tt == TK.AST_PARENTHESIS) {  // new C (...)
					Syntax syn = stmt.parentNULL.ks.syntax(ctx, KW.ExprMethodCall);
					Expr nexpr = new Expr(ctx, syn, tk1, tk1.ty, 0);
					Expr expr = new Expr(syn);
					expr.setCons(tkNEW, nexpr);
					return expr;
				}
				if(tk1.kw.equals(KW.Type) && tk2.tt == TK.AST_BRANCET) {     // new C [...]
					Syntax syn = stmt.parentNULL.ks.syntax(ctx, KW._new);
					KClass ct = KClass.arrayClass;//CT_p0(ctx, CT_Array, tk1.ty);//TODO
					tkNEW.setmn("newArray", jkonoha.MNTYPE.method);
					Expr nexpr = new Expr(ctx, syn, tk1, ct, 0); //TODO cid : unsigned int
					Expr expr = new Expr(syn);
					expr.setCons(tkNEW, nexpr);
					return expr;
				}
			}
			return null;
		}
	};
	
	private final Syntax classSyntax = new Syntax("class") {
		{
			this.rule = "\"class\" $USYMBOL [\"extends\" extends: $USYMBOL] $block";
		}
	
		@Override
		public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
			Token tkC = stmt.token(ctx, KW.Usymbol, null);
			Token tkE= stmt.token(ctx, "extends", null);
			KClass supct = KClass.objectClass;
			List<KClass> ifct = new ArrayList<KClass>();
			if (tkE != null) { //TODO if(tkE)
				assert(KW.TK_KW[tkE.tt].equals(KW.Usymbol));
				supct = tkE.ty;
				if(supct.isFinal()) {
					ctx.SUGAR_P(System.err, 0, -1,  "%s is final", supct.getName()); //TODO (...,0, -1, ...) is correct?
					return false;
				}
				if(supct.isFinal()) {
					ctx.SUGAR_P(System.err, 0, -1, "%s has undefined field(s)", supct.getName()); //TODO (...,0, -1, ...) is correct?
					return false;
				}
			}
			//KClass ct = defineClassName(ctx, gamma, gamma.ks, cflag, tkC.text, supcid, stmt.uline);
			KonohaClass ct = new KonohaClass(tkC.text, supct, ifct.toArray(new KClass[0]));
			gamma.cc.addClass(ct);
			gamma.ks.addClass(tkC.text, ct);
			tkC.kw = KW.Type;
			tkC.ty = ct;
			stmt.parseClassBlock(ctx, tkC);
			Block bk = stmt.block(ctx, KW.Block, null);
	//		CT_setField(ctx, ct, supct, checkFieldSize(ctx, bk));
	//		if(!CT_addClassFields(ctx, ct, gamma, bk, stmt.uline)) {
	//			return false;
	//		}
			stmt.syntax = null;
			for(Stmt s : bk.blocks) {
				stmt.parentNULL.blocks.add(s);
			}
	//		checkMethodDecl(ctx, tkC, bk);
			
			return true;
		}
		
//		private int checkFieldSize(CTX ctx, Block bk) {
//			int i, c = 0;
//			for(i = 0; i < bk.blocks.size(); i++) {
//				Stmt stmt = bk.blocks.get(i);
//				DBG_P("stmt.kw=%s", KW_t(stmt.syntax.kw));
//				if(stmt.syntax.kw.equals(KW.StmtTypeDecl)) {
//					Expr expr = stmt.expr(ctx, KW.Expr, null);
//					if(expr.syn.kw.equals(KW.COMMA)) {
//						c += (expr.cons.size() - 1);
//					}
//					else if(expr.syn.kw == KW.LET || Expr_isTerm(expr)) {
//						c++;
//					}
//				}
//			}
//			return c;
//		}
	
	};


	private final Syntax extendsSyntax = new Syntax("extends") {//Joseph
		{
			this.rule = "\"extends\" $USYMBOL";
		}
	};

	private final Syntax dotSyntax = new Syntax(".") {//Joseph
		{
			this.priority = 16;
		}

		private boolean isFileName(List<Token> tls, int c, int e){
			if(c+1 < e) {
				Token tk = tls.get(c+1);
				return (tk.tt == TK.SYMBOL || tk.tt == TK.USYMBOL || tk.tt == TK.MSYMBOL);
			}
			return false;
		}
		
		@Override public Expr parseExpr(CTX ctx, Stmt stmt, List<Token> tls, int s, int c, int e) {
			//DBG_P("s=%d, c=%d", s, c);
			assert(s < c);
			if(isFileName(tls, c, e)) {
				Expr expr = stmt.newExpr2(ctx, tls, s, c);
				Expr expr2 = new Expr(this);
				expr2.setCons(tls.get(c+1), expr);
				return expr2;
			}
			if(c + 1 < e) c++;
			return null;
		}

		@Override
		public Expr exprTyCheck(CTX ctx, Expr expr, Gamma gamma, KClass ty) {//Joseph
			//in /package/konoha/class_glue.h:271
			Object o = expr.cons.get(0);
			Token tkN;
			if (o instanceof Token) {
				tkN = (Token)o;
				//int fn = tosymbolUM(ctx, tkN);//TODO
				Expr self = expr.tyCheckAt(ctx, 1, gamma, ty.varClass, 0);
				if (self != null) {
	//				KonohaClass klass = ctx.scriptClass;
	//				KonohaMethod mtd = (KonohaMethod)klass.getMethod(MN_toGETTER(fn), self.ty);//TODO
	//				if (mtd == null) {
	//					mtd = (KonohaMethod)klass.getMethod(MN_toISBOOL(fn), self.ty);//TODO
	//				}
	//				if (mtd != null) {
	//					expr.cons.set(0, mtd);
	//					return expr.tyCheckCallParams(ctx, stmt, mtd, gamma, reqty);//TODO
	//				}
				}
				System.out.println("undefined field: " + tkN.text);
			}
			return null;
		}
//		private void tosymbolUM (CTX ctx, Token tk) {//TODO
//			assert(tk.tt == TK.SYMBOL || tk.tt == TK.USYMBOL || tk.tt == TK.MSYMBOL);
//			return ctx.Ksymbol2(tk.text);//TODO in src/konoha/klibexec.h: 339
//		}
	};
	
	
	@Override
	public void init(CTX ctx, KonohaSpace ks) {
		Syntax[] syndef= {
				newSyntax,
				classSyntax,
				extendsSyntax,
				dotSyntax,
		};
		ks.defineSyntax(ctx, syndef);
	}
	
}
