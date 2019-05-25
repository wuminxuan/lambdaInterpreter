package cn.seecoder;

import java.util.ArrayList;

public class Index {
    public static void main(String[] args) {
        String source = "(\\x.\\y.x y)(\\y.y)(\\x.x) ";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        AST ast = parser._Set();
        Interpreter interpreter = new Interpreter();
        AST newast = interpreter.cal(ast);
        System.out.println(newast.finaltoString(new ArrayList<String>()));
    }
}
