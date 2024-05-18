package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.EntityNotFoundException;
import ru.practicum.shareit.common.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ValidationException("Email must be not null");
        }
        if (userDto.getName() == null) {
            throw new ValidationException("Name must be not null");
        }
        User user = UserMapper.fromUserDto(userDto);

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void remove(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        String updatedName = userDto.getName();
        String updatedEmail = userDto.getEmail();
        User stored = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", userId)));
        if (updatedName != null) {
            stored.setName(updatedName);
        }
        if (updatedEmail != null) {
            stored.setEmail(updatedEmail);
        }

        return UserMapper.toUserDto(userRepository.save(stored));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long userId) {
        return UserMapper.toUserDto(userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, String.format("Id = %s", userId))));
    }
}
