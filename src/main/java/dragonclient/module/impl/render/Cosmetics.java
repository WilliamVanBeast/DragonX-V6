package dragonclient.module.impl.render;

import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.ListSetting;

public class Cosmetics extends Module{
    public ListSetting who = new ListSetting("Who", new String[] {"Only you", "Everyone", "Everyone Else"}, "Only you");
    public BooleanSetting show = new BooleanSetting("Show cosmetics", true);
    public BooleanSetting tophat = new BooleanSetting("TopHat", false);
    public BooleanSetting blaze = new BooleanSetting("Blaze", false);
    public BooleanSetting crystal = new BooleanSetting("Crystal Wings", false);
    public BooleanSetting halo = new BooleanSetting("Halo", false);
    public BooleanSetting glasses = new BooleanSetting("Glasses", false);
    public BooleanSetting dogPet = new BooleanSetting("Pet Dog", false);


    public Cosmetics() {
        super("Cosmetics", Category.RENDER);
        addSettings(who, tophat, blaze, crystal, halo, glasses, dogPet, show);
    }
}
