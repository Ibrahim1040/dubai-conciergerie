package com.ibrahim.dubaiconciergerie.demo.service;

import com.ibrahim.dubaiconciergerie.demo.dto.PropertyDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.entity.User;
import jakarta.validation.Valid;

import java.util.List;

public interface PropertyService {

    List<Property> getAll();

    Property createProperty(Property property);

    Property getById(Long id);

    List<Property> getAllPublicProperties();

    Property getPropertyForOwner(Long propertyId, User owner);

    List<Property> getPropertiesForOwner(User owner);

    Property getOwnerProperty(Long ownerId, Long propertyId);

    Property updateOwnerProperty(Long ownerId, Long propertyId, Property updates);

    void deleteOwnerProperty(Long ownerId, Long propertyId);
}
