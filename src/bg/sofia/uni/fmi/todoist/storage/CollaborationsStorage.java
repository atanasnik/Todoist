package bg.sofia.uni.fmi.todoist.storage;

import bg.sofia.uni.fmi.todoist.collaboration.Collaboration;
import bg.sofia.uni.fmi.todoist.exception.AlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.CollaborationAlreadyExistsException;
import bg.sofia.uni.fmi.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.todoist.exception.NotFoundException;
import bg.sofia.uni.fmi.todoist.validation.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CollaborationsStorage implements Storage<Collaboration> {

    private static final String COLLABORATION_NAME = "Collaboration name";
    private static final String COLLABORATION_OBJECT = "Collaboration";

    private final Map<String, Collaboration> collaborations;

    {
        collaborations = new HashMap<>();
    }

    @Override
    public void add(String identifier, Collaboration toAdd) throws AlreadyExistsException {
        Validator.stringArgument(identifier, COLLABORATION_NAME);
        Validator.objectArgument(toAdd, COLLABORATION_OBJECT);

        if (collaborations.containsKey(identifier)) {
            throw new CollaborationAlreadyExistsException("Collaboration already exists");
        }

        collaborations.put(identifier, toAdd);
    }

    @Override
    public Set<Collaboration> list() {
        return Set.copyOf(collaborations.values());
    }

    public void remove(String identifier) throws NotFoundException {
        Validator.stringArgument(identifier, COLLABORATION_NAME);

        if (!collaborations.containsKey(identifier)) {
            throw new CollaborationNotFoundException("Collaboration not found");
        }

        collaborations.remove(identifier);
    }

    public Collaboration getByIdentifier(String identifier) throws NotFoundException {
        Validator.stringArgument(identifier, COLLABORATION_NAME);

        if (!collaborations.containsKey(identifier)) {
            throw new CollaborationNotFoundException("Collaboration not found");
        }

        return collaborations.get(identifier);
    }

    public boolean contains(String identifier) {
        return collaborations.containsKey(identifier);
    }

}
