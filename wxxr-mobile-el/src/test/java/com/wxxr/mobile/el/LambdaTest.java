package com.wxxr.mobile.el;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.wxxr.javax.el.ELProcessor;

public class LambdaTest {
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    
    @Before
    public void setUp() {    
    } 
    
    void testExpr(ELProcessor elp, String testname, String expr, Long expected) {
        System.out.println("=== Test Lambda Expression:" + testname + " ===");
        System.out.println(" ** " + expr);
        Object result = elp.eval(expr);
        System.out.println("    returns " + result);
        assertEquals(expected, result);
    }

    @Test
    public void testImmediate() {
        ELProcessor elp = new ELProcessor();
        testExpr(elp, "immediate", "(x->x+1)(10)", 11L);
        testExpr(elp, "immediate0", "(()->1001)()", 1001L);
        testExpr(elp, "immediate1", "((x,y)->x+y)(null, null)", 0L);
        testExpr(elp, "immediate 2", "(((x,y)->x+y)(3,4))", 7L);
        testExpr(elp, "immediate 3", "(x->(y=x)+1)(10) + y", 21L);
    }

    @Test
    public void testAssignInvoke() {
        ELProcessor elp = new ELProcessor();
        testExpr(elp, "assign", "func = x->x+1; func(10)", 11L);
        testExpr(elp, "assign 2", "func = (x,y)->x+y; func(3,4)", 7L);
    }

    @Test
    public void testConditional() {
        ELProcessor elp = new ELProcessor();
        elp.eval("cond = true");
        testExpr(elp, "conditional", "(x->cond? x+1: x+2)(10)", 11L);
        elp.eval("cond = false");
        testExpr(elp, "conditional 2",
                 "func = cond? (x->x+1): (x->x+2); func(10)", 12L);
    }

    @Test
    public void testFact() {
        ELProcessor elp = new ELProcessor();        
        testExpr(elp, "factorial", "fact = n->n==0? 1: n*fact(n-1); fact(5)", 120L);
        testExpr(elp, "fibonacci", "f = n->n==0? 0: n==1? 1: f(n-1)+f(n-2); f(10)", 55L);
    }

    @Test
    public void testVar() {
        ELProcessor elp = new ELProcessor();
        elp.setVariable("v", "x->x+1");
        testExpr(elp, "assignment to variable", "v(10)", 11L);
    }

    @Test
    public void testLambda() {
        ELProcessor elp = new ELProcessor();
        testExpr(elp, "Lambda Lambda", "f = ()->y->y+1; f()(100)", 101L);
        testExpr(elp, "Lambda Lambda 2", "f = (x)->(tem=x; y->tem+y); f(1)(100)", 101L);
        testExpr(elp, "Lambda Lambda 3", "(()->y->y+1)()(100)", 101L);
        testExpr(elp, "Lambda Lambda 4", "(x->(y->x+y)(1))(100)", 101L);
        testExpr(elp, "Lambda Lambda 5", "(x->(y->x+y))(1)(100)", 101L);
        testExpr(elp, "Lambda Lambda 6"
                , "(x->y->x(0)+y)(x->x+1)(100)", 101L);
    }
}
