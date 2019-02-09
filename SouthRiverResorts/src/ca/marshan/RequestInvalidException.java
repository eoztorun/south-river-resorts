package ca.marshan;
//By: Martyn Whanslaw
import javax.servlet.ServletException;

@SuppressWarnings("serial")
public class RequestInvalidException extends ServletException {

	public RequestInvalidException() {
		// TODO Auto-generated constructor stub
	}

	public RequestInvalidException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RequestInvalidException(Throwable rootCause) {
		super(rootCause);
		// TODO Auto-generated constructor stub
	}

	public RequestInvalidException(String message, Throwable rootCause) {
		super(message, rootCause);
		// TODO Auto-generated constructor stub
	}

}
