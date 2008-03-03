package gralej.blocks;

import gralej.prefs.GPrefsChangeListener;
import gralej.prefs.GralePreferences;
import java.util.Map;
import java.util.TreeMap;

public class LayoutFactory {
    
    public LayoutFactory() {}
    
    static LayoutFactory _instance;
    
    public static LayoutFactory getInstance() {
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
    private Map<String, BlockLayout> _layoutCache = new TreeMap<String, BlockLayout>();

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
        abstract BlockLayout newInstance();
    }

    final static LayoutCreator HLC = new LayoutCreator() {
        BlockLayout newInstance() {
            return new HorizontalBlockLayout();
        }
    };

    final static LayoutCreator VLC = new LayoutCreator() {
        BlockLayout newInstance() {
            return new VerticalBlockLayout();
        }
    };

    final static LayoutCreator VCLC = new LayoutCreator() {
        BlockLayout newInstance() {
            return new VerticalCenteredBlockLayout();
        }
    };

    private BlockLayout getLayout(String id, LayoutCreator creator) {
        LayoutParams params = getLayoutParams("layout." + id);
        String cacheId = makeCacheId("v!" + id, params.lead, params.intra,
                params.trail);
        BlockLayout layout;
        synchronized (_layoutCache) {
            layout = _layoutCache.get(cacheId);
            if (layout == null) {
                BlockLayout alayout = creator.newInstance();
                alayout.setName(id);
                alayout.setAll(params.lead, params.intra, params.trail);
                layout = alayout;
                _layoutCache.put(cacheId, layout);
            }
        }
        return layout;
    }

    public BlockLayout getAVMLayout() {
        return getLayout("avm", VLC);
    }

    public BlockLayout getAVPairListLayout() {
        return getLayout("avpairlist", VLC);
    }

    public BlockLayout getAVPairLayout() {
        return getLayout("avpair", HLC);
    }

    public BlockLayout getListLayout() {
        return getLayout("list", HLC);
    }

    public BlockLayout getListContentLayout() {
        return getLayout("listcontent", HLC);
    }

    public BlockLayout getReentrancyLayout() {
        return getLayout("reentrancy", HLC);
    }

    public BlockLayout getNodeLayout() {
        return getLayout("node", VCLC);
    }
}
