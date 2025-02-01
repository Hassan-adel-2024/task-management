package com.todolist.controller;

import com.todolist.dto.TaskDto;
import com.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService taskService;
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public ResponseEntity<TaskDto> add(@RequestBody TaskDto taskDto) {
        TaskDto savedDtTask = taskService.addTask(taskDto);
        return new ResponseEntity<>(savedDtTask, HttpStatus.CREATED);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<TaskDto> findByID(@PathVariable Long id){
        TaskDto foundTask = taskService.findTask(id);
        return new ResponseEntity<>(foundTask,HttpStatus.OK);
    }
    @GetMapping("/find_all")
    public ResponseEntity<List<TaskDto>> findAll(){
        List<TaskDto> taskDtoList = taskService.findAll();
        return new ResponseEntity<>(taskDtoList,HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity update(@RequestBody TaskDto taskDto ,@PathVariable Long id){
        TaskDto updatedTask = taskService.updateTask(taskDto,id);
        return new ResponseEntity(updatedTask,HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        taskService.delete(id);
        return "Task deleted successfully";
    }
    @PutMapping("/complete/{id}")
    public ResponseEntity<TaskDto> completeTask(@PathVariable Long id){
        TaskDto updatedTask = taskService.completeTask(id);
        return new ResponseEntity<>(updatedTask,HttpStatus.OK);

    }
    @PutMapping("cancel/{id}")
    public ResponseEntity<TaskDto> cancelTask(@PathVariable Long id){
        TaskDto canceledTask = taskService.cancelTask(id);
        return new ResponseEntity<>(canceledTask,HttpStatus.OK);
    }
}
