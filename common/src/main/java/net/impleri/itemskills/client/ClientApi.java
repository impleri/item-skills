package net.impleri.itemskills.client;

import net.impleri.itemskills.api.Restrictions;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.itemskills.restrictions.Restriction;
import net.impleri.playerskills.api.RestrictionsApi;
import net.impleri.playerskills.client.RestrictionsClient;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ClientApi extends RestrictionsClient<ResourceLocation, Restriction> {
    public static final ClientApi INSTANCE = new ClientApi(Registry.INSTANCE, Restrictions.INSTANCE);

    private ClientApi(net.impleri.playerskills.restrictions.Registry<Restriction> registry, RestrictionsApi<ResourceLocation, Restriction> serverApi) {
        super(registry, serverApi);
    }

    private List<ResourceLocation> pluckTarget(List<Restriction> list) {
        return list.stream().map(r -> r.target).toList();
    }

    public List<ResourceLocation> getHidden() {
        return pluckTarget(getFiltered(r -> !r.producible && !r.consumable));
    }

    public List<ResourceLocation> getUnproducible() {
        return pluckTarget(getFiltered(r -> !r.producible));
    }

    public List<ResourceLocation> getUnconsumable() {
        return pluckTarget(getFiltered(r -> !r.consumable));
    }

    public boolean isProducible(ResourceLocation item) {
        return canPlayer(item, "producible");
    }

    public boolean isConsumable(ResourceLocation item) {
        return canPlayer(item, "consumable");
    }

    public boolean isHoldable(ResourceLocation item) {
        return canPlayer(item, "holdable");
    }

    public boolean isIdentifiable(ResourceLocation item) {
        return canPlayer(item, "identifiable");
    }

    public boolean isHarmful(ResourceLocation item) {
        return canPlayer(item, "harmful");
    }

    public boolean isWearable(ResourceLocation item) {
        return canPlayer(item, "wearable");
    }

    public boolean isUsable(ResourceLocation item) {
        return canPlayer(item, "usable");
    }

}
