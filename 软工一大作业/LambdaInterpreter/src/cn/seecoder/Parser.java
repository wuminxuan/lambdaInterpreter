package cn.seecoder;

import java.util.ArrayList;

public class Parser {
    public Lexer lexer;
    //构造器
    public Parser(Lexer l){
        this.lexer=l;
    }
    //Parser的传入方法
    public AST parse(){
        AST ret=this.term(new ArrayList<String> ());
        this.lexer.match(Type.EOF);
        return ret;
    }
    //
    public AST term(ArrayList<String> ctx){
        if(lexer.skip(Type.LAMBDA)){      //term ::= LAMBDA LCID DOT term          **Abstraction
                String valueOfToken=lexer.token(Type.LCID);   //get the value of LCID
                lexer.match(Type.DOT);           //judge if the next Token is DOT,else throw RuntimeException
                ArrayList<String> temp=new ArrayList<String>();
                temp.add(valueOfToken);
                temp.addAll(ctx);
                AST term=this.term(temp);
                return new AST(valueOfToken,term);   //LAMBDA LCID DOT term -> Abstraction
            }
            else {
            return this.application(ctx);
        }         //term ::= application
        }

    public AST application(ArrayList<String> ctx){
        //application ::= atom application'
        AST lhs=this.atom(ctx);
        while (true) {
            AST rhs=this.atom(ctx);
            if(rhs==null){
                return lhs;                        //递归直到无atom
            }
            else{
                lhs=new AST(lhs,rhs);           //lhs,rhs->Application
            }
        }
    }
    public AST atom(ArrayList<String> ctx){
        if(lexer.skip(Type.LAPREN)){                 //atom ::= LPAREN term RPAREN
            AST temp=this.term(ctx);
            this.lexer.match(Type.RAPREN);
            return temp;
        }
        else if(lexer.next(Type.LCID)){
                String valueOfAtom=lexer.token.value;
                lexer.nextTokern();
                if(ctx.contains(valueOfAtom)){
                    return new AST(ctx.indexOf(valueOfAtom));          //return a Identifier
                }
                else{
                    return new AST(-1,valueOfAtom);       //自由变量
                }
            }
        else {
            return null;
        }
        }


    public static void main(String[] args){
        String source = "\\x.x";
        Lexer lexer=new Lexer(source);
        Parser parser=new Parser(lexer);
        AST ast=parser.parse();

    }
}

