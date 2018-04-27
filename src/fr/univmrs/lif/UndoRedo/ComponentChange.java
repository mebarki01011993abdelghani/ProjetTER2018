package fr.univmrs.lif.UndoRedo;

import java.util.Objects;
import java.util.Optional;

public abstract class ComponentChange<T> {
    protected final T oldValue;
    protected final T newValue;

    protected ComponentChange(T oldValue, T newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }



	public abstract void redo();
    public abstract ComponentChange<T> invert();

    public Optional<ComponentChange<?>> mergeWith(ComponentChange<?> other) {
        // don't merge changes by default
        return Optional.empty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldValue, newValue);
    }
};
