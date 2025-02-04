package com.todolist.controller;

import com.todolist.dto.TaskDto;
import com.todolist.entity.TaskStatus;
import com.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService taskService;
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<TaskDto> add(@RequestBody TaskDto taskDto) {
        TaskDto savedDtTask = taskService.addTask(taskDto);
        return new ResponseEntity<>(savedDtTask, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/find/{id}")
    public ResponseEntity<TaskDto> findByID(@PathVariable Long id){
        TaskDto foundTask = taskService.findTask(id);
        return new ResponseEntity<>(foundTask,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/find_all")
    public ResponseEntity<List<TaskDto>> findAll(){
        List<TaskDto> taskDtoList = taskService.findAll();
        return new ResponseEntity<>(taskDtoList,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity update(@RequestBody TaskDto taskDto ,@PathVariable Long id){
        TaskDto updatedTask = taskService.updateTask(taskDto,id);
        return new ResponseEntity(updatedTask,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        taskService.delete(id);
        return "Task deleted successfully";
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/update_status/{id}")
    public ResponseEntity<TaskDto> completeTask(
            @PathVariable Long id,
            @RequestBody Map<String, TaskStatus> request) {

        TaskStatus newStatus = request.get("taskStatus");
        if (newStatus == null) {
            return ResponseEntity.badRequest().build();
        }

        TaskDto updatedTask = taskService.updateTaskStatus(id, newStatus);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }


}
