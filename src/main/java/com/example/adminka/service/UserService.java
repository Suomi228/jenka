package com.example.adminka.service;

import com.example.adminka.dto.UserDto;
import com.example.adminka.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class UserService {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<UserDto> getAllUsers() {
        return users.stream()
                .map(this::toDto)
                .toList();
    }

    public UserDto getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public UserDto createUser(UserDto userDto) {
        User user = toEntity(userDto);
        user.setId(idGenerator.getAndIncrement());
        users.add(user);
        return toDto(user);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        Optional<User> userOpt = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setDescription(userDto.getDescription());
            return toDto(user);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public void deleteUser(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getDescription());
    }

    private User toEntity(UserDto dto) {
        return new User(null, dto.getName(), dto.getEmail(), dto.getDescription());
    }
}

