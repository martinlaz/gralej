/*
 *  $Id$
 *
 *  Author:
 *     Martin Lazarov [mlazarov at sfs.uni-tuebingen.de]
 *     
 *  This file is part of the Gralej system
 *     http://code.google.com/p/gralej/
 *
 *  Gralej is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gralej is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package gralej.blocks;

import gralej.Config;
import java.util.Map;
import java.util.TreeMap;

public class LayoutFactory {
    private Config _cfg;
    
    private Map<String, BlockLayout> _layoutCache = new TreeMap<String, BlockLayout>();
    private static LayoutFactory _instance;
    
    public LayoutFactory() {
        this(Config.currentConfig());
    }
    public LayoutFactory(Config cfg) {
        _cfg = cfg;
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
    
    void updateConfig() {
        updateConfig(_cfg);
    }
    
    void updateConfig(Config cfg) {
        for (BlockLayout x : _layoutCache.values()) {
            cfg.set("block.layout." + x.getName() + ".space.leading",  x.getLeadingSpace());
            cfg.set("block.layout." + x.getName() + ".space.intra",    x.getIntraSpace());
            cfg.set("block.layout." + x.getName() + ".space.trailing", x.getTrailingSpace());
        }
    }
    
    void updateSelf() {
        for (BlockLayout x : _layoutCache.values()) {
            x.setAll(
                    _cfg.getInt("block.layout." + x.getName() + ".space.leading"),
                    _cfg.getInt("block.layout." + x.getName() + ".space.intra"),
                    _cfg.getInt("block.layout." + x.getName() + ".space.trailing")
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
            int leading     = _cfg.getInt(id_ + ".space.leading");
            int intra       = _cfg.getInt(id_ + ".space.intra");
            int trailing    = _cfg.getInt(id_ + ".space.trailing");
            
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

    public BlockLayout getLRSNodeLayout() {
        return getLayout("lrsnode", HLC);
    }

    public BlockLayout getLRSBlockLayout() {
        return getLayout("lrsblock", VCLC);
    }
}
