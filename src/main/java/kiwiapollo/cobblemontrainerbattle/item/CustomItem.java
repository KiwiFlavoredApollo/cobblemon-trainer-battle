package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.block.CustomBlock;
import kiwiapollo.cobblemontrainerbattle.item.misc.DeprecatedItem;
import kiwiapollo.cobblemontrainerbattle.item.misc.MiscItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.BdspTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.InclementEmeraldTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.RadicalRedTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.ticket.XyTicketItem;
import kiwiapollo.cobblemontrainerbattle.item.token.BdspTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.token.InclementEmeraldTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.token.RadicalRedTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.token.XyTokenItem;
import kiwiapollo.cobblemontrainerbattle.item.vsseeker.VsSeekerItem;

public class CustomItem {
    public static void initialize() {
        DeprecatedItem.initialize();
        CustomBlock.initialize();
        MiscItem.initialize();
        VsSeekerItem.initialize();
        InclementEmeraldTicketItem.initialize();
        InclementEmeraldTokenItem.initialize();
        RadicalRedTicketItem.initialize();
        RadicalRedTokenItem.initialize();
        XyTicketItem.initialize();
        XyTokenItem.initialize();
        BdspTicketItem.initialize();
        BdspTokenItem.initialize();
    }
}
