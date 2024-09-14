package peng.ch2.RPN;

import java.util.Stack;

public class AstPrinter implements Expr.Visitor<String> {
    
    public String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return processOperator(expr.operator, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return expr.expression.accept(this);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return processOperator(expr.operator, expr.right);
    }

    private String processOperator(Token operator, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        for (Expr expr : exprs) {
            builder.append(expr.accept(this)).append(" ");
        }
        builder.append(operator.lexeme);

        return builder.toString();
    }

    public static void main(String[] args) {
        // Create an expression (1 + 2) * (4 - 3)
        Expr expression_1 = new Expr.Binary(
            new Expr.Binary(
                new Expr.Literal(1),
                new Token(TokenType.PLUS, "+", null, 1),
                new Expr.Literal(2)),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Binary(
                new Expr.Literal(4),
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(3))
        );

        // Print the expression in RPN
        System.out.println(new AstPrinter().print(expression_1)); // Output should be: "1 2 + 4 3 - *"

        Expr expression_2 = new Expr.Binary(
          new Expr.Unary(
              new Token(TokenType.MINUS, "-", null, 1),
              new Expr.Literal(123)),
          new Token(TokenType.STAR, "*", null, 1),
          new Expr.Grouping(
              new Expr.Literal(45.67)));
  
        System.out.println(new AstPrinter().print(expression_2));
    }
}
