package dragonclient.module;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import dragonclient.Dragon;
import dragonclient.module.impl.combat.AntiFireball;
import dragonclient.module.impl.combat.AttackCrash;
import dragonclient.module.impl.combat.AutoTotem;
import dragonclient.module.impl.combat.Autoclicker;
import dragonclient.module.impl.combat.ComboOneHit;
import dragonclient.module.impl.combat.Criticals;
import dragonclient.module.impl.combat.FastBow;
import dragonclient.module.impl.combat.KnockbackDisplacer;
import dragonclient.module.impl.combat.NoClickDelay;
import dragonclient.module.impl.combat.Regen;
import dragonclient.module.impl.combat.Velocity;
import dragonclient.module.impl.combat.WTap;
import dragonclient.module.impl.hud.Compass;
import dragonclient.module.impl.hud.ConfigScreen;
import dragonclient.module.impl.hud.Fps;
import dragonclient.module.impl.hud.Keystrokes;
import dragonclient.module.impl.hud.Radar;
import dragonclient.module.impl.misc.Anticheat;
import dragonclient.module.impl.misc.ClientSpoofer;
import dragonclient.module.impl.misc.Debugger;
import dragonclient.module.impl.misc.KillMessage;
import dragonclient.module.impl.misc.XCarry;
import dragonclient.module.impl.movement.InvMove;
import dragonclient.module.impl.movement.No003;
import dragonclient.module.impl.movement.Sprint;
import dragonclient.module.impl.player.Blink;
import dragonclient.module.impl.player.NoFall;
import dragonclient.module.impl.render.ChinaHat;
import dragonclient.module.impl.render.ESP;
import dragonclient.module.impl.render.Fullbright;
import dragonclient.module.impl.render.HUD;
import dragonclient.module.impl.render.NameTags;
import dragonclient.module.impl.render.Penis;
import dragonclient.module.impl.render.SkeletonEsp;
import dragonclient.module.impl.render.Tracers;
import dragonclient.module.impl.render.Trails;
import dragonclient.module.impl.render.XRay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;


public class ModuleManager {

    public final TreeSet<Module> modules = new TreeSet<>(
            (module1, module2) -> module1.getName().compareTo(module2.getName()));
    private final HashMap<Class<?>, Module> moduleClassMap = new HashMap<>();

    public boolean shouldNotify = false;
    public int toggleSoundMode = 0;
    public float toggleVolume = 0F;
    public float popSoundPower = 90F;
    public float swingSoundPower = 75F;

    public ModuleManager() {

    }

    /**
     * Register all modules
     */
    public void registerModules() {
        registerModule(new ClientSpoofer());
        registerModule(new HUD());
        registerModule(new Fullbright());
        registerModule(new Criticals());
        registerModule(new Autoclicker());
        registerModule(new ESP());
        registerModule(new XCarry());
        registerModule(new Tracers());
        registerModule(new KillMessage());
        registerModule(new AntiFireball());
        registerModule(new InvMove());
        registerModule(new NoClickDelay());
        registerModule(new Trails());
        registerModule(new No003());
        registerModule(new XRay());
        registerModule(new Blink());
        registerModule(new Anticheat());
        registerModule(new Debugger());
        registerModule(new Sprint());
        registerModule(new ConfigScreen());
        registerModule(new Fps());
        registerModule(new Compass());
        registerModule(new Keystrokes());
        registerModule(new AttackCrash());
        registerModule(new WTap());
        registerModule(new Regen());
        registerModule(new ComboOneHit());
        registerModule(new ChinaHat());
        registerModule(new AutoTotem());
        registerModule(new Velocity());
        registerModule(new NoFall());
        registerModule(new FastBow());
        registerModule(new NameTags());
        registerModule(new KnockbackDisplacer());
        //registerModule(new Penis());
        registerModule(new SkeletonEsp());
    }

    /**
     * Register a module
     */
    public void registerModule(Module module) {
        modules.add(module);
        moduleClassMap.put(module.getClass(), module);

        module.onInitialize();
    }

    public Module getModuleByName(String name) {
        return modules.stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Unregister a module
     */
    public void unregisterModule(Module module) {
        modules.remove(module);
        moduleClassMap.remove(module.getClass());
    }

    public <T extends Module> T getModule(Class<T> moduleClass) {
        return moduleClass.cast(moduleClassMap.get(moduleClass));
    }

    public <T extends Module> T get(Class<T> clazz) {
        return getModule(clazz);
    }

    /**
     * Get module by name
     */
    public Module getModule(String moduleName) {
        return modules.stream()
                .filter(module -> module.getName().equalsIgnoreCase(moduleName))
                .findFirst()
                .orElse(null);
    }

    public TreeSet<Module> getModules() {
        return modules;
    }

    public List<Module> getEnabledModules() {
        return modules.stream()
                .filter(Module::isEnabled)
                .collect(Collectors.toList());
    }

        public void addChatMessage(String message) {
		message = Dragon.CLIENT_NAME_CHAT + message;
		
		Minecraft.getMinecraft().player.addChatMessage(new TextComponentString(message));
	}
}
