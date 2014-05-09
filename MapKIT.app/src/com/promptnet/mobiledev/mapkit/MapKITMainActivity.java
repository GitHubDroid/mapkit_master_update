package com.promptnet.mobiledev.mapkit;

import java.io.File;

import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import com.nutiteq.MapView;
import com.nutiteq.components.Color;
import com.nutiteq.components.Components;
import com.nutiteq.components.MapPos;
import com.nutiteq.components.Options;
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

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ZoomControls;

public class MapKITMainActivity extends Activity {
	
	private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     // spinner in status bar, for progress indication
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
        
        setContentView(R.layout.main);
        
        // enable logging for troubleshooting - optional
        Log.enableAll();
        Log.setTag("mapkit");

        //  Get the MapView from the Layout xml - mandatory
        mapView = (MapView) findViewById(R.id.mapView);
        

            // create and set MapView components - mandatory
            Components components = new Components();
            // set stereo view: works if you rotate to landscape and device has HTC 3D or LG Real3D
            mapView.setComponents(components);
        
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
        
     
        
        // Activate some mapview options to make it smoother - optional
        
        mapView.getOptions().setPreloading(false);
        mapView.getOptions().setSeamlessHorizontalPan(true);
        mapView.getOptions().setTileFading(false);
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
        //  mapView.getOptions().setPersistentCachePath(this.getDatabasePath("mapcache").getPath());
        // set persistent raster cache limit to 100MB
        
        mapView.getOptions().setPersistentCacheSize(100 * 1024 * 1024);
        
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
        SCAmarkerLayer.add(new Marker(markerLocation, SCAmarkerLabel, SCAmarkerStyle, SCAmarkerLayer));
        mapView.getLayers().addLayer(SCAmarkerLayer);
                
        // Location: Scarsdale
        // NB! it must be in base layer projection (EPSG3857), so we convert it from lat and long
        // Test using Centre Point: 40.9690798,-73.7635316
        mapView.setFocusPoint(mapView.getLayers().getBaseLayer().getProjection().fromWgs84(-73.7635316f, 40.9690798f));
        
        // Set Initial Zoom Level at launch 
        mapView.setZoom(14.0f);
        
        // Increase RasterTaskPoolSize values for multi-threading and to make user experience more smooth and improve performance.
        // The surrounding tiles are pre-fetched and loaded.
        // But it do put some work on the processor. So use according to requirement. Normally any value between 4 to 8 are good.
        
        mapView.getOptions().setRasterTaskPoolSize(4);
        
 

    // zoom buttons using Android widgets, get the zoomcontrols that was defined in main.xml
    ZoomControls zoomControls = (ZoomControls) findViewById(R.id.zoomcontrols);
    // set zoomcontrols listeners to enable zooming
    zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
        public void onClick(final View v) {
            mapView.zoomIn();
        }
    });
    zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
        public void onClick(final View v) {
            mapView.zoomOut();
        }
    });
    
    }
      // it is suggested to start and stop mapping in Activity lifecycle methods, as following:

      @Override
      protected void onStart() {
          super.onStart();
          //Start the map - mandatory
          mapView.startMapping();
      }

      @Override
      protected void onStop() {
          //Stop the map - mandatory to avoid problems with app restart
          mapView.stopMapping();
          super.onStop();
      }
      
      
      //Handle device orientation change
      @Override
      public Object onRetainNonConfigurationInstance() {
          return this.mapView.getComponents();
      }

	
}
