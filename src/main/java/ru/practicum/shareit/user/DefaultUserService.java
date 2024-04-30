package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.AlreadyExistException;
import ru.practicum.shareit.common.NotFoundException;
import ru.practicum.shareit.common.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        String email = userDto.getEmail();
        String name = userDto.getName();
        if (email == null) {
            throw new ValidationException("Email must be not null");
        }
        if (name == null) {
            throw new ValidationException("Name must be not null");
        }
        User stored = userRepository.findByEmail(email);
        if (stored != null) {
            throw new AlreadyExistException(User.class, String.format("Email = %s", email));
        }
        return UserMapper.toUserDto(userRepository.create(UserMapper.fromUserDto(userDto)));
    }

    @Override
    public void remove(int userId) {
        userRepository.remove(userId);
    }

    @Override
    public UserDto update(int userId, User user) {
        user.setId(userId);
        User stored = userRepository.findById(userId);
        if (stored == null) {
            throw new NotFoundException(User.class, String.format("Id = %s", userId));
        }
        return UserMapper.toUserDto(userRepository.update(user));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Integer userId) {
        return UserMapper.toUserDto(userRepository.findById(userId));
    }
}
