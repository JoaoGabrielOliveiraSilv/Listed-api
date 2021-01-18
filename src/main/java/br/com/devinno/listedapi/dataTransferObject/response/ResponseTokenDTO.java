package br.com.devinno.listedapi.dataTransferObject.response;

public class ResponseTokenDTO {
	private String token;
	
	public ResponseTokenDTO(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
