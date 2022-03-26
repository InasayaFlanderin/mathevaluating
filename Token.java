package Matheval;

interface Token {
	public String toString();
	public String toToken();
	public String getType();
}

interface Node {
	public String toString();
}

class Complex implements Token, Node {
	private double real;
	private double imaginary;

	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public String toString() {
		if(this.real == 0 && this.imaginary == 0) {
			return "0";
		} else if(this.real == 0) {
			return this.imaginary + "i";
		} else if(this.imaginary == 0) {
			return "" + this.real;
		} else {
			return this.real + " + " + this.imaginary + "i";
		}
	}

	public String toToken() {
		return "Complex(" + this.real + "," + this.imaginary + ")";
	}

	public String getType() {
		return "Constant";
	}
}

class Operator implements Token {
	private char name;
	private int precedence;

	public Operator(char name, int precedence) {
		this.name = name;
		this.precedence = precedence;
	}

	public String toString() {
		return "" + this.name;
	}

	public String toToken() {
		return "Operator(" + this.name + "," + this.precedence + ")";
	}

	public int getPrecedence() {
		return this.precedence;
	}

	public String getType() {
		return "Operator";
	}
}

class Method implements Token {
	private String name;

	public Method(String name) {
		this.name = name;
	}

	public String toString() {
		return "" + this.name;
	}

	public String toToken() {
		return "Method(" + this.name + ")";
	}

	public String getType() {
		return "Method";
	}
}

class Symbol implements Token, Node {
	private char name;
	private Node value;

	public Symbol(char name) {
		this.name = name;
	}

	public String toString() {
		return "" + this.name;
	}

	public String toToken() {
		return "Symbol(" + this.name + ")";
	}

	public String getType() {
		return "Variable";
	}

	public void setValue(Node value) {
		this.value = value;
	}

	public Complex getValue() {
		return (Complex) this.value;
	}
}

class OperatorNode implements Node {
	private Node right;
	private Operator operator;
	private Node left;

	public OperatorNode(Operator operator) {
		this.operator = operator;
	}

	public void setNode(Node right, Node left) {
		this.right = right;
		this.left = left;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return this.right;
	}

	public Node getLeft() {
		return this.left;
	}

	public Operator getOperator() {
		return this.operator;
	}

	public String toString() {
		return "{ \n\t Operator: " + this.operator.toString() + "\n\t right: " + right.toString() + "\n\t left: " + left.toString() + "\n}";
	}
}

class MethodNode implements Node {
	private Method name;
	private Node value;

	public MethodNode(Method name) {
		this.name = name;
	}

	public void setNode(Node value) {
		this.value = value;
	}

	public String getName() {
		return this.name.toString();
	}

	public Node getValue() {
		return this.value;
	}

	public String toString() {
		return "{ \n\t Method: " + getName() + "\n\t value: " + this.value.toString() + "\n}";
	}
}