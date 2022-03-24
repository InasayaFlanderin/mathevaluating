package Matheval;

import Matheval.Token;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Parser {
	private List<Token> tokens;
	private int position;
	private Token currentToken;

	public Parser(List<Token> tokens) {
		this.tokens = tokens;
		this.position = -1;
		this.currentToken = null;
	}

	public Queue<Token> rpn() {
		Queue<Token> rpn = new LinkedList<Token>();
		Stack<Token> operator = new Stack<Token>();

		advanced();
		while(currentToken != null) {
			if(currentToken.getType().equals("Constant") || currentToken.getType().equals("Variable")) {
				rpn.add(currentToken);
			} else if(currentToken.getType().equals("Method")) {
				operator.push(currentToken);
			} else if(currentToken.getType().equals("Operator")) {
				if(currentToken.toString().equals("(")) {
					operator.push(currentToken);
				} else if(currentToken.toString().equals(")")) {
					while(!((Token) operator.peek()).toString().equals("(")) {
						rpn.add(operator.pop());
					}

					operator.pop();

					if(!operator.isEmpty()) {
						if(((Token) operator.peek()).getType().equals("Method")) {
							rpn.add(operator.pop());
						}
					}
				} else if(currentToken.toString().equals("=")) {
					while(!operator.isEmpty()) {
						if(((Token) operator.peek()).toString().equals("(")) {
							operator.pop();
						} else {
							rpn.add(operator.pop());
						}
					}
				} else {
					if(operator.isEmpty()) {
						operator.push(currentToken);
					} else {
						while(!operator.isEmpty()) {
							if(!((Token) operator.peek()).toString().equals("(") && ((Operator) operator.peek()).getPrecedence() >= ((Operator) currentToken).getPrecedence()) {
								rpn.add(operator.pop());
							} else {
								break;
							}
						}

						operator.push(currentToken);
					}
				}
			} else {
				continue;
			}

			advanced();
		}

		while(!operator.isEmpty()) {
			rpn.add(operator.pop());
		}

		return rpn;
	}

	private void advanced() {
		position++;

		if(position < tokens.size()) {
			this.currentToken = (Token) tokens.get(position);
		} else {
			this.currentToken = null;
		}
	}
}