package gralej.blocks;

import gralej.Config;
import java.util.Map;
import java.util.TreeMap;

public class LayoutFactory {
    
    private Map<String, BlockLayout> _layoutCache = new TreeMap<String, BlockLayout>();
    private static LayoutFactory _instance;
    
    public LayoutFactory() {
        init();
    }
    
    public static LayoutFactory getInstance() {
        if (_instance == null)
            _instance = new LayoutFactory();
        return _instance;
    }
    
    private void init() {
        getAVMLayout();
        getAVPairLayout();
        getAVPairListLayout();
        getListLayout();
        getListContentLayout();
        getReentrancyLayout();
        getNodeLayout();
    }
    
    BlockLayout getLayout(String type) {
        return _layoutCache.get("block.layout." + type);
    }
    
    void updateConfig(Config cfg) {
        for (BlockLayout x : _layoutCache.values()) {
            cfg.put("block.layout." + x.getName() + ".space.leading",  x.getLeadingSpace());
            cfg.put("block.layout." + x.getName() + ".space.intra",    x.getIntraSpace());
            cfg.put("block.layout." + x.getName() + ".space.trailing", x.getTrailingSpace());
        }
    }
    
    void updateSelf() {
        for (BlockLayout x : _layoutCache.values()) {
            x.setAll(
                    Config.i("block.layout." + x.getName() + ".space.leading"),
                    Config.i("block.layout." + x.getName() + ".space.intra"),
                    Config.i("block.layout." + x.getName() + ".space.trailing")
                    );
        }
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
        String id_ = "block.layout." + id;
        BlockLayout layout = _layoutCache.get(id_);
        if (layout == null) {
            int leading     = Config.i(id_ + ".space.leading");
            int intra       = Config.i(id_ + ".space.intra");
            int trailing    = Config.i(id_ + ".space.trailing");
            
            BlockLayout alayout = creator.newInstance();
            alayout.setName(id);
            alayout.setAll(leading, intra, trailing);
            layout = alayout;
            _layoutCache.put(id_, layout);
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
