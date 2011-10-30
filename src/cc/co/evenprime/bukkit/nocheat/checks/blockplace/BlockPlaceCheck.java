package cc.co.evenprime.bukkit.nocheat.checks.blockplace;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.config.Permissions;
import cc.co.evenprime.bukkit.nocheat.config.cache.ConfigurationCache;
import cc.co.evenprime.bukkit.nocheat.data.BaseData;

/**
 * 
 */
public class BlockPlaceCheck {

    private final ReachCheck     reachCheck;
    private final OnLiquidCheck  onLiquidCheck;
    private final DirectionCheck directionCheck;
    private final NoCheat        plugin;

    public BlockPlaceCheck(NoCheat plugin) {

        this.plugin = plugin;

        reachCheck = new ReachCheck(plugin);
        onLiquidCheck = new OnLiquidCheck(plugin);
        directionCheck = new DirectionCheck(plugin);
    }

    public boolean check(final Player player, final Block blockPlaced, final Block blockPlacedAgainst, final ConfigurationCache cc) {

        boolean cancel = false;

        // Which checks are going to be executed?
        final boolean onliquid = cc.blockplace.onliquidCheck && !player.hasPermission(Permissions.BLOCKPLACE_ONLIQUID);
        final boolean reach = cc.blockplace.reachCheck && !player.hasPermission(Permissions.BLOCKPLACE_REACH);
        final boolean direction = cc.blockplace.directionCheck && !player.hasPermission(Permissions.BLOCKPLACE_DIRECTION);

        final BaseData data = plugin.getData(player.getName());

        if(blockPlaced != null && blockPlacedAgainst != null) {
            data.blockplace.blockPlaced.set(blockPlaced);
            data.blockplace.blockPlacedAgainst.set(blockPlacedAgainst);
            data.blockplace.placedType = blockPlaced.getType();

            if(!cancel && direction) {
                cancel = directionCheck.check(player, data, cc);
            }

            if(!cancel && reach) {
                cancel = reachCheck.check(player, data, cc);
            }

            if(!cancel && onliquid) {
                cancel = onLiquidCheck.check(player, data, cc);
            }
        }

        return cancel;
    }
}
