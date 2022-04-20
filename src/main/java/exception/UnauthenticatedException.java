package exception;

public class UnauthenticatedException extends RuntimeException{
    public UnauthenticatedException(String msg){
        super(msg);
    }

}
