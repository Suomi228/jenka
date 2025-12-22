package com.example.adminka.controller;

import com.example.adminka.dto.CreateUserDto;
import com.example.adminka.dto.UserDto;
import com.example.adminka.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен",
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = UserDto.class)))
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя с указанным ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя. ID генерируется автоматически, не передавайте его в запросе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные пользователя")
    })
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Parameter(description = "Данные пользователя (id не требуется, будет сгенерирован автоматически)", required = true)
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateUserDto.class))
            )
            @Valid @RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(createUserDto));
    }

    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя с указанным ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long id,
            @Parameter(description = "Обновленные данные пользователя", required = true)
            @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя с указанным ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}


