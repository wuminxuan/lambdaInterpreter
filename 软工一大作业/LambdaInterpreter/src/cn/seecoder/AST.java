package cn.seecoder;

import java.util.ArrayList;

public class AST implements Abstraction,Application,Identifier{
    public String identifier;
    public String param;
    public AST body,lhs,rhs;
    public int position;
    public Node node;

    //Abstraction的构造方法
    public AST(String identifier,AST body){
        this.param=identifier;
        this.body=body;
        this.node=Node.Abstraction;
    }
    //Identifier的构造方法
    public AST(int position){
        this.position=position;
        this.node=Node.Identifier;
    }
    //Identifier的构造方法，自由变量
    public AST(int position,String identifier){
        this.position=position;
        this.identifier=identifier;
        this.node=Node.Identifier;
    }
    //Application的构造方法
    public AST(AST lhs,AST rhs){
        this.lhs=lhs;
        this.rhs=rhs;
        this.node=Node.Application;
    }
    public String ApplicationToString(ArrayList<String> ctx){                  //Application的toString方法
        if((this.lhs.position==-1)&&(this.rhs.position==-1)){                     //如果左右都为自由变量，则直接返回左右的标识
            String ret=this.lhs.IdentifierToString(ctx)+" "+this.rhs.IdentifierToString(ctx);
            return ret;
        }
        String left,right;             //根据左右的Node类型，递归调用各toString
        if(this.lhs.node==Node.Application){
            left=this.lhs.ApplicationToString(ctx);
        }
        else if(this.lhs.node==Node.Identifier){
            left=this.lhs.IdentifierToString(ctx);
        }
        else if(this.lhs.node==Node.Abstraction){
            left=this.lhs.AbstractionToString(ctx);
        }
        else{
            throw new RuntimeException("Error!");
        }
        if(this.rhs.node==Node.Abstraction){
            right=this.rhs.AbstractionToString(ctx);
        }
        else if(this.rhs.node==Node.Application){
            right=this.rhs.ApplicationToString(ctx);
        }
        else if(this.rhs.node==Node.Identifier){
            right=this.rhs.IdentifierToString(ctx);
        }
        else{
            throw new RuntimeException("Error!");
        }
        return left+" "+right;

    }
    public String IdentifierToString(ArrayList<String> ctx){     //如果Identifier为自由变量,则返回标识。若不是，则返回指代的atom
        if(this.position==-1){
            return identifier;
        }
        else{
            return ctx.get(this.position);
        }

    }
    public String AbstractionToString(ArrayList<String> ctx){
        if(this.node==Node.Abstraction&&this.param==null){
            return "";
        }
        StringBuilder temp=new StringBuilder();
        temp.append("(\\"+this.param+".");
        ArrayList<String> ctx_1=new ArrayList<String>();
        if(this.body.node==Node.Application){
            ctx_1.add(this.param);
            ctx_1.addAll(ctx);
            temp.append(this.body.ApplicationToString(ctx_1));
        }
        else if(this.body.node==Node.Abstraction){
            ctx_1.add(this.param);
            ctx_1.addAll(ctx);
            temp.append(this.body.AbstractionToString(ctx_1));
        }
        else{
            ctx_1.add(this.param);
            ctx_1.addAll(ctx);
            temp.append((this.body.IdentifierToString(ctx_1)));
        }
        return temp.append(")").toString();
    }
    public String ToString(ArrayList<String> ctx){
        if(this.node==Node.Application){
            return this.ApplicationToString(ctx);
        }
        else if(this.node==Node.Abstraction){
            return this.AbstractionToString(ctx);
        }
        else return this.IdentifierToString(ctx);

    }

}
