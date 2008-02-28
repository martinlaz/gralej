package gralej.gui.blocks;

import gralej.prefs.GPrefsChangeListener;
import gralej.prefs.GralePreferences;
import java.util.Map;
import java.util.TreeMap;

final class LayoutFactory {
    
    private LayoutFactory() {}
    
    static LayoutFactory _instance;
    
    static LayoutFactory getInstance() {
        if (_instance == null)
            _instance = new LayoutFactory();
        return _instance;
    }
    
    static {
        GPrefsChangeListener l = new GPrefsChangeListener() {
            public void preferencesChange() {
                _instance = null;
            }
        };
        GralePreferences.getInstance().addListener(l, "layout.");
    }

    static class LayoutParams {
        int lead, intra, trail;

        LayoutParams(int lead, int intra, int trail) {
            this.lead = lead;
            this.intra = intra;
            this.trail = trail;
        }
    }

    private Map<String, LayoutParams> _layoutParamMap = new TreeMap<String, LayoutParams>();
    private Map<String, ILayout> _layoutCache = new TreeMap<String, ILayout>();

    synchronized private LayoutParams getLayoutParams(String id) {
        LayoutParams params = _layoutParamMap.get(id);
        if (params == null) {
            params = new LayoutParams(Config.getInt(id + ".space.leading"),
                    Config.getInt(id + ".space.intra"), Config.getInt(id
                            + ".space.trailing"));
            _layoutParamMap.put(id, params);
        }
        return params;
    }

    private static String makeCacheId(String s) {
        return s;
    }

    private static String makeCacheId(String s, int i, int j, int k) {
        return makeCacheId(s) + ":" + i + ":" + j + ":" + k;
    }

    static abstract class LayoutCreator {
        abstract AbstractLayout newInstance();
    }

    final static LayoutCreator HLC = new LayoutCreator() {
        AbstractLayout newInstance() {
            return new HorizontalLayout();
        }
    };

    final static LayoutCreator VLC = new LayoutCreator() {
        AbstractLayout newInstance() {
            return new VerticalLayout();
        }
    };

    final static LayoutCreator VCLC = new LayoutCreator() {
        AbstractLayout newInstance() {
            return new VerticalCenteredLayout();
        }
    };

    private ILayout getLayout(String id, LayoutCreator creator) {
        LayoutParams params = getLayoutParams("layout." + id);
        String cacheId = makeCacheId("v!" + id, params.lead, params.intra,
                params.trail);
        ILayout layout;
        synchronized (_layoutCache) {
            layout = _layoutCache.get(cacheId);
            if (layout == null) {
                AbstractLayout alayout = creator.newInstance();
                alayout.setAll(params.lead, params.intra, params.trail);
                layout = alayout;
                _layoutCache.put(cacheId, layout);
            }
        }
        return layout;
    }

    ILayout getAVMLayout() {
        return getLayout("avm", VLC);
    }

    ILayout getAVPairListLayout() {
        return getLayout("avpairlist", VLC);
    }

    ILayout getAVPairLayout() {
        return getLayout("avpair", HLC);
    }

    ILayout getListLayout() {
        return getLayout("list", HLC);
    }

    ILayout getListContentLayout() {
        return getLayout("listcontent", HLC);
    }

    ILayout getReentrancyLayout() {
        return getLayout("reentrancy", HLC);
    }

    ILayout getNodeLayout() {
        return getLayout("node", VCLC);
    }
}
