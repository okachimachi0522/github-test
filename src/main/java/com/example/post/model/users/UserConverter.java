package com.example.post.model.users;

public class UserConverter {
	public static User userRegisterFormToUser(UserRegisterForm userRegisterForm) {
		User user = new User();
		user.setUsername(userRegisterForm.getUsername());
		user.setPassword(userRegisterForm.getPassword());
		user.setName(userRegisterForm.getName());
		user.setGender(userRegisterForm.getGender());
		user.setBirthDate(userRegisterForm.getBirthDate());
		user.setEmail(userRegisterForm.getEmail());
		
		return user;
	}
	
	public static UserUpdateForm userToUserUpdateForm(User user) {
		UserUpdateForm userUpdateForm = new UserUpdateForm();
		userUpdateForm.setId(user.getId());
		userUpdateForm.setUsername(user.getUsername());
		userUpdateForm.setPassword(user.getPassword());
		userUpdateForm.setName(user.getName());
		userUpdateForm.setGender(user.getGender());
		userUpdateForm.setBirthDate(user.getBirthDate());
		userUpdateForm.setEmail(user.getEmail());
		
		return userUpdateForm;
	}
	
	public static User userUpdateFormToUser(UserUpdateForm userUpdateForm) {
		User user = new User();
		user.setId(userUpdateForm.getId());
		user.setPassword(userUpdateForm.getPassword());
		user.setEmail(userUpdateForm.getEmail());
		
		return user;
	}
}
