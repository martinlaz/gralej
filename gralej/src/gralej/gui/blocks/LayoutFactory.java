package gralej.gui.blocks;

import java.util.Map;
import java.util.TreeMap;

final class LayoutFactory {

    static class LayoutParams {
        int lead, intra, trail;

        LayoutParams(int lead, int intra, int trail) {
            this.lead = lead;
            this.intra = intra;
            this.trail = trail;
        }
    }

    private static Map<String, LayoutParams> _layoutParamMap = new TreeMap<String, LayoutParams>();
    private static Map<String, ILayout> _layoutCache = new TreeMap<String, ILayout>();

    synchronized private static LayoutParams getLayoutParams(String id) {
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

    private static ILayout getLayout(String id, LayoutCreator creator) {
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

    static ILayout getAVMLayout() {
        return getLayout("avm", VLC);
    }

    static ILayout getAVPairListLayout() {
        return getLayout("avpairlist", VLC);
    }

    static ILayout getAVPairLayout() {
        return getLayout("avpair", HLC);
    }

    static ILayout getListLayout() {
        return getLayout("list", HLC);
    }

    static ILayout getListContentLayout() {
        return getLayout("listcontent", HLC);
    }

    static ILayout getReentrancyLayout() {
        return getLayout("reentrancy", HLC);
    }

    static ILayout getNodeLayout() {
        return getLayout("node", VCLC);
    }
}
