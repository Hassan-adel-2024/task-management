package com.todolist.service;

import com.todolist.dto.TaskDto;
import com.todolist.entity.Task;
import com.todolist.entity.TaskStatus;
import com.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface TaskService {
   TaskDto addTask(TaskDto taskDto);

   TaskDto findTask(Long id);

   List<TaskDto> findAll();

   TaskDto updateTask(TaskDto updatedTaskDto, Long id);

   public void delete(Long id);

   public TaskDto updateTaskStatus(Long id, TaskStatus taskStatus);
}
