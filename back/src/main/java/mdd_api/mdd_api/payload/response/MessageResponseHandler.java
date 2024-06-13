package mdd_api.mdd_api.payload.response;

public class MessageResponseHandler {
	
	private String message;
	
	public MessageResponseHandler(String message) {
		this.message = message;
	}
	 
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}
