package simplenim;

public class NimError extends Exception{

	private static final long serialVersionUID = 1L;
	int errorCode;
	  String description;

	  NimError(int paramInt)
	  {
	    this.errorCode = paramInt; } 
	  NimError(int paramInt, String paramString) { this.errorCode = paramInt; this.description = paramString;
	  }
}
