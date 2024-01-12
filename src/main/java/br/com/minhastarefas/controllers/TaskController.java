package br.com.minhastarefas.controllers;

import br.com.minhastarefas.dtos.TaskRecordDto;
import br.com.minhastarefas.models.TaskModel;
import br.com.minhastarefas.repositories.TaskRepository;
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
public class TaskController {

    @Autowired
    TaskRepository taskRepository;


    @PostMapping("/tasks")
    public ResponseEntity<TaskModel> saveTask(@RequestBody @Valid TaskRecordDto taskRecordDto) {
        var taskModel = new TaskModel();
        BeanUtils.copyProperties(taskRecordDto, taskModel); // Recebe o taskRecordDto e será convertido no taskModel.
        return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(taskModel));
    }
    // Sem a anotação @Valid nenhuma validação seria feita

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskModel>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.findAll());
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Object> getOneTask(@PathVariable(value = "id") UUID id) {
        Optional<TaskModel> taskO = taskRepository.findById(id);
        if (taskO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(taskO.get());
    }

    @PutMapping("/tasks/{id}")
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
    public ResponseEntity<Object> deleteTask(@PathVariable(value = "id") UUID id) {
        Optional<TaskModel> taskO = taskRepository.findById(id);
        if (taskO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Task not found"));
        }
        taskRepository.delete(taskO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
    }
}