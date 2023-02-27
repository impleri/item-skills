package net.impleri.itemskills.client;

import net.impleri.itemskills.api.Restrictions;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.itemskills.restrictions.Restriction;
import net.impleri.playerskills.api.RestrictionsApi;
import net.impleri.playerskills.client.RestrictionsClient;
import net.minecraft.world.item.Item;

import java.util.List;

public class ClientApi extends RestrictionsClient<Item, Restriction> {
    public static final ClientApi INSTANCE = new ClientApi(Registry.INSTANCE, Restrictions.INSTANCE);

    private ClientApi(net.impleri.playerskills.restrictions.Registry<Restriction> registry, RestrictionsApi<Item, Restriction> serverApi) {
        super(registry, serverApi);
    }

    private List<Item> pluckTarget(List<Restriction> list) {
        return list.stream().map(r -> r.target).toList();
    }

    public List<Item> getHidden() {
        return pluckTarget(getFiltered(r -> !r.producible && !r.consumable));
    }

    public List<Item> getUnproducible() {
        return pluckTarget(getFiltered(r -> !r.producible));
    }

    public List<Item> getUnconsumable() {
        return pluckTarget(getFiltered(r -> !r.consumable));
    }

    public boolean isProducible(Item item) {
        return canPlayer(item, "producible");
    }

    public boolean isConsumable(Item item) {
        return canPlayer(item, "consumable");
    }

    public boolean isHoldable(Item item) {
        return canPlayer(item, "holdable");
    }

    public boolean isIdentifiable(Item item) {
        return canPlayer(item, "identifiable");
    }

    public boolean isHarmful(Item item) {
        return canPlayer(item, "harmful");
    }

    public boolean isWearable(Item item) {
        return canPlayer(item, "wearable");
    }

    public boolean isUsable(Item item) {
        return canPlayer(item, "usable");
    }

}
