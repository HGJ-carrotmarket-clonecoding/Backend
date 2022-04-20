package exception;

public class NoneLoginException extends RuntimeException{
    public NoneLoginException(String msg){
        super(msg);
    }
}
