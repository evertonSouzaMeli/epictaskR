package br.com.fiap.epictaskapi.service;

import br.com.fiap.epictaskapi.model.Task;
import br.com.fiap.epictaskapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TaskService {
    
    @Autowired
    TaskRepository repository;

    public Page<Task> listAll(Pageable paginacao){
        return repository.findAll(paginacao);
    }

    public void save(Task task) {
        repository.save(task);
    }

    public Optional<Task> getById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}