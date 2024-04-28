package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    @Override
    public UserDto create(User user) {
        return UserDto.toUserDto(userRepository.create(user));
    }

    @Override
    public void remove(int userId) {
        userRepository.remove(userId);
    }

    @Override
    public UserDto update(User user) {
        return UserDto.toUserDto(userRepository.update(user));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Integer userId) {
        return UserDto.toUserDto(userRepository.findById(userId));
    }
}
