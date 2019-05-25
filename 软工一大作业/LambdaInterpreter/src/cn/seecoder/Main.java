package cn.seecoder;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // write your code here
        String source = "(\\x.\\y.x y)(\\x.x)(\\y.y)";
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        AST ast = parser.parse();
        Interpreter interpreter = new Interpreter();
        AST newast = interpreter.eval(ast);
        System.out.println(newast.ToString(new ArrayList<String>()));
    }
}
