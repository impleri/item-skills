package net.impleri.itemskills.client;

import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.restrictions.Restriction;
import net.impleri.itemskills.restrictions.Restrictions;
import net.impleri.playerskills.client.RestrictionsClient;
import net.minecraft.world.item.Item;

import java.util.List;

public class ClientApi extends RestrictionsClient<Item, Restriction, Restrictions> {
    public static final ClientApi INSTANCE = new ClientApi(ItemSkills.RESTRICTIONS, Restrictions.INSTANCE);

    private ClientApi(net.impleri.playerskills.restrictions.Registry<Restriction> registry, Restrictions serverApi) {
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
        return serverApi.isProducible(getPlayer(), item, null);
    }

    public boolean isConsumable(Item item) {
        return serverApi.isConsumable(getPlayer(), item, null);
    }

    public boolean isHoldable(Item item) {
        return serverApi.isHoldable(getPlayer(), item, null);
    }

    public boolean isIdentifiable(Item item) {
        return serverApi.isIdentifiable(getPlayer(), item, null);
    }

    public boolean isHarmful(Item item) {
        return serverApi.isHarmful(getPlayer(), item, null);
    }

    public boolean isWearable(Item item) {
        return serverApi.isWearable(getPlayer(), item, null);
    }

    public boolean isUsable(Item item) {
        return serverApi.isUsable(getPlayer(), item, null);
    }

}
