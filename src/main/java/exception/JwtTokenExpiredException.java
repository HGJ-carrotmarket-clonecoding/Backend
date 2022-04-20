package exception;


public class JwtTokenExpiredException extends RuntimeException{
    public JwtTokenExpiredException(String msg){
        super(msg);
    }
}
