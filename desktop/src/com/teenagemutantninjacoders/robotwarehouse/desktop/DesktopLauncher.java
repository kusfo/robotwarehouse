package com.teenagemutantninjacoders.robotwarehouse.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.IdProvider;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;

import de.golfgl.gdxgamesvcs.NoGameServiceClient;

public class DesktopLauncher {
	public static void main (String[] arg) {
		NoGameServiceClient noGameServiceClient = new NoGameServiceClient();
		FooDesktopTrackingServices fooDesktopTrackingServices = new FooDesktopTrackingServices();
        FooAdServices fooAdServices = new FooAdServices();
        FooPlatformServices fooGameServices = new FooPlatformServices();
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		IdProvider fooIdProvider = new FooIdProvider();
        RobotWarehouseGame robotWarehouseGame;

		config.setTitle("Robot Warehouse");
		config.setWindowedMode(GameConstants.VIRTUAL_HORIZONTAL_RESOLUTION,GameConstants.VIRTUAL_VERTICAL_RESOLUTION);
		if(arg.length == 1) {
			robotWarehouseGame = new RobotWarehouseGame(noGameServiceClient, fooDesktopTrackingServices, fooAdServices,  fooGameServices, fooIdProvider, "Computer", arg[0]);
		} else {
			robotWarehouseGame = new RobotWarehouseGame(noGameServiceClient, fooDesktopTrackingServices, fooAdServices, fooGameServices, fooIdProvider, "Computer" ,true);
		}
		new Lwjgl3Application(robotWarehouseGame, config);
	}
}
