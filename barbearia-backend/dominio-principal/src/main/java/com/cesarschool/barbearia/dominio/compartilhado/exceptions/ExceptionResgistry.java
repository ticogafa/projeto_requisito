package com.cesarschool.barbearia.dominio.compartilhado.exceptions;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;

public class ExceptionResgistry {

    private Map<Class<?>, Supplier<?>> registry;

    public void register(Exception exception, Supplier<?> handler, HttpStatus status){
        registry.put(exception.getClass(), handler);
    }

    // TODO - terminar isso aq dps
    private Optional<ExceptionAdapter> getExcactMatchAdapter(Exception exceptionInstance){
        Class<?> exceptionClass = exceptionInstance.getClass();
        if (registry.get(exceptionClass) != null){

        }
        return Optional.of(new ExceptionAdapter(exceptionInstance));
    }


    //     def _get_exact_match_adapter(self, exception_instance: Exception) -> Optional[GenericExceptionAdapter]:
//         """Try to find an adapter for the exact exception type."""
//         exception_type = type(exception_instance)
//         cls = None
//         if exception_type in self._registry:
//             adapter_cls, status_code = self._registry[exception_type]
//             cls = adapter_cls(exception_instance, status_code)
//         return cls

//     public  _get_inheritance_match_adapter(self, exception_instance: Exception) -> Optional[GenericExceptionAdapter]:
//         """Try to find an adapter for a parent exception class."""
//         cls = None
//         for exc_cls, (adapter_cls, status_code) in self._registry.items():
//             if isinstance(exception_instance, exc_cls):
//                 cls = adapter_cls(exception_instance, status_code)
//         return cls

}
// from typing import Optional

// from core.exceptions.adapters.base import GenericExceptionAdapter
// from core.utils.singleton import Singleton


// class ExceptionAdapterRegistry(metaclass=Singleton):
//     """
//     A singleton registry for associating exception classes with their corresponding
//     adapter classes and optional HTTP status codes.
//     When an exception instance is provided, the registry retrieves the most appropriate adapter,
//     either by exact type match or by inheritance, defaulting to a generic adapter if no match is found.
//     """
//     def __init__(self):
//         self._registry: dict[
//             type[Exception],
//             tuple[type[GenericExceptionAdapter], Optional[int]]
//         ] = {}

//     def register(
//         self,
//         exception_class: type[Exception],
//         adapter_class: type[GenericExceptionAdapter] = GenericExceptionAdapter,
//         status_code: Optional[int] = None
//     ) -> None:
//         """Register an exception class with its adapter and optional status code."""
//         self._registry[exception_class] = (adapter_class, status_code)

//     def get_adapter(self, exception_instance: Exception) -> GenericExceptionAdapter:
//         """Retrieve the appropriate adapter for the given exception instance."""
//         return (
//             self._get_exact_match_adapter(exception_instance)
//             or self._get_inheritance_match_adapter(exception_instance)
//             or GenericExceptionAdapter(exception_instance)
//         )

