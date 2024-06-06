package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {
    private Long id;
    @Size(min = 2, max = 50)
    private String name;
    @Email
    private String email;
}