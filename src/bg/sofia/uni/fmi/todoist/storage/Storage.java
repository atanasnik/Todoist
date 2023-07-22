package bg.sofia.uni.fmi.todoist.storage;

import bg.sofia.uni.fmi.todoist.exception.AlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;

import java.util.List;
import java.util.Set;

public interface Storage<T> {

    public void add(String identifier, T toAdd) throws AlreadyExistsException;

    public Set<T> list();

}