package com.promptnet.mobiledev.mapkit;

import java.io.File;
import java.io.IOException;

import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ZoomControls;

import com.nutiteq.MapView;
import com.nutiteq.components.Components;
import com.nutiteq.components.MapPos;
import com.nutiteq.components.Options;
import com.nutiteq.components.Range;
import com.nutiteq.datasources.raster.MBTilesRasterDataSource;
import com.nutiteq.datasources.raster.MapsforgeRasterDataSource;
import com.nutiteq.geometry.Marker;
import com.nutiteq.log.Log;
import com.nutiteq.projections.EPSG3857;
import com.nutiteq.rasterlayers.RasterLayer;
import com.nutiteq.style.MarkerStyle;
import com.nutiteq.ui.DefaultLabel;
import com.nutiteq.ui.Label;
import com.nutiteq.utils.UnscaledBitmapLoader;
import com.nutiteq.vectorlayers.MarkerLayer;

/**
 * Address search / Geocoding sample.
 * 
 * Sample uses Android searchable interface, which is linked to Activity via AndroidManifest file.
 * 
 * Classes:
 * 1. geocode.MapQuestGeocoder.java Geocoder implementation, uses MapQuest Open API REST API Map
 * 
 * 2. mapquest.SearchQueryResults.java - ListView which initiates real search, and shows results as ListView
 * 
 * 3. mapquest.SearchRecentSuggestionsProvider.java - stores last search terms to memory
 * 
 * 4. AddressSearchActivity.java opens Android default search UI. Search result comes from resuming 
 *      from search results activity, this is shown on map, and map is re-centered to found result.
 * 
 * 5. Resources: values/strings.xml, layout/search_query_results.xml and layout/searchrow.xml define ListView.
 *      xml/searchable.xml - needed for Android searchable interface
 *
 * Used layer(s):
 *  RasterLayer with TMS tile source for base map
 *        
 *
 *
 */
public class AddressSearch extends Activity {

    private static Marker searchResult;
    private MapView mapView;
    private MarkerLayer searchMarkerLayer;
   
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mapkit_mapview);

        Log.enableAll();
        Log.setTag("addresssearch");

        // 1. Get the MapView from the Layout xml - mandatory
        mapView = (MapView) findViewById(R.id.mapView);

        // Optional, but very useful: restore map state during device rotation,
        // it is saved in onRetainNonConfigurationInstance() below
        Components retainObject = (Components) getLastNonConfigurationInstance();
        if (retainObject != null) {
            // just restore configuration, skip other initializations
            mapView.setComponents(retainObject);
            return;
        } else {
            // 2. create and set MapView components - mandatory
            Components components = new Components();
            mapView.setComponents(components);
        }

        // 3. Define map layer for basemap - mandatory.
        // Here we use MapQuest open tiles
        // Almost all online tiled maps use EPSG3857 projection.
        // Define base layer. Almost all online maps use EPSG3857 projection.
        // Use Offline Mapsforge Base Layer Map.
        
        //use built-in render theme
        
        XmlRenderTheme renderTheme = InternalRenderTheme.OSMARENDER;
        MapDatabase mapDatabase = new MapDatabase();
        mapDatabase.closeFile();
                
        String mapFilePath = Environment.getExternalStorageDirectory().getPath()+ "/newyork.map"; 
        
        File mapFile =  new File(Environment.getExternalStorageDirectory(), "/maps/newyork.map");
        
        FileOpenResult fileOpenResult = mapDatabase.openFile(mapFile);
        
        if (fileOpenResult.isSuccess()) {
        	Log.debug("MapsforgeRasterDataSource: MapDatabase opened ok: " + mapFilePath);
        	
        }
        
        MapsforgeRasterDataSource dataSource = new MapsforgeRasterDataSource(new EPSG3857(), 0, 20, mapFile, mapDatabase, renderTheme, this.getApplication());
        RasterLayer mapLayer = new RasterLayer(dataSource, 1044);
        mapView.getLayers().setBaseLayer(mapLayer);
        
        adjustMapDpi();
        

 //Add MBTiles Layer to basemap
        
        String mbtileFile = Environment.getExternalStorageDirectory().getPath()+ "/layers.mbtiles"; 
        File mbFile = new File(Environment.getExternalStorageDirectory(), "/layers/layers.mbtiles");
               
        try {
        	MBTilesRasterDataSource mbtileSource = new MBTilesRasterDataSource (new EPSG3857(), 0, 20, mbtileFile, false, this.getApplicationContext());
        	RasterLayer mbLayer = new RasterLayer(mbtileSource, mbFile.hashCode());
        	
        	//Set mbtile layer zoom constraint from zoom level 14 to level 20
        	mbLayer.setVisibleZoomRange(new Range(14, 20));
        	mapView.getLayers().addLayer(mbLayer);
        	
        } catch (IOException e) {
            // means usually that given .mbtiles file is not found or cannot be opened as sqlite database
            Log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
     // Location: Scarsdale
        mapView.setFocusPoint(mapView.getLayers().getBaseLayer().getProjection().fromWgs84(-73.7635316f, 40.9690798f));

        // rotation - 0 = north-up
        mapView.setMapRotation(0f);
        // zoom - 0 = world, like on most web maps
        mapView.setZoom(13.0f);
        // tilt means perspective view. Default is 90 degrees for "normal" 2D map view, minimum allowed is 30 degrees.
        mapView.setTilt(65.0f);

        // Activate some mapview options to make it smoother - optional
        mapView.getOptions().setPreloading(true);
        mapView.getOptions().setSeamlessHorizontalPan(true);
        mapView.getOptions().setTileFading(true);
        mapView.getOptions().setKineticPanning(true);
        mapView.getOptions().setDoubleClickZoomIn(true);
        mapView.getOptions().setDualClickZoomOut(true);

        // set sky bitmap - optional, default - white
        mapView.getOptions().setSkyDrawMode(Options.DRAW_BITMAP);
        mapView.getOptions().setSkyOffset(4.86f);
        mapView.getOptions().setSkyBitmap(
                UnscaledBitmapLoader.decodeResource(getResources(),
                        R.drawable.sky_small));

        // Map background, visible if no map tiles loaded - optional, default - white
        mapView.getOptions().setBackgroundPlaneDrawMode(Options.DRAW_BITMAP);
        mapView.getOptions().setBackgroundPlaneBitmap(
                UnscaledBitmapLoader.decodeResource(getResources(),
                        R.drawable.background_plane));
        mapView.getOptions().setClearColor(Color.WHITE);

        // configure texture caching - optional, suggested
        mapView.getOptions().setTextureMemoryCacheSize(20 * 1024 * 1024);
        mapView.getOptions().setCompressedMemoryCacheSize(8 * 1024 * 1024);

        // define online map persistent caching - optional, suggested. Default - no caching
        mapView.getOptions().setPersistentCachePath(this.getDatabasePath("mapcache").getPath());
        // set persistent raster cache limit to 100MB
        mapView.getOptions().setPersistentCacheSize(100 * 1024 * 1024);
        
        // Add simple marker to map. 
        // define marker style (image, size, color)
        
        Bitmap SCApointMarker = UnscaledBitmapLoader.decodeResource(getResources(), R.drawable.olmarker);
        MarkerStyle SCAmarkerStyle = MarkerStyle.builder().setBitmap(SCApointMarker).setSize(0.5f).setColor(Color.WHITE).build();
        
     // define label what is shown when you click on marker
        Label SCAmarkerLabel = new DefaultLabel("Village of Scarsdale", "Village Hall");
        
     // define location of the marker, it must be converted to base map coordinate system
        MapPos markerLocation = mapLayer.getProjection().fromWgs84(-73.7967994f, 40.9884312f);
        
     // create layer and add object to the layer, finally add layer to the map. 
     // All overlay layers must be same projection as base layer, so we reuse it
        
        MarkerLayer SCAmarkerLayer = new MarkerLayer(mapLayer.getProjection());
        
        // Add SCAmarker Layer zoom constraint from zoom level 14 to 20
        SCAmarkerLayer.setVisibleZoomRange(new Range(14, 20));
        
        SCAmarkerLayer.add(new Marker(markerLocation, SCAmarkerLabel, SCAmarkerStyle, SCAmarkerLayer));
        mapView.getLayers().addLayer(SCAmarkerLayer);
        
        // Increase RasterTaskPoolSize values for multi-threading and to make user experience more smooth and improve performance.
        // The surrounding tiles are pre-fetched and loaded.
        // But it does put some work on the processor. So use according to requirement. Normally any value between 4 to 8 are good.
        
        mapView.getOptions().setRasterTaskPoolSize(4);
        
        
        // 4. zoom buttons using Android widgets - optional
        // get the zoomcontrols that was defined in main.xml
        ZoomControls zoomControls = (ZoomControls) findViewById(R.id.zoomcontrols);
        // set zoomcontrols listeners to enable zooming
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mapView.zoomIn();
            }
        });
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mapView.zoomOut();
            }
        });
        
     // create layer for search result 
        searchMarkerLayer = new MarkerLayer(mapView.getLayers().getBaseLayer().getProjection());
//      searchResult = new Marker(new MapPos(0,0), null, (MarkerStyle) null, null);
//      searchResult.setVisible(false);
        mapView.getLayers().addLayer(searchMarkerLayer);

        // open search right away
        // search class is defined in AndroidManifest.xml as android.intent.action.SEARCH
        onSearchRequested();
    }
      

    @Override
    protected void onStart() {
        mapView.startMapping();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.stopMapping();
        
    }
    
    @Override 
    protected void onResume() {

        super.onResume();
        Log.debug("onResume");

        if (searchResult != null && searchResult.getMapPos().x != 0) {
            // recenter to searchResult
            Log.debug("Add search result and recenter to it: ");
            if (searchResult.getLayer() == null) {
                searchMarkerLayer.add(searchResult);
            }
            mapView.setFocusPoint(searchResult.getMapPos());
            //searchResult.setVisible(true);
            mapView.selectVectorElement(searchResult);
        }
    }
    
    public static void setSearchResult(Marker marker) {
        Log.debug("Search result selected: " + marker.getMapPos());
        searchResult = marker;
    }

    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
        
    // adjust zooming to DPI, so texts on rasters will be not too small
    // useful for non-retina rasters, they would look like "digitally zoomed"

    private void adjustMapDpi() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float dpi = metrics.densityDpi;
        // following is equal to  -log2(dpi / DEFAULT_DPI)
        float adjustment = (float) - (Math.log(dpi / DisplayMetrics.DENSITY_HIGH) / Math.log(2));
        Log.debug("adjust DPI = "+dpi+" as zoom adjustment = "+adjustment);
        mapView.getOptions().setTileZoomLevelBias(adjustment / 2.0f);
    }
    
    public MapView getMapView() {
        return mapView;
    }

}

