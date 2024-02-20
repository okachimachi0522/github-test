package com.example.post.model.users;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterForm {
	/*
	 * @Size: 문자 길이 측정
	 * @NotNull: null 불가
	 * @NotEmpty: null, "" 불가
	 * @NotBlank: null, "" 불가
	 * @Past: 과거 날짜만 가능
	 * @PastOrPresent: 오늘이거나 과거 날짜만 가능
	 * @Future: 미래 날짜만 가능
	 * @FutureOrPresent: 오늘이거나 미래 날짜만 가능
	 * @Pattern: 정규식 적용
	 * @Max: 최대값
	 * @Min: 최소값
	 * @Valid: 해당 객체의 Validation 적용
	 */
	// 4자리 이상 20자리 이하
	@Size(min = 4, max = 20)
	private String username;		// 로그인 아이디
	@Size(min = 4, max = 20)
	private String password;		// 로그인 패스워드
	@NotBlank
	private String name;			// 이름
	@NotNull(message = "성별은 반드시 선택 해야 합니다.")
	private GenderType gender;		// 성별
	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;	// 생년월일
	private String email;			// 이메일 주소
}
