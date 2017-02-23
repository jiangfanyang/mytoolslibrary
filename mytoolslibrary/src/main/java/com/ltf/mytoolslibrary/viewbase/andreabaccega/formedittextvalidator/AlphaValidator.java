package com.ltf.mytoolslibrary.viewbase.andreabaccega.formedittextvalidator;

public class AlphaValidator extends RegexpValidator {
	public AlphaValidator(String message) {
		super(message, "[a-zA-Z \\./-]*");
	}
}
