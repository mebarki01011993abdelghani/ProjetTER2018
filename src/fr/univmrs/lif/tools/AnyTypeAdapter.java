package fr.univmrs.lif.tools;


import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AnyTypeAdapter extends XmlAdapter<Object, Object> {

	 @Override
	   public Object marshal(Object v) throws Exception {
	    return v;
	   }

	 @Override
	   public Object unmarshal(Object v) throws Exception {
	    return v;
	   }

	}
