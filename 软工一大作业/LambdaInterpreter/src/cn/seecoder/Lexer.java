package cn.seecoder;
import java.util.regex.Pattern;

public class Lexer{

    public String source;   //lexer对象的源
    public int index;
    public Token token;

    public Lexer(String s){
        source = s;
        index =0;
        this.token=null;
        this.nextTokern();

    }
    public char nextChar(){
        if(this.index>=this.source.length()){
            index++;
            return '\0';
        }
        return this.source.charAt(index++);

    }
    //lexer类里的主方法，用来将lexer对象指向下一个token,只改变lexer属性，无返回值
    public  void  nextTokern(){
        char c;
        String p1="\\s";
        String p2="[a-z]";
        String p3="[a-zA-Z]";
        do{
            c=nextChar();
        }while(Pattern.matches(p1,c+""));//将c提取出来，并检验c是否是空格符，如果是则继续提取，不是则跳出给下一步
        switch (c) {
            case '\\':
                this.token=new Token(Type.LAMBDA, null);
                break;
            case '.':
                this.token = new Token(Type.DOT, null);
                break;
            case '(':
                this.token = new Token(Type.LAPREN, null);
                break;
            case ')':
                this.token = new Token(Type.RAPREN, null);
                break;
            case '\0':
                this.token = new Token(Type.EOF, null);
                break;
            default:
                String tem = c + "";
                if (Pattern.matches(p2,tem)) {
                    String tem1 = "";
                    do {                                  //
                        tem1 = tem1 + c;
                        c = nextChar();
                    } while (Pattern.matches(p3, c + ""));
                    index -= 1;
                    this.token = new Token(Type.LCID, tem1);
                } else {
                    System.out.println("WRONG!");
                }
        }
    }
    //对lexer的辅助方法，判断当前lexer对象的属性的type是否与传入值相等
    public boolean next(Type type){
        boolean result=token.type==type;
        return result;
    }
    //对lexer的辅助方法，判断当前lexer对象的属性的type是否与传入值相等，若相等则同时将lexer指向下一个token
    public boolean skip(Type type){
        if(this.next(type)){
            this.nextTokern();
            return true;
        }

        return false;
    }
    //对lexer的辅助方法，断言skip
    public void match(Type type){
        if(next(type)){
            this.nextTokern();
        }
        else{
            throw new RuntimeException("Error!");
        }
    }

    //
    public String token(Type type){
        String result=this.token.value;
        this.match(type);
        return result;
    }

    public static void main(String[] args){
        String source = "(\\x.\\y.x)(\\x.x)(\\y.y)";
        Lexer lexer=new Lexer(source);
        if(lexer.skip(Type.LAPREN)){
            System.out.print("RIGHT!(1)");
        }
        else {
            System.out.print(("WRONG!"));
        }
        if(lexer.next(Type.LAMBDA)){
            System.out.print("(2)");
        }
        else{
            System.out.print("Wrong!(2)");
        }
    }


}

