package br.com.minhastarefas.controllers;

import br.com.minhastarefas.dtos.TaskRecordDto;
import br.com.minhastarefas.models.TaskModel;
import br.com.minhastarefas.repositories.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Tag(name = "Task", description = "Endpoints for managing tasks")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;


    @PostMapping("/tasks")
    @Operation(summary = "Adds a new Task", description = "Adds a new Task", tags = {"Task"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
    public ResponseEntity<TaskModel> saveTask(@RequestBody @Valid TaskRecordDto taskRecordDto) {
        var taskModel = new TaskModel();
        BeanUtils.copyProperties(taskRecordDto, taskModel); // Recebe o taskRecordDto e será convertido no taskModel.
        return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(taskModel));
    }
    // Sem a anotação @Valid nenhuma validação seria feita

    @GetMapping("/tasks")
    @Operation(summary = "Finds all Task", description = "Finds all Task", tags = {"Task"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
    public ResponseEntity<List<TaskModel>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.findAll());
    }

    @GetMapping("/tasks/{id}")
    @Operation(summary = "Finds a Task", description = "Finds a Task", tags = {"Task"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
    public ResponseEntity<Object> getOneTask(@PathVariable(value = "id") UUID id) {
        Optional<TaskModel> taskO = taskRepository.findById(id);
        if (taskO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(taskO.get());
    }

    @PutMapping("/tasks/{id}")
    @Operation(summary = "Updates a Task", description = "Updates a Task", tags = {"Task"}, responses = {
            @ApiResponse(description = "Updated", responseCode = "200", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
    public ResponseEntity<Object> updateTask(@PathVariable(value = "id") UUID id,
                                             @RequestBody @Valid TaskRecordDto taskRecordDto) {
        Optional<TaskModel> taskO = taskRepository.findById(id);
        if (taskO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Task not found."));
        }
        var taskModel = taskO.get();
        BeanUtils.copyProperties(taskRecordDto, taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.save(taskModel));
    }

    @DeleteMapping("/tasks/{id}")
    @Operation(summary = "Deletes a Task", description = "Deletes a Task", tags = {"Task"}, responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
    public ResponseEntity<Object> deleteTask(@PathVariable(value = "id") UUID id) {
        Optional<TaskModel> taskO = taskRepository.findById(id);
        if (taskO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Task not found"));
        }
        taskRepository.delete(taskO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
    }
}