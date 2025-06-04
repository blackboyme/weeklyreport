package com.caac.weeklyreport.controller;

import com.caac.weeklyreport.entity.Task;
import com.caac.weeklyreport.service.TaskService;
import com.caac.weeklyreport.util.TokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    private String getCurrentUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            return TokenUtil.getUserIdFromToken(token);
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        task.setUserId(userId);
        boolean saved = taskService.save(task);
        if (saved) {
            Task createdTask = taskService.getById(task.getId());
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id, HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        Task task = taskService.getById(id);
        if (task != null && task.getUserId().equals(userId)) {
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        List<Task> tasks = taskService.getAllTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task task, HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        Task existingTask = taskService.getById(id);
        if (existingTask != null && existingTask.getUserId().equals(userId)) {
            task.setId(id);
            task.setUserId(userId);
            Task updatedTask = taskService.getUpdatedTask(task);
            return ResponseEntity.ok(updatedTask);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id, HttpServletRequest request) {
        String userId = getCurrentUserId(request);
        Task existingTask = taskService.getById(id);
        if (existingTask != null && existingTask.getUserId().equals(userId)) {
            taskService.removeById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
