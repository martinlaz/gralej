package gralej.gui.icons;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * A generic icon theme that loads icons from a resource (jar file). This
 * implements the singleton pattern.
 * 
 * @author Niels Ott
 * @version $Id$
 */
public class GenericIconTheme implements IconTheme {

    private String iconpackagename;
    private HashMap<String, ImageIcon> map;
    private static GenericIconTheme instance = null;

    /**
     * Creates a generic icon theme using the directory specified. This
     * directory must be inside the package this very class is in.
     * 
     * @param iconpackagename
     */
    private GenericIconTheme(String iconpackagename) {
        this.iconpackagename = iconpackagename;
        map = new HashMap<String, ImageIcon>();
        preloadAllIcons();
        instance = this;
    }

    private void preloadAllIcons() {

        map.put("closeviews", loadIcon("grale-closeviews.png"));
        map.put("closewindow", loadIcon("grale-closewindow.png"));
        map.put("connected", loadIcon("connect_established.png"));
        map.put("deletedata", loadIcon("grale-deletedata.png"));
        map.put("disconnected", loadIcon("connect_no.png"));
        map.put("expand", loadIcon("grale-expand.png"));
        map.put("filefloppy", loadIcon("filesaveas.png"));
        map.put("fileopen", loadIcon("fileopen.png"));
        map.put("fileprint", loadIcon("fileprint.png"));
        map.put("gralelogo", loadIcon("grale-LOGO.png"));
        map.put("grale", loadIcon("grale-GRALE.png"));
        map.put("magglass", loadIcon("viewmag.png"));
        map.put("maximize", loadIcon("window_fullscreen.png"));
        map.put("nextstruc", loadIcon("forward.png"));
        map.put("prevstruc", loadIcon("back.png"));
        map.put("raisewindow", loadIcon("grale-raisewindow.png"));
        map.put("showstruc", loadIcon("grale-showstruc.png"));
        map.put("large-showstruc", loadIcon("grale-large-showstruc.png"));
        map.put("showtree", loadIcon("grale-showtree.png"));
        map.put("unexpand", loadIcon("grale-unexpand.png"));
        map.put("zoomin", loadIcon("viewmag+.png"));
        map.put("zoomout", loadIcon("viewmag-.png"));
        map.put("cancel", loadIcon("button_cancel.png"));
        map.put("ok", loadIcon("button_more.png"));
        map.put("configure", loadIcon("configure.png"));
        map.put("fileclose", loadIcon("fileclose.png"));
        map.put("stop", loadIcon("stop.png"));
        map.put("fitwindow", loadIcon("view_fit_window.png"));
        map.put("dummy.v", loadIcon("vertical-dummy.png"));
        map.put("large-l+f", loadIcon("looknfeel.png"));
        map.put("large-configure", loadIcon("large-configure.png"));
        map.put("large-advanced-settings", loadIcon("large-bomb.png"));
        

    }

    /**
     * 
     * @param name
     * @return my return null if it doesn't work out.
     */
    private ImageIcon loadIcon(String filename) {

        // construct source URL
        URL imageURL = GenericIconTheme.class.getResource(iconpackagename + "/"
                + filename);

        // try to load
        ImageIcon icon = null;
        if (imageURL != null) {
            icon = new ImageIcon(imageURL);
        }

        if (icon == null) {
            System.err.println("GenericIconTheme: Failed to load icon: "
                    + filename);
        }

        return icon;

    }

    /**
     * @see IconTheme#getIcon(String)
     */
    public ImageIcon getIcon(String name) {
        // return a clone of the corresponding icon
        // or null if it is null
        ImageIcon res = map.get(name);
        if (res != null) {
            res = new ImageIcon(res.getImage());
        }

        return res;
    }

    /**
     * @see IconTheme#getIconNames()
     */
    public List<String> getIconNames() {
        // return a clone of the map's keys
        return new LinkedList<String>(map.keySet());
    }

    /**
     * Creates one generic icon theme instance (singleton pattern) using the
     * directory specified. This directory must be inside the package this very
     * class is in.
     * 
     * @param iconpackagename
     */
    protected static IconTheme getInstance(String iconpackagename) {
        if (instance == null) {
            instance = new GenericIconTheme(iconpackagename);
        }
        return instance;
    }

}
