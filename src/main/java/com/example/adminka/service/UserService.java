package com.example.adminka.service;

import com.example.adminka.dto.CreateUserDto;
import com.example.adminka.dto.UserDto;
import com.example.adminka.model.User;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        // Инициализация тестовых пользователей
        User user1 = new User();
        user1.setId(idGenerator.getAndIncrement());
        user1.setName("Иван Иванов");
        user1.setEmail("ivan@example.com");
        user1.setDescription("Первый тестовый пользователь");
        users.add(user1);
        
        User user2 = new User();
        user2.setId(idGenerator.getAndIncrement());
        user2.setName("Мария Петрова");
        user2.setEmail("maria@example.com");
        user2.setDescription("Второй тестовый пользователь");
        users.add(user2);
        
        User user3 = new User();
        user3.setId(idGenerator.getAndIncrement());
        user3.setName("Алексей Сидоров");
        user3.setEmail("alex@example.com");
        user3.setDescription("Третий тестовый пользователь");
        users.add(user3);
    }

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

    public UserDto createUser(CreateUserDto createUserDto) {
        // Игнорируем id из DTO, генерируем новый
        User user = new User();
        user.setId(idGenerator.getAndIncrement());
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setDescription(createUserDto.getDescription());
        users.add(user);
        return toDto(user);
    }
    
    // Перегрузка для обратной совместимости
    public UserDto createUser(UserDto userDto) {
        CreateUserDto createUserDto = new CreateUserDto(
            userDto.getName(),
            userDto.getEmail(),
            userDto.getDescription()
        );
        return createUser(createUserDto);
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
}


