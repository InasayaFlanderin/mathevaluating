package Matheval;

interface Token {
	public String toString();
	public String toToken();
	public String getType();
}

class Complex implements Token {
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

class Symbol implements Token {
	private char name;

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
}