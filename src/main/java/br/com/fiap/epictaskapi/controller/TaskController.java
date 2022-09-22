package br.com.fiap.epictaskapi.controller;

import br.com.fiap.epictaskapi.model.Task;
import br.com.fiap.epictaskapi.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService service;
    
    @GetMapping
    @Cacheable("task")
    public Page<Task> index(@PageableDefault(size = 10) Pageable paginacao){
        return service.listAll(paginacao);
    }
    // HashMap - Redis
    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task){
        service.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("{id}")
    public ResponseEntity<Task> show(@PathVariable Long id){
        return ResponseEntity.of(service.getById(id));
    }

    @DeleteMapping("{id}")
    @CacheEvict(value = "task", allEntries = true)
    public ResponseEntity<Object> destroy(@PathVariable Long id){
        var optional = service.getById(id);

        if(optional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @PutMapping("{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task newTask){
        // buscar a tarefa no BD
        var optional = service.getById(id);

        // verificar se existe
        if(optional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        
        // atualizar os valores
        var task = optional.get();
        BeanUtils.copyProperties(newTask, task);
        task.setId(id);

        // salvar no BD
        service.save(task);

        return ResponseEntity.ok(task);
    
    }



}
