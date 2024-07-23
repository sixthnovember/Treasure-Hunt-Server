package server.exceptions;

public class CastleCountException extends GenericExampleException {

	private static final long serialVersionUID = 1L;
	private final String errorName;
	private final String errorMessage;

	public CastleCountException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
		this.errorName = errorName;
		this.errorMessage = errorMessage;
	}

	public String getErrorName() {
		return errorName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
