package com.ibrahim.dubaiconciergerie.demo.service;

import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.entity.User;

import java.util.List;

public interface PropertyService {

    List<Property> getAllPublicProperties();

    Property createProperty(Property property);

    Property getPropertyById(Long id);

    List<Property> getPropertiesForOwner(User owner);

    Property updateProperty(Long id, Property updated);

    void deleteProperty(Long id);
}
