package dk.itu.jcafdemo.entity;

import dk.itu.jcafdemo.item.Presentation;
import dk.pervasive.jcaf.ContextEvent;
import dk.pervasive.jcaf.ContextItem;
import dk.pervasive.jcaf.entity.Person;

public class Visitor extends Person {

	public Visitor() {
		super();
	}
	/**
	 * Constructor for Visitor.
	 * @param id
	 */ 
	public Visitor(String id) {
		super(id);
	}

	/**
	 * Constructor for Visitor.
	 * @param id
	 * @param name
	 */
	public Visitor(String id, String name) {
		super(id, name);
	}	
	
	@Override
	public String getEntityInfo() {
		return "Visitor entity";
	}
	
	@Override
	public void contextChanged(ContextEvent event) {
		super.contextChanged(event);

		ContextItem item = event.getItem();
		if(item != null) {
			
			if(item instanceof Presentation) {
				System.out.println("Name: " + getName() + " @ Presentation: " + ((Presentation)event.getItem()).getName());
			} else {
				
			} if(item instanceof Room) {
				System.out.println("Name: " + getName() + " in Room: " + ((Room)event.getItem()).toString());
			}
			
		}
		
	}
}
