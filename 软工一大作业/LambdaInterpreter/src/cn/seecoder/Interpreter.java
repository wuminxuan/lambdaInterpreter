package cn.seecoder;
public class Interpreter {
    public boolean isTerminal(AST node) {
        if (node.node==Node.Abstraction){                 //当节点为Abstraction时，为终结符
            return true;
        }
        else{
            return false;
        }
    }

    public  AST eval(AST ast){                   //Evaluate method
        while(true){
            if(ast.node==Node.Application){
                if(ast.lhs.position==-1&&ast.rhs.position==-1){
                    break;
                }
                if(isTerminal(ast.lhs)&&isTerminal(ast.rhs)||isTerminal(ast.lhs)&&ast.rhs.position==-1){
                    return substitute(ast.rhs,ast.lhs.body);
                }
                else if(isTerminal(ast.lhs)){
                    ast.rhs=eval(ast.rhs);
                }
                else{
                    ast.lhs=eval(ast.lhs);
                }
            }
            else if(isTerminal(ast)){
                return ast;
            }
            else return ast;
        }
        return ast;
    }
    public  AST shift(int by,AST node,int from){
        if(node.node==Node.Identifier){
            if(node.position==-1){
                return node;
            }
            else if(node.position>=from){
                return new AST(node.position+by);
            }
            else{
                return new AST(node.position);
            }
        }
        else if(node.node==Node.Application){
            return new AST(shift(by,node.lhs,from),shift(by,node.rhs,from));
        }
        else if(isTerminal(node)){
            return new AST(node.param,shift(by,node.body,from+1));
        }
        return null;
    }
    public AST subst(AST value,AST node,int deepth){
        if(node.node==Node.Identifier){
            if(deepth==node.position){
                return shift(deepth,value,deepth);
            }
            else {
                return node;
            }
        }
        else if(node.node==Node.Application){
            return new AST(subst(value,node.lhs,deepth),subst(value,node.rhs,deepth));
        }
        else if(isTerminal(node)){
            return new AST(node.param,subst(value,node.body,deepth+1));
        }

        return null;
    }
    public AST substitute(AST position,AST node){
        return shift(-1,subst(shift(1,position,0),node,0),0);
    }
}
