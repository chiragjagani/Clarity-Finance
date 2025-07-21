package com.cj.financeapp.exceptions;
/**
 * Exception thrown when a requested resource is not found.
 * This exception is typically used in REST APIs to indicate that
 * a specific resource (like an entity) could not be located.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
