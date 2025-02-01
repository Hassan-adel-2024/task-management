package com.todolist.service.impl;

import com.todolist.dto.TaskDto;
import com.todolist.entity.Task;
import com.todolist.entity.TaskStatus;
import com.todolist.exception.ResourceNotFound;
import com.todolist.repository.TaskRepository;
import com.todolist.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public TaskDto addTask(TaskDto taskDto){
        Task task= modelMapper.map(taskDto,Task.class);

        Task addedTask =taskRepository.save(task);
        TaskDto addedDtoTask = modelMapper.map(addedTask,TaskDto.class);

         return addedDtoTask;
    }

    @Override
    public TaskDto findTask(Long id) {
        Task task = taskRepository.findById(id).
                orElseThrow(()-> new ResourceNotFound("Task not fount with id "+id));
        return modelMapper.map(task,TaskDto.class);
    }

    @Override
    public List<TaskDto> findAll() {
        List<Task> taskList = taskRepository.findAll();

        return taskList.stream().map((Task)->modelMapper.map(Task,TaskDto.class)).collect(Collectors.toList());
    }

    @Override
    public TaskDto updateTask(TaskDto updatedTaskDto, Long id) {
        Task updatedTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Resource not found with id : "+id));
        updatedTask.setTitle(updatedTaskDto.getTitle());
        updatedTask.setDescription(updatedTaskDto.getDescription());
        updatedTask.setTaskStatus(updatedTaskDto.getTaskStatus());
        Task savedTask = taskRepository.save(updatedTask);
        return modelMapper.map(savedTask,TaskDto.class);
    }

    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id).
                orElseThrow(()-> new ResourceNotFound("Task not found with id : "+id));
        taskRepository.delete(task);
    }

    @Override
    public TaskDto completeTask(Long id) {
        Task existingTask = taskRepository.findById(id).
                orElseThrow(()-> new ResourceNotFound("Task not found with id : "+id));
        existingTask.setTaskStatus(TaskStatus.COMPLETED);
        taskRepository.save(existingTask);
        return modelMapper.map(existingTask,TaskDto.class);
    }

    @Override
    public TaskDto cancelTask(Long id) {
        Task existingTask = taskRepository.findById(id).
                orElseThrow(()-> new ResourceNotFound("Task not found with id :"+id));
        existingTask.setTaskStatus(TaskStatus.CANCELED);
        taskRepository.save(existingTask);
        return modelMapper.map(existingTask,TaskDto.class);
    }


}
