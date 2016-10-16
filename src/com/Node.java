package com;

import java.util.List;
import  java.util.Map;

import static java.lang.Math.pow;


public class Node {


    private List<String> s;
    public Node left=null,right=null;
    private char sym='u';
    private boolean isDigital = false;
    static private Node root;
    static private Map<String,String> symbol_table;
    static private String[] op = {"+", "-", "*"};
    static private String dsym = "";

    public Node(List<String> s){
        this.s=s;
    }

    static public void set_table(Map<String,String> table){
        symbol_table = table;
    }
    static public void set_dsym(String sym){
    	dsym = sym;
    }
    
    static public String list_to_string(List<String> exp) {
    	StringBuffer re = new StringBuffer();
    	for (String i : exp) {
    		re.append(i);
    	}
    	return re.toString();
    }
    
    public void expression() {
    	for (String i : op) {
    		int p = s.lastIndexOf(i);
            if (p != -1) {
                sym = i.charAt(0);
                left = new Node(s.subList(0, p));
                right = new Node(s.subList(p + 1, s.size()));
                left.expression();
                right.expression();
                return;
            }
    	}
        int p = s.lastIndexOf("^");
        if (p!=-1) {
            sym='^';
            left=new Node(s.subList(0,1));
            right=new Node(s.subList(2,3));
            return;
        }
    }

    public String calculate(){
    	if (sym == 'u'){
    		
    			if (symbol_table.containsKey(s.get(0))) {
    				this.isDigital = true;
    				return symbol_table.get(s.get(0));
    			
            }else {
                if (s.get(0).matches("\\d+")){
                    this.isDigital=true;
                }
                return s.get(0);
            }
        }
    	String re_left = left.calculate();
        String re_right = right.calculate();
    	if (sym == '^'){
        	if (left.isDigital && right.isDigital){
            	this.isDigital = true;
                return String.valueOf(((int)pow(Integer.parseInt(re_left),
                		Integer.parseInt(re_right))));
            } else {
            	return re_left + "^" + re_right;
            }
        } else if (sym == '*') {
        	if (left.isDigital && right.isDigital){
            	this.isDigital = true;
                return String.valueOf(Integer.parseInt(re_left)
                		* Integer.parseInt(re_right));
            } else {
            	return new String(re_left + "*" + re_right);
            }
        } else if (sym == '+') {
        	if (left.isDigital && right.isDigital){
            	this.isDigital = true;
                return String.valueOf(Integer.parseInt(re_left)
                		+ Integer.parseInt(re_right));
            } else {
            	return re_left + "+" + re_right;
            }
        } else if (sym == '-') {
        	if (left.isDigital && right.isDigital){
            	this.isDigital = true;
                return String.valueOf(Integer.parseInt(re_left)
                		- Integer.parseInt(re_right));
            } else {
            	return re_left + "-" + re_right;
            }
        }
		return null;
    }
    public String derivative() {
    	String re = derivative2();
    	if (re.isEmpty()) {
    		return "0";
    	} else{
    		return re;
    	}
    }
    
    private String derivative2() {
    	if (sym == 'u'){
            if (s.get(0).equals(dsym)) {
    			return "1";
    		}
            return "";
        }
    	if (sym == '^'){
    		if (s.get(0).equals(dsym)) {
	    		int index = Integer.parseInt(right.s.get(0));
	    		if (index > 2) {
	    			return Integer.toString(index) + "*" + left.s.get(0)
	        			+ "^" + Integer.toString(index-1);
	    		} else if (index == 2) {
	    			return "2*" + left.s.get(0);
	    		} else {
	    			return "1";
	    		}
    		} else {
    			return "";
    		}
        }
    	String re_left = left.derivative2();
        String re_right = right.derivative2();
    	if (sym == '*') {
    		String s1, s2;
    		s1 = re_left + "*" + list_to_string(right.s);
    		s2 = list_to_string(left.s) + "*" + re_right;
    		if (re_left.isEmpty())   {
        		s1 = "";
        	} else if (re_left.equals("1")){
        		s1 = list_to_string(right.s);
        	}
    		if (re_right.isEmpty()) {
        		s2 = "";
        	} else if (re_right.equals("1")){
        		s2 = list_to_string(left.s);
        	}
    		if (!s1.isEmpty() && !s2.isEmpty()) {
    			return s1 + "+" + s2;
    		} else {
    			return s1 + s2;
    		}
        } else if (sym == '+') {
        	String s1, s2;
        	s1 = re_left;
        	s2 = re_right;
        	if (!s1.isEmpty() && !s2.isEmpty()) {
    			return s1 + "+" + s2;
    		} else {
    			return s1 + s2;
    		}
        } else if (sym == '-') {
        	String s1, s2;
        	s1 = re_left;
        	s2 = re_right;
        	if (!s1.isEmpty() && !s2.isEmpty()) {
    			return s1 + "-" + s2;
    		} else {
    			return s1 + s2;
    		}
        }
		return null;
    }
}
