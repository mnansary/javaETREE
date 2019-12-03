import java.util.*;

public class Expression extends ExpressionTree {
   
    public String eqn;

    public String fullyParenthesised() {
        // add implementation here
        ArrayList<String> ans = new ArrayList<String>();
        parentasize(this.root, ans);
        String answer = new String();
        for (String b:ans) answer += b + " ";
        this.eqn=answer;
        return answer.replace(" ", "");
   }
   private void parentasize(Node<String> r, ArrayList<String> ans) {
       String root = r.getData();
       Iterator<? extends Node<String>> i = r.children();
       if(!i.hasNext()){
           ans.add(root);
           return;
       }
           ans.add("(");
           parentasize(i.next(), ans);
           ans.add(root);
           parentasize(i.next(), ans);
           ans.add(")");
   }
   public Expression(String s) {
        super();
        // add implementation here
        List<String> exp_infix= new ArrayList<>(); 
        List<String> exp_postFix= new ArrayList<>();  
        exp_infix=createInFix(s);
        exp_postFix=createPostFix(exp_infix);
        root=constructTree(exp_postFix);
         
   }
    public double evaluate() {
        List<String> exp_infix= new ArrayList<>(); 
        List<String> exp_postFix= new ArrayList<>();  
        exp_infix=createInFix(this.eqn);
        exp_postFix=createPostFix(exp_infix);
        double answer= evaluatePostfix(exp_postFix);
        return answer;
    }
   // string to infix
    private List<String> createInFix(String expression){
    
        List<String> inFix= new LinkedList<>(); //queue
        List<String> result= new ArrayList<String>(); 
    
        String[] temporary = expression.split("\\s+"); //temp = a
    
        for (int i = 0; i < temporary.length; i++ )
            inFix.add(temporary[i]);
        
        while(!inFix.isEmpty()){
            String variable = inFix.remove(0);
            result.add(variable);
        }

        return result;
    }
   // infix to postfix
   /*
    * 
    Algorithm
    1. Scan the infix expression from left to right.
    2. If the scanned character is an operand, output it.
    3. Else,
    …..3.1 If the precedence of the scanned operator is greater than the precedence of the operator in the stack(or the stack is empty), push it.
    …..3.2 Else, Pop the operator from the stack until the precedence of the scanned operator is less-equal to the precedence of the operator residing on the top of the stack. Push the scanned operator to the stack.
    4. If the scanned character is an ‘(‘, push it to the stack.
    5. If the scanned character is an ‘)’, pop and output from the stack until an ‘(‘ is encountered.
    6. Repeat steps 2-6 until infix expression is scanned.
    7. Pop and output from the stack until it is not empty.

    */
    private List<String> createPostFix(List<String> inFix){
        
        List<String> stack= new LinkedList<>(); //queue
        List<String> postfix= new ArrayList<>(); 
        
        
        for (int i = 0; i < inFix.size(); i++) {
            String variable = inFix.get(i);
            // If variable is an operand, append it to the output
            if (isOprand(variable) ){
                postfix.add(variable);
            }
            else if (variable.equals("(")){
                stack.add(variable);
                //System.out.println(stack);
            }
            // If the scanned character is an ‘)’, pop and output from the stack
            // until an ‘(‘ is encountered.
            else if (variable.equals(")")) {
                for (int k = stack.size()-1;k>=0;k--) {
                    //System.out.println("IN ):"+ stack);
                    String var=stack.get(k);
                    if (var.equals("(")){
                        stack.remove(k);
                        break;
                    }
                    else {
                        postfix.add(var);
                        stack.remove(k);
                    }
                } 
                //System.out.println("After ):"+ stack);
            }
            else {
                for(int j=stack.size()-1;j>=0;j--){ 
                    String op=stack.get(j);
                    if(getPrecedence(variable) <= getPrecedence(op)){
                        postfix.add(stack.remove(j));
                    }
                    else{
                        break;
                    }    
                } 
                stack.add(variable);
                //System.out.println("After OPS:"+stack);
            }   
        }
        //After the input is over, keep popping and appending to the output until the stack is empty.
        while (!stack.isEmpty()) {
            postfix.add(stack.remove(stack.size()-1));
        }
        
       return postfix;
   }
   //value checks
    private boolean isOprand(String x) {

        if("+".equals(x)||"-".equals(x)||"*".equals(x)||"/".equals(x) ||"(".equals(x)||")".equals(x)){
            return false;
        }
        else {
            return true;
        }
    }
    // check precendence
    private static int getPrecedence(String op) {
        switch (op) {
        case "+":
        case "-":
            return 1;

        case "*":
        case "/":
            return 2;
        
        }
        return -1;
        
    }
    //
    private BNode<String> constructTree(List<String> postfix) { 
        Stack<BNode<String>> st = new Stack<>(); 
        BNode<String> t, t1, t2; 
  
        // Traverse through every character of 
        // input expression 
        for (int i = 0; i < postfix.size(); i++) { 
            String variable=postfix.get(i);
            // If operand, simply push into stack 
            if (isOprand(variable)) { 
                t = new BNode<String>(variable,null,null,null); 
                st.push(t); 
            } 
            else // operator 
            { 
                t = new BNode<String>(variable,null,null,null); 
  
                // Pop two top nodes 
                // Store top 
                t1 = st.pop();      // Remove top 
                t2 = st.pop(); 
  
                //  make them children 
                t.right = t1; 
                t.left = t2; 
  
                st.push(t); 
            } 
        } 
  
        //  only element will be root of expression 
        // tree 
        t = st.peek(); 
        st.pop(); 
  
        return t; 
    } 
    //
    private double evaluatePostfix(List<String> postfix) 
    { 
        //create a stack 
        Stack<Float> stack=new Stack<>(); 
          
        // Scan all characters one by one 
        for(int i=0;i<postfix.size();i++) 
        { 
            String variable = postfix.get(i); 
              
            if(isOprand(variable)) 
                stack.push(Float.parseFloat(variable)); 
              
            //  If the scanned character is an operator, pop two 
            // elements from stack apply the operator 
            else
            { 
                float val1 = stack.pop(); 
                float val2 = stack.pop(); 
                  
                switch(variable) 
                { 
                    case "+": 
                        stack.push(val2+val1); 
                        break; 
                      
                    case "-": 
                        stack.push(val2- val1); 
                        break; 
                      
                    case "/": 
                        stack.push(val2/val1); 
                        break; 
                      
                    case "*": 
                        stack.push(val2*val1); 
                        break; 
                } 
            } 
        } 
        return stack.pop();     
    } 

    
}
   
   


