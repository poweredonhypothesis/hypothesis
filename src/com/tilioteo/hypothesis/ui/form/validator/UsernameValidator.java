package com.tilioteo.hypothesis.ui.form.validator;

import com.tilioteo.hypothesis.core.Messages;
import com.tilioteo.hypothesis.persistence.UserService;
import com.vaadin.data.Validator;

@SuppressWarnings("serial")
public class UsernameValidator implements Validator {

	private UserService userService;
	private Long id;
	
	public UsernameValidator(Long id) {
		this.id = id;
		userService = UserService.newInstance();
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (userService.usernameExists(id, (String) value)) {
			throw new InvalidValueException(
					Messages.getString("Message.Error.UsernameExists"));
		}
	}

}
