package com.cesarschool.barbearia.dominio.compartilhado.exceptions;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ExceptionRegistry {

       private final Map<Class<? extends Exception>, ExceptionEntry> registry = new ConcurrentHashMap<>();

    public void register(Class<? extends Exception> exceptionClass,
                         Class<? extends ExceptionAdapter> adapter,
                         HttpStatus status) {
        registry.put(exceptionClass, new ExceptionEntry(adapter, status));
    }

    public void register(Class<? extends Exception> exceptionClass,
                         HttpStatus status) {
        registry.put(exceptionClass, new ExceptionEntry(ExceptionAdapter.class, status));
    }

    private Optional<ExceptionAdapter> getExactMatchAdapter(Exception ex) {
        Class<?> cls = ex.getClass();
        ExceptionEntry entry = registry.get(cls);
        
        if (entry == null) return Optional.empty();

        return Optional.of(new ExceptionAdapter(ex, entry.getStatus()));
    }
    private Optional<ExceptionAdapter> getInheritanceMatchAdapter(Exception ex) {
        ExceptionEntry found = null;

        for (var e : registry.entrySet()) {
            Class<? extends Exception> registered = e.getKey();
            if (registered.isAssignableFrom(ex.getClass())) {
                found = e.getValue();
            }
        }

        if (found == null) return Optional.empty();

        return Optional.of(new ExceptionAdapter(ex, found.getStatus()));
    }

    public ExceptionAdapter getAdapter(Exception ex) {
        return
            getExactMatchAdapter(ex)
                .or(() -> getInheritanceMatchAdapter(ex))
                .orElse(new ExceptionAdapter(ex));
    }

    /**
     * Configuração padrão de exceções.
     * Chamado automaticamente após construção do bean.
     */
    public void configureDefaults() {
        // Exceções de validação
        register(IllegalArgumentException.class, HttpStatus.BAD_REQUEST);
        register(IllegalStateException.class, HttpStatus.CONFLICT);
        
        // Exceções genéricas
        register(RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        register(Exception.class, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

