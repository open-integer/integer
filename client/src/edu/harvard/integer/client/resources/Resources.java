package edu.harvard.integer.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources {
	Images IMAGES = GWT.create(Images.class);
	Icons ICONS = GWT.create(Icons.class);
	
	public interface Images extends ClientBundle {
		
		@Source("/images/DefaultDevice.png")
		ImageResource defaultDevice();
		
		@Source("/images/Network.jpg")
		ImageResource network();

		@Source("/images/Server.png")
		ImageResource server();
		
		@Source("/images/Router.png")
		ImageResource router();
		
		@Source("/images/Unknown.png")
		ImageResource unknown();
		
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

		@Source("/icons/refresh.png")
		ImageResource refresh();
	}
}
