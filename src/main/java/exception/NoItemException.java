package exception;

public class NoItemException extends RuntimeException{
    public NoItemException(String msg){
        super(msg);
    }
}
