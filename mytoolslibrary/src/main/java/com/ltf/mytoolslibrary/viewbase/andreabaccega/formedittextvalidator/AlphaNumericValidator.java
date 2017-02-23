package com.ltf.mytoolslibrary.viewbase.andreabaccega.formedittextvalidator;


public class AlphaNumericValidator extends RegexpValidator {
	public AlphaNumericValidator(String message) {
		super(message, 	"[a-zA-Z0-9 \\./-]*");
	}
	
}
