import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class InvalidFormatException extends Exception {
    public String error() {
        return "Syntax Error";
    }
}
class Stack{
    char arr[];
    int size;
    int top;

    //Constructor
    Stack(){
        size=100;
        arr = new char[size];
        top = -1;
    }

    public boolean isEmpty(){
        return top == -1;
    }
    public boolean isFull(){
        return top == size-1;
    }
    public void push(char ch){
        if(isFull()) return;
        arr[++top]=ch;
    }
    public char pop(){
        if(isEmpty()) return 'X';
        return arr[top--];
    }
    public char peek(){
        if(isEmpty()) return 'X';
        else return arr[top];
    }
}
class BasicCalculator implements ActionListener {
    JButton btn[] = new JButton[16];
    JTextField fld;

    BasicCalculator() {
        int i;
        JFrame frame = new JFrame("Basic Calculator");
        frame.setLayout(new BorderLayout(5, 5));
        frame.setSize(300, 370);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 3, 3));
        buttonPanel.setPreferredSize(new Dimension(280, 240));
        for (i = 0; i < btn.length; i++) {
            btn[i] = new JButton();
            btn[i].setSize(65, 65);
        }
        btn[10].setText("+");
        btn[11].setText("-");
        btn[12].setText("*");
        btn[13].setText("/");
        btn[14].setText("%");
        btn[15].setText("=");

        for (i = 0; i < btn.length; i++) {
            if (i <= 9)
                btn[i].setText(String.valueOf(i));
            btn[i].addActionListener(this);
        }
        for (i = 0; i < btn.length; i++)
            buttonPanel.add(btn[i]);

        JButton clrbtn = new JButton("CLR");
        clrbtn.setPreferredSize(new Dimension(280, 35));
        clrbtn.addActionListener(this);

        fld = new JTextField(20);
        fld.setPreferredSize(new Dimension(280, 70));
        fld.setFont(new Font("Arial", Font.PLAIN, 24));

        frame.add(fld, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(clrbtn, BorderLayout.SOUTH);
        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("=")) {
            try {
                double val = calculate(fld.getText());
                fld.setText(String.valueOf(val));
            } catch (InvalidFormatException e) {
                fld.setText(e.error());
            }
        } else if (ae.getActionCommand().equals("CLR")) {
            fld.setText("");
        } else {
            fld.setText(fld.getText() + ae.getActionCommand());
        }
    }
     public static int precedence(char ch){
        if(ch=='/' || ch=='*' || ch=='%') return 2;
        else return 1;
     }
     public static String postfix(String str) throws InvalidFormatException {
        Stack st = new Stack();
        String expr="";
        for(int i=0;i<str.length();i++){
            char ch = str.charAt(i);
            if (ch>=48 && ch<=57){
                expr+=ch;
            }else{
                if(i==0 || !Character.isDigit(str.charAt(i-1))) throw new InvalidFormatException();
                while(!st.isEmpty() && (precedence(st.peek()) > precedence(ch)))    expr+=st.pop();
                st.push(ch);
            }
        }
        while(!st.isEmpty()){
            expr+=st.pop();
        }
        return expr;
     }
    public static double calculate(String str) throws InvalidFormatException {
         String postfixExp = postfix(str);
         
         //Calculate the value of postfix expression
         Stack st = new Stack();
            for(int i=0;i<postfixExp.length();i++){
                char ch = postfixExp.charAt(i);
                if(ch>=48 && ch<=57){
                    st.push(ch);
                }else{
                    double b = st.pop()-'0';
                    double a = st.pop()-'0';
                    switch(ch){
                        case '+':
                            st.push((char)(a+b+'0'));
                            break;
                        case '-':
                            st.push((char)(a-b+'0'));
                            break;
                        case '*':
                            st.push((char)(a*b+'0'));
                            break;
                        case '/':
                            st.push((char)(a/b+'0'));
                            break;
                        case '%':
                            st.push((char)(a%b+'0'));
                            break;
                    }
                }
            }
            return st.pop()-'0';
    }
}

public class BasicCalculatorDemo {
    public static void main(String[] args) {
        new BasicCalculator();
    }
}