package edu.harvard.integer.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources {
	Images IMAGES = GWT.create(Images.class);
	Icons ICONS = GWT.create(Icons.class);
	
	public interface Images extends ClientBundle {

		@Source("/images/grayRouter.jpg")
		ImageResource grayRouter();
		
		@Source("/images/graySwitch.jpg")
		ImageResource graySwitch();
		
		@Source("/images/greenRouter.jpg")
		ImageResource greenRouter();
		
		@Source("/images/redRouter.jpg")
		ImageResource redRouter();
		
		@Source("/images/pcom.jpg")
		ImageResource pcom();
		
		@Source("/images/wirelessRouter80.jpg")
		ImageResource wirelessRouter80();
		
		@Source("/images/wirelessRouter128.jpg")
		ImageResource wirelessRoute128();

	}
	
	public interface Icons extends ClientBundle {
		
		@Source("/icons/cog.png")
		ImageResource cog();
		
		@Source("/icons/package.png")
		ImageResource packages();
		
		@Source("/icons/about.png")
		ImageResource about();
		
		@Source("/icons/disk.png")
		ImageResource disk();

	}
}
