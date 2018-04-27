package fr.univmrs.lif.view.component;

import fr.univmrs.lif.enumeration.ComponentFamily;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.Settings;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.component.alu.Adder;
import fr.univmrs.lif.model.component.msi.Decoder;
import fr.univmrs.lif.model.component.msi.Multiplexer;
import fr.univmrs.lif.tools.Point2DProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.When;
import javafx.beans.binding.When.BooleanConditionBuilder;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;

public class PlugViewFactory {

	/**
	 * Build plugview with correct binding to component
	 * @param componentView
	 * @param pinIndice
	 * @return
	 */
	public static PlugView buildPlugInView(ComponentView componentView, Plug plug) {

		Component component = componentView.getModel();
		Pane draw = componentView.getComponentDraw();
		ComponentType type = component.getType();

		Point2DProperty head = new Point2DProperty();
		Point2DProperty tail = new Point2DProperty();

		if(type == ComponentType.MUX)
			return buildMuxPlugIns(componentView,plug);
		if(type == ComponentType.DECODER)
			return buildDecPlugIns(componentView,plug);
		if(type == ComponentType.ADDER)
			return buildAdderPlugIns(componentView,plug);

		// plug input left side
		if(component.getFamily() == ComponentFamily.GATE ||
				(type == ComponentType.DISPLAY)||
				(type == ComponentType.MODULE) ||
				(type == ComponentType.OUTPUTMODULE) 
				){
			head.xProperty().bind(componentView.layoutXProperty().add(draw.layoutXProperty().subtract(Settings.WIRE_LENGTH)));
			head.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.heightProperty().divide(component.getInputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.heightProperty().divide(component.getInputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
		}// Bottom
		else if(type == ComponentType.LED){
			head.xProperty().bind(componentView.layoutXProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.widthProperty().divide(component.getInputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
			head.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().add(Settings.WIRE_LENGTH)));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.widthProperty().divide(component.getInputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
		}// Right
		else if(type == null){
			head.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().add(Settings.WIRE_LENGTH)));
			head.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.heightProperty().divide(component.getInputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.heightProperty().divide(component.getInputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
		}// Top
		else if(type == null){
			head.xProperty().bind(componentView.layoutXProperty().add(Bindings.multiply(draw.widthProperty().divide(component.getInputNumberProperty().add(1)),plug.indiceProperty().add(1))));
			head.yProperty().bind(componentView.layoutYProperty().add((draw.layoutYProperty().subtract(Settings.WIRE_LENGTH))));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.widthProperty().divide(component.getInputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
		}

		return new PlugView(plug,head, tail);

	}



	


	public static PlugView buildPlugOutView(ComponentView componentView,Plug plug ) {
		Component component = componentView.getModel();
		Pane draw = componentView.getComponentDraw();
		ComponentType type = component.getType();
		Point2DProperty head = new Point2DProperty();
		Point2DProperty tail = new Point2DProperty();

		if(type == ComponentType.MUX)
			return buildMuxPlugOuts(componentView,plug);
		if(type == ComponentType.DECODER)
			return buildDecPlugOuts(componentView,plug);
		if(type == ComponentType.ADDER)
			return buildAdderPlugOuts(componentView,plug);

		if(type == ComponentType.JOINT){
			if(plug.getIndice() == 0){
				head.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().add(Settings.WIRE_LENGTH)));
				head.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty().add(draw.heightProperty().divide(2))));
				tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
				tail.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty().add(draw.heightProperty().divide(2))));

			}else{//bot
				head.xProperty().bind(componentView.layoutXProperty().add(draw.layoutYProperty().add(draw.widthProperty().divide(2))));
				head.yProperty().bind(Bindings.when(plug.connectedProperty().not()).then(
						componentView.layoutYProperty().add(draw.heightProperty().add(Settings.WIRE_LENGTH))).
						otherwise(componentView.layoutYProperty().add(draw.heightProperty().divide(2))));


				tail.xProperty().bind(componentView.layoutXProperty().add(draw.layoutYProperty().add(draw.widthProperty().divide(2))));
				tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
			}
			return new PlugView(plug,head, tail);
		}
		// Right
		if((component.getFamily() == ComponentFamily.GATE)||
				(type == ComponentType.SWITCH) ||
				(type == ComponentType.CLOCK)||
				(type == ComponentType.INPUTMODULE) ||
				(type == ComponentType.MODULE)){
			head.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().add(Settings.WIRE_LENGTH)));
			head.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.heightProperty().divide(component.getOutputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.heightProperty().divide(component.getOutputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
		} // Left
		else if(type == null){
			head.xProperty().bind(componentView.layoutXProperty().add(draw.layoutXProperty().subtract(Settings.WIRE_LENGTH)));
			head.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.heightProperty().divide(component.getOutputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.heightProperty().divide(component.getOutputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
		}
		else if(type == ComponentType.VDD){
			head.xProperty().bind(componentView.layoutXProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.widthProperty().divide(component.getOutputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
			head.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().add(Settings.WIRE_LENGTH)));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.widthProperty().divide(component.getOutputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty()));
		}
		else if(type == ComponentType.GND){
			head.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
			head.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty().subtract(Settings.WIRE_LENGTH)));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.layoutYProperty()));
		}



		return new PlugView(plug,head, tail);

	}

	



	//TODO mux multibit
	private static PlugView buildMuxPlugIns(ComponentView componentView, Plug plug) {

		Multiplexer component = (Multiplexer) componentView.getModel();
		Pane draw = componentView.getComponentDraw();


		Point2DProperty head = new Point2DProperty();
		Point2DProperty tail = new Point2DProperty();

		if(plug.getIndice() < component.getBit()){
			head.xProperty().bind(componentView.layoutXProperty().add(draw.layoutXProperty().subtract(Settings.WIRE_LENGTH)));
			head.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
		}else{
			head.xProperty().bind(componentView.layoutXProperty().add(Bindings.multiply(draw.widthProperty().divide(component.getInputNumberProperty()),plug.indiceProperty())));
			head.yProperty().bind(componentView.layoutYProperty().add((draw.layoutYProperty().subtract(Settings.WIRE_LENGTH))));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.widthProperty().divide(component.getInputNumberProperty()),plug.indiceProperty()))));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
		}

		return new PlugView(plug, head, tail);
	}

	private static PlugView buildMuxPlugOuts(ComponentView componentView, Plug plug) {
	
		Pane draw = componentView.getComponentDraw();


		Point2DProperty head = new Point2DProperty();
		Point2DProperty tail = new Point2DProperty();

		head.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
		head.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().add(Settings.WIRE_LENGTH)));
		tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
		tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));

		return new PlugView(plug, head, tail);
	}

	//TODO dec multibit
	private static PlugView buildDecPlugIns(ComponentView componentView, Plug plug) {

	
		Pane draw = componentView.getComponentDraw();


		Point2DProperty head = new Point2DProperty();
		Point2DProperty tail = new Point2DProperty();

		head.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
		head.yProperty().bind(componentView.layoutYProperty().add((draw.layoutYProperty().subtract(Settings.WIRE_LENGTH))));
		tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
		tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
	

		return new PlugView(plug, head, tail);
	}

	private static PlugView buildDecPlugOuts(ComponentView componentView, Plug plug) {
		Decoder component = (Decoder) componentView.getModel();
		Pane draw = componentView.getComponentDraw();


		Point2DProperty head = new Point2DProperty();
		Point2DProperty tail = new Point2DProperty();
		
		head.xProperty().bind(componentView.layoutXProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.widthProperty().divide(component.getOutputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
		head.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().add(Settings.WIRE_LENGTH)));
		tail.xProperty().bind(componentView.layoutXProperty().add(draw.layoutYProperty().add(Bindings.multiply(draw.widthProperty().divide(component.getOutputNumberProperty().add(1)),plug.indiceProperty().add(1)))));
		tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));


		return new PlugView(plug, head, tail);
	}
	
	private static PlugView buildAdderPlugIns(ComponentView componentView, Plug plug) {
	
		Pane draw = componentView.getComponentDraw();


		Point2DProperty head = new Point2DProperty();
		Point2DProperty tail = new Point2DProperty();
		if(plug.getIndice() == 0){
			head.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().add(Settings.WIRE_LENGTH)));
			head.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
			}else{
			head.xProperty().bind(componentView.layoutXProperty().add(Bindings.multiply(draw.widthProperty().divide(4),plug.indiceProperty().multiply(2).subtract(1))));
			head.yProperty().bind(componentView.layoutYProperty().add((draw.layoutYProperty().subtract(Settings.WIRE_LENGTH))));
			tail.xProperty().bind(componentView.layoutXProperty().add(Bindings.multiply(draw.widthProperty().divide(4),plug.indiceProperty().multiply(2).subtract(1))));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
		}

		return new PlugView(plug, head, tail);
	}

	private static PlugView buildAdderPlugOuts(ComponentView componentView, Plug plug) {
		
		Pane draw = componentView.getComponentDraw();


		Point2DProperty head = new Point2DProperty();
		Point2DProperty tail = new Point2DProperty();
		if(plug.getIndice() == 0){
			head.xProperty().bind(componentView.layoutXProperty().add(draw.layoutXProperty().subtract(Settings.WIRE_LENGTH)));
			head.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
		
		}else{
			head.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
			head.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().add(Settings.WIRE_LENGTH)));
			tail.xProperty().bind(componentView.layoutXProperty().add(draw.widthProperty().divide(2)));
			tail.yProperty().bind(componentView.layoutYProperty().add(draw.heightProperty().divide(2)));
		}

		return new PlugView(plug, head, tail);
	}
}
