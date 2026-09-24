package com.teenagemutantninjacoders.robotwarehouse.domain;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SortedIntList;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.EpisodeDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.TestLevelModelProvider;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.AdServices;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.IdProvider;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.PlatformServices;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.TrackingServices;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.screens.LogoScreen;
import com.teenagemutantninjacoders.robotwarehouse.display.screens.MainScreen;
import com.teenagemutantninjacoders.robotwarehouse.display.screens.SelectionScreen;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.GameController;

import java.util.ArrayList;

import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.IGameServiceListener;


public class RobotWarehouseGame extends Game implements IGameServiceListener {
    public IGameServiceClient  playServices;
    public final TrackingServices trackingServices;
    public final AdServices adServices;
    public final PlatformServices platformServices;
    public final IdProvider idProvider;
    private Screen actualScreen;
	private final boolean debugBuild;
	private String deviceID;
    private String launchLevel = "";
	private String leaderBoardId = "CgkIrvLv5PcIEAIQBg"; //TODO fix that
    private boolean dynamicAssets = false;

	public RobotWarehouseGame(IGameServiceClient playServices, TrackingServices trackingServices, AdServices adServices, PlatformServices platformServices, IdProvider idProvider, String deviceID, String launchLevel) { //FIXME fix changed signature
    	this(playServices, trackingServices, adServices, platformServices,  idProvider, deviceID,true);
    	this.launchLevel = launchLevel;
	}

	public RobotWarehouseGame(IGameServiceClient  playServices, TrackingServices trackingServices, AdServices adServices, PlatformServices platformServices, IdProvider idProvider, String deviceId, boolean debugBuild) {
		this.playServices = playServices;
		this.trackingServices = trackingServices;
		this.adServices = adServices;
		this.platformServices = platformServices;
		this.debugBuild = debugBuild;
		this.idProvider = idProvider;
		this.deviceID = deviceId;
	}

	@Override
	public void create () {
    	playServices.setListener(this);
    	playServices.resumeSession();
    	GlobalPreferencesData.getInstance().setDeviceID(deviceID);
    	GlobalPreferencesData.getInstance().checkGamePreferences();

		I18NBundle globalBundleGameData = createGlobalBundleGameData();
		GlobalGeneralData.getInstance().setGlobalBundleData(globalBundleGameData);
		I18NBundle tutorialBundleData = createTutorialBundleData();
		GlobalGeneralData.getInstance().setTutorialBundleData(tutorialBundleData);

		// Tomamos el control de la tecla back (android) para darle un uso personalizado
		Gdx.input.setCatchKey(Input.Keys.BACK,true);

        // Cargamos los assets totales o solo los base si la carga dinámica está activada
		Assets.initialize(Gdx.app.getType());
		if(dynamicAssets)
            Assets.loadBaseAssets();
		else
		    Assets.loadAllAssets();

		loadEpisodes();
		if(launchLevel.isEmpty()) {
			if(debugBuild) {
				GlobalGeneralData.getInstance().debug_keys = true;
				//goToScreen(SCREEN_TYPE.LOGO_SCREEN);
				goToScreen(SCREEN_TYPE.MAIN_SCREEN);
				//goToScreen(SCREEN_TYPE.SELECTION_SCREEN);
				//goToScreen(SCREEN_TYPE.GAME_SCREEN);
			} else {
				goToScreen(SCREEN_TYPE.LOGO_SCREEN);
			}
		} else {
			GlobalGeneralData.getInstance().debug_keys = true;
		    goToScreen(SCREEN_TYPE.GAME_SCREEN);
		}
	}

	private void loadEpisodes(){
		FileHandle file;
		String levelJson;

		// Indice episodios
        file = Gdx.files.internal("data/episodes/episodesIndex.json");
        levelJson = file.readString();
        Json json = new Json();
		ArrayList<String> episodesList = json.fromJson(ArrayList.class, String.class, levelJson);

		// Datos de episodios
		SortedIntList<Integer> episodesByNumber = new SortedIntList<Integer>();
		ArrayList<EpisodeDTO> episodesData = new ArrayList<EpisodeDTO>();
        for(String episodeName: episodesList) {
            file = Gdx.files.internal("data/episodes/" + episodeName + "/" + episodeName + ".json");
            levelJson = file.readString();
            json = new Json();
            EpisodeDTO episodeDto = json.fromJson(EpisodeDTO.class, levelJson);
            episodesData.add(episodeDto);
            // Guardamos el posicion del episodio en el numero de episodio, para poder buscar por numero
			episodesByNumber.insert(episodeDto.getEpisodeNumber(), episodesData.size() - 1);
        }
		// Lo grabamos en las clases globales.
		GlobalGeneralData.getInstance().setEpisodesByNumber(episodesByNumber);
		GlobalGeneralData.getInstance().setEpisodesData(episodesData);
		// Todo: Opcionalmente, faltaria la consulta al firebase para episodios activables desde alli
	}

    private I18NBundle createGlobalBundleGameData() {
        FileHandle bundleFileHandle = Gdx.files.internal("i18n/globalbundle");
        return I18NBundle.createBundle(bundleFileHandle);
    }

	private I18NBundle createTutorialBundleData() {
		FileHandle bundleFileHandle = Gdx.files.internal("i18n/tutorialbundle");
		return I18NBundle.createBundle(bundleFileHandle);
	}


    public void goToScreen(SCREEN_TYPE screen){

		if(actualScreen != null) actualScreen.dispose();
        if(dynamicAssets) Assets.unloadScreenAssets();
		switch(screen)
		{
			case LOGO_SCREEN:
                if(dynamicAssets) Assets.loadLogoAssets();
			    actualScreen = new LogoScreen(this);
                setScreen(actualScreen);
			    break;
			case MAIN_SCREEN:
                if(dynamicAssets) Assets.loadMainAssets();
				actualScreen = new MainScreen(this);
				setScreen(actualScreen);
				break;

            case SELECTION_SCREEN:
                if(dynamicAssets) Assets.loadSelectionAssets();
                actualScreen = new SelectionScreen(this);
                setScreen(actualScreen);
                break;

			case GAME_SCREEN:
                if(dynamicAssets) Assets.loadLevelAssets();
			    if(Gdx.files.absolute(launchLevel).exists()) {
			        actualScreen = new GameController(this, new TestLevelModelProvider(launchLevel));
                } else {
					if(!GlobalGeneralData.getInstance().debug_admobdisabled) {
						adServices.pokeInterstitial();
						if(adServices.mustShowInterstitial()) {
			    			adServices.loadInterstitialAd();
						}
					}
				    actualScreen = new GameController(this);
                }
				setScreen(actualScreen);
				break;
		}
	}

	public boolean isDebugBuild() {
		return debugBuild;
	}

	@Override
	public void gsOnSessionActive() {
		Gdx.app.log("RobotWarehouseGame", "Session is active now");
	}

	@Override
	public void gsOnSessionInactive() {
		Gdx.app.log("RobotWarehouseGame", "Session is inactive now");

	}

	@Override
	public void gsShowErrorToUser(GsErrorType gsErrorType, String s, Throwable throwable) {
		Gdx.app.log("RobotWarehouseGame","Error:" + s );
		platformServices.showLoginError();
	}

	public String getLeaderBoardId() {
		return leaderBoardId;
	}

	public enum SCREEN_TYPE{
		LOGO_SCREEN, MAIN_SCREEN, SELECTION_SCREEN, GAME_SCREEN
	}

	@Override
	public void render () {
		// (!) Sobreescribimos el delta si es demasiado alto para evitar la acumulacion en los arrastres de ventana
		//     Esto no da el delta correcto pero si suficientemente bajo para limitar la mayoria de errores.

		//Gdx.app.log("Delta", Float.toString(Gdx.graphics.getDeltaTime()));
		if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
			if (screen != null)
				screen.render(Math.min(Gdx.graphics.getDeltaTime(), 0.1f)); // TODO: 15/02/2017
		}else{
			super.render();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.dispose();
	}

	@Override
	public void pause () {
		//Gdx.app.log("Delta", "Pausado");
		if (screen != null) screen.pause();
		playServices.pauseSession();
	}

	@Override
	public void resume() {
		super.resume();
		playServices.resumeSession();
	}

	public void adClosed() {
    	if(actualScreen instanceof GameController) {
			GameController gameController = (GameController) actualScreen;
			gameController.getEventsManager().addSelectionScreenAfterAdEvent();
		} else {
			Gdx.app.log("RobotWarehouseGame", "Cannot recover from closing Ad");
		}
    	adServices.loadInterstitialAd();
	}

    public PlatformServices getPlatformServices(){
	    return platformServices;
    }
}