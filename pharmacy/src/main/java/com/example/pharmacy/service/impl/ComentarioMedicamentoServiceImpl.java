package com.example.pharmacy.service.impl;

import com.example.pharmacy.model.ComentarioMedicamento;
import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.repository.ComentarioMedicamentoRepository;
import com.example.pharmacy.repository.UsuarioRepository;
import com.example.pharmacy.service.ComentarioMedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComentarioMedicamentoServiceImpl implements ComentarioMedicamentoService {

    @Autowired
    private ComentarioMedicamentoRepository comentarioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public ComentarioMedicamento create(ComentarioMedicamento comentario) {
        if (comentario.getFecha() == null) {
            comentario.setFecha(LocalDateTime.now());
        }
        
        return comentarioRepository.save(comentario);
    }

    @Override
    public ComentarioMedicamento findById(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }

    @Override
    public List<ComentarioMedicamento> findByMedicamento(Long idMedicamento) {
        List<ComentarioMedicamento> comentarios = comentarioRepository.findByIdMedicamento(idMedicamento);
        
        for (ComentarioMedicamento comentario : comentarios) {
            if (comentario.getIdUsuario() != null) {
                usuarioRepository.findById(comentario.getIdUsuario()).ifPresent(usuario -> {
                    comentario.setNombreUsuario(usuario.getNombre());
                });
            }
        }
        
        return comentarios;
    }

    @Override
    public List<ComentarioMedicamento> findByMedicamentoHierarchical(Long idMedicamento) {
        List<ComentarioMedicamento> allComments = findByMedicamento(idMedicamento);
        System.out.println("ComentarioService - Total comments found for medicamento " + idMedicamento + ": " + allComments.size());
        
        allComments.forEach(comment -> {
            System.out.println("Comment ID: " + comment.getIdComentario() + 
                             ", Parent ID: " + comment.getParentId() + 
                             ", Text: " + comment.getTexto().substring(0, Math.min(20, comment.getTexto().length())) + "...");
        });
        
        Map<Long, ComentarioMedicamento> commentMap = new HashMap<>();
        for (ComentarioMedicamento comment : allComments) {
            comment.setRespuestas(new ArrayList<>()); 
            commentMap.put(comment.getIdComentario(), comment);
            System.out.println("Mapped comment ID " + comment.getIdComentario() + " to map");
        }
        
        List<ComentarioMedicamento> rootComments = new ArrayList<>();
        
        for (ComentarioMedicamento comment : allComments) {
            if (comment.getParentId() == null) {
                rootComments.add(comment);
                System.out.println("Added root comment ID " + comment.getIdComentario());
            } else {
                ComentarioMedicamento parent = commentMap.get(comment.getParentId());
                if (parent != null) {
                    parent.getRespuestas().add(comment);
                    System.out.println("Added comment ID " + comment.getIdComentario() + 
                                     " as reply to parent ID " + comment.getParentId());
                } else {
                    System.out.println("Warning: Parent comment ID " + comment.getParentId() + 
                                     " not found for comment ID " + comment.getIdComentario() + 
                                     ". Adding as root comment.");
                    rootComments.add(comment);
                }
            }
        }
        
        System.out.println("\nFinal hierarchical structure:");
        System.out.println("Root comments count: " + rootComments.size());
        rootComments.forEach(root -> {
            System.out.println("Root comment ID " + root.getIdComentario() + 
                             " has " + root.getRespuestas().size() + " direct replies");
            root.getRespuestas().forEach(reply -> 
                System.out.println("  - Reply ID " + reply.getIdComentario() + 
                                 " to comment " + root.getIdComentario()));
        });
        
        return rootComments;
    }

    @Override
    public void delete(Long id) {
        ComentarioMedicamento comentario = findById(id);
        List<ComentarioMedicamento> allComments = findByMedicamento(comentario.getIdMedicamento());
        
        Map<Long, List<ComentarioMedicamento>> childrenMap = new HashMap<>();
        for (ComentarioMedicamento c : allComments) {
            if (c.getParentId() != null) {
                childrenMap.computeIfAbsent(c.getParentId(), k -> new ArrayList<>()).add(c);
            }
        }
        
        List<Long> deleteOrder = new ArrayList<>();
        collectCommentsToDelete(id, childrenMap, deleteOrder);
        
        System.out.println("Deleting comments in order: " + deleteOrder);
        
        for (Long commentId : deleteOrder) {
            System.out.println("Deleting comment ID: " + commentId);
            comentarioRepository.deleteById(commentId);
        }
    }

    private void collectCommentsToDelete(Long commentId, Map<Long, List<ComentarioMedicamento>> childrenMap, List<Long> deleteOrder) {
        List<ComentarioMedicamento> children = childrenMap.getOrDefault(commentId, new ArrayList<>());
        for (ComentarioMedicamento child : children) {
            collectCommentsToDelete(child.getIdComentario(), childrenMap, deleteOrder);
        }
        
        deleteOrder.add(commentId);
    }
} 
