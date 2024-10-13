package com.ticketPing.user.domain.entity;

import audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import com.ticketPing.user.presentation.request.CreateUserRequest;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    private String email;
    private String password;
    private String nickname;
    private LocalDate birthday;
    private Gender gender;

    public static User from(CreateUserRequest request, String encodedPassword) {
        return User.builder()
                .email(request.email())
                .password(encodedPassword)
                .nickname(request.nickname())
                .birthday(request.birthday())
                .gender(Gender.getGender(request.gender()))
                .build();
    }
}
