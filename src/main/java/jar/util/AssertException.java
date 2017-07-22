package jar.util;

public class AssertException extends AppException {

	private static final String errName = "AssertionError";

	public AssertException(){
		super(errName);
	}
	public AssertException(String message){
		super(errName, message);
	}
}
