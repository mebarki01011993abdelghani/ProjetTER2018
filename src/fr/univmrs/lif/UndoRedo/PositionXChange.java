package fr.univmrs.lif.UndoRedo;

import java.util.Objects;
import java.util.Optional;

import org.reactfx.Change;

import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.tools.Point2DProperty;

public class PositionXChange extends ComponentChange<Double> {
    private Component component;

	public PositionXChange(Double oldValue, Double newValue) {
        super(oldValue, newValue);
    }
    public PositionXChange(Change<Number> c) {
        super(c.getOldValue().doubleValue(), c.getNewValue().doubleValue());
    }
    @Override
	public void redo() { component.getPosition().xProperty().setValue(newValue); }
    @Override
	public PositionXChange invert() { return new PositionXChange(newValue, oldValue); }
    @Override
	public Optional<ComponentChange<?>> mergeWith(ComponentChange<?> other) {
        if(other instanceof PositionXChange) {
            return Optional.of(new PositionXChange(oldValue, ((PositionXChange) other).newValue));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof PositionXChange) {
        	PositionXChange that = (PositionXChange) other;
            return Objects.equals(this.oldValue, that.oldValue)
                && Objects.equals(this.newValue, that.newValue);
        } else {
            return false;
        }
    }
}
