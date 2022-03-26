package Matheval;

import Matheval.Token;

import java.util.List;
import java.util.LinkedList;
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

	public Node parse(Stack<Token> rpn) {
		Token nodeI = tokens.pop();

		if(nodeI.getType().equals("Constant") || currentToken.getType().equals("Variable")) {
			return (Node) nodeI;
		} else {
			if(nodeI.getType().equals("Method")) {
				MethodNode method = new MethodNode((Method) nodeI);
				fillMethod(method, tokens);
				return method;
			} else {
				OperatorNode operator = new OperatorNode((Operator) nodeI);
				fillOperator(operator, tokens);
				return operator;
			}
		}
	}

	public Stack<Token> rpn() {
		Stack<Token> rpn = new Stack<Token>();
		Stack<Token> operator = new Stack<Token>();

		advanced();
		while(currentToken != null) {
			if(currentToken.getType().equals("Constant") || currentToken.getType().equals("Variable")) {
				rpn.push(currentToken);
			} else if(currentToken.getType().equals("Method")) {
				operator.push(currentToken);
			} else if(currentToken.getType().equals("Operator")) {
				if(currentToken.toString().equals("(")) {
					operator.push(currentToken);
				} else if(currentToken.toString().equals(")")) {
					while(!((Token) operator.peek()).toString().equals("(")) {
						rpn.push(operator.pop());
					}

					operator.pop();

					if(!operator.isEmpty()) {
						if(((Token) operator.peek()).getType().equals("Method")) {
							rpn.push(operator.pop());
						}
					}
				} else if(currentToken.toString().equals("=")) {
					while(!operator.isEmpty()) {
						if(((Token) operator.peek()).toString().equals("(")) {
							operator.pop();
						} else {
							rpn.push(operator.pop());
						}
					}
				} else {
					if(operator.isEmpty()) {
						operator.push(currentToken);
					} else {
						while(!operator.isEmpty()) {
							if(!((Token) operator.peek()).toString().equals("(") && ((Operator) operator.peek()).getPrecedence() >= ((Operator) currentToken).getPrecedence()) {
								rpn.push(operator.pop());
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
			rpn.push(operator.pop());
		}

		return rpn;
	}

	private void fillMethod(MethodNode method, Stack<Token> tokens) {
		Token nodeI = tokens.pop();

		if(nodeI.getType().equals("Constant") || nodeI.getType().equals("Variable")) {
			method.setNode((Node) nodeI);
		} else {
			if(nodeI.getType().equals("Method")) {
				MethodNode imethod = new MethodNode((Method) nodeI);
				fillMethod(imethod, tokens);
				method.setNode(imethod);
			} else {
				OperatorNode operator = new OperatorNode((Operator) nodeI);
				fillOperator(operator, tokens);
				method.setNode(operator);
			}
		}
	}

	private void fillOperator(OperatorNode operator, Stack<Token> tokens) {
		Token right = tokens.pop();

		if(right.getType().equals("Constant") || right.getType().equals("Variable")) {
			operator.setRight((Node) right);
		} else {
			if(right.getType().equals("Method")) {
				MethodNode method = new MethodNode((Method) right);
				fillMethod(method, tokens);
				operator.setRight(method);
			} else {
				OperatorNode ioperator = new OperatorNode((Operator) right);
				fillOperator(ioperator, tokens);
				operator.setRight(ioperator);
			}
		}

		Token left = tokens.pop();

		if(left.getType().equals("Constant") || left.getType().equals("Variable")) {
			operator.setLeft((Node) left);
		} else {
			if(left.getType().equals("Method")) {
				MethodNode method = new MethodNode((Method) left);
				fillMethod(method, tokens);
				operator.setLeft(method);
			} else {
				OperatorNode ioperator = new OperatorNode((Operator) left);
				fillOperator(ioperator, tokens);
				operator.setLeft(ioperator);
			}
		}
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