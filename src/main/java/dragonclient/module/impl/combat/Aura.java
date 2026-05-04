package dragonclient.module.impl.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import dragonclient.Dragon;
import dragonclient.event.Events.AttackEvent;
import dragonclient.event.Events.PacketReceiveEvent;
import dragonclient.event.Events.PostMotionEvent;
import dragonclient.event.Events.PreMotionEvent;
import dragonclient.event.Events.Render3DEvent;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.event.Events.WorldChangedEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.FloatSetting;
import dragonclient.module.settings.IntegerSetting;
import dragonclient.module.settings.ListSetting;
import dragonclient.util.PacketUtil;
import dragonclient.util.PlayerUtil;
import dragonclient.util.RandomUtil;
import dragonclient.util.RotationUtil;
import dragonclient.util.TimeUtil;
import net.lax1dude.eaglercraft.Mouse;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;


public class Aura extends Module {
    private final TimeUtil timer = new TimeUtil();

    public static EntityLivingBase target;

    public static float yaw, pitch, lastYaw, lastPitch, serverYaw, serverPitch;
    private float randomYaw, randomPitch, derpYaw;
    private float sinWaveTicks;
    private double targetPosX, targetPosY, targetPosZ;
    private Vec3d positionOnPlayer, lastPositionOnPlayer;

    private double ticks = 0;
    private final long lastFrame = 0;
    private int hitTicks, cps, targetIndex;
    public boolean blocking;
    private final List<CPacketPlayer.Position> packetList = new ArrayList<>();
    private boolean targetstrafe;

    private ListSetting mode = new ListSetting("Mode", new String[] {
        "Single", "Switch", "Multi"
    } , "Single");

    private ListSetting rotationMode = new ListSetting("Rot Mode", new String[] {
        "Custom", "Custom Simple", "Custom Advanced", "Smooth", "Sin Wave", "Up", "Down", "Derp", "None"
    } , "Custom");

    private ListSetting blockMode = new ListSetting("Block Mode", new String[] {
        "None", "Fake", "Vanilla", "Bypass", "NCP", "AAC", "Interact", "Hypixel"
    } , "None");

    private ListSetting sortingMode = new ListSetting("Sort Mode", new String[] {
        "Distance", "Health", "Hurttime"
    } , "Distance");

    // General Settings
    private FloatSetting range = new FloatSetting("Range", 3.0f, 0.0f, 6.0f);
    private FloatSetting rotationRange = new FloatSetting("Rot Range", 6f, 0f, 12f) {
        @Override
        public void changeValue(Float value) {
            if (value < range.get())
                super.changeValue(range.get());
            else
                super.changeValue(value);
        }
    };
    
    private BooleanSetting extendedRangeEnabled = new BooleanSetting("ExtRange", false);
    private FloatSetting extendedRange = new FloatSetting("Ext Range", 7f, 6f, 12f, () -> extendedRangeEnabled.get());
    private IntegerSetting minCps = new IntegerSetting("Min CPS", 8, 1, 20);
    private IntegerSetting maxCps = new IntegerSetting("Max CPS", 8, 1, 20) {
        @Override
        public void changeValue(Integer value) {
            if (value < minCps.get())
                super.changeValue(minCps.get());
            else
                super.changeValue(value);
        }
    };

    private IntegerSetting maxTargets = new IntegerSetting("Max Targets", 25, 2, 50,
            () -> mode.get().equalsIgnoreCase("Multi"));

    // Bypass Settings
    private FloatSetting predict = new FloatSetting("Predict", 0.0f, 0.0f, 4.0f);
    private FloatSetting random = new FloatSetting("Random", 0.0f, 0.0f, 18.0f);
    private FloatSetting maxRotation = new FloatSetting("Max Rot", 180.0f, 1.0f, 180.0f,
            () -> rotationMode.get().equalsIgnoreCase("Custom") || rotationMode.get().equalsIgnoreCase("Custom Simple"));
    private FloatSetting minRotation = new FloatSetting("Min Rot", 180.0f, 0.0f, 180.0f,
            () -> rotationMode.get().equalsIgnoreCase("Custom") || rotationMode.get().equalsIgnoreCase("Custom Simple")); 
    private FloatSetting maxYawRot = new FloatSetting("Max Yaw Rot", 180.0f, 1.0f, 180.0f,
            () -> rotationMode.get().equalsIgnoreCase("Custom Advanced"));
    private FloatSetting minYawRot = new FloatSetting("Min Yaw Rot", 180.0f, 0.0f, 180.0f,
            () -> rotationMode.get().equalsIgnoreCase("Custom Advanced"));
    private FloatSetting maxPitchRot = new FloatSetting("Max Pitch Rot", 180.0f, 1.0f, 180.0f,
            () -> rotationMode.get().equalsIgnoreCase("Custom Advanced"));
    private FloatSetting minPitchRot = new FloatSetting("Min Pitch Rot", 180.0f, 0.0f, 180.0f,
            () -> rotationMode.get().equalsIgnoreCase("Custom Advanced"));
    private FloatSetting sinWaveSpeed = new FloatSetting("Sin Speed", 180.0f, 1.0f, 180.0f,
            () -> rotationMode.get().equalsIgnoreCase("Sin Wave"));
    private IntegerSetting derpSpeed = new IntegerSetting("Derp Speed", 30, 1, 180,
            () -> rotationMode.get().equalsIgnoreCase("Derp"));
    private BooleanSetting predictedPosition = new BooleanSetting("Predicted Position", false,
            () -> !rotationMode.get().equalsIgnoreCase("Derp") || !rotationMode.get().equalsIgnoreCase("None"));
    private BooleanSetting rayTrace = new BooleanSetting("Ray Trace", false);
    private BooleanSetting alwaysSwing = new BooleanSetting("Always Swing", false);
    private BooleanSetting deadZone = new BooleanSetting("Dead Zone", false);
    private BooleanSetting throughWalls = new BooleanSetting("Through Walls", true);
    private BooleanSetting silentRotations = new BooleanSetting("Silent Rotations", true);
    private BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", true);
    private BooleanSetting onlyInAir = new BooleanSetting("Only In Air", false, () -> keepSprint.get());
    private BooleanSetting strafe = new BooleanSetting("Movement Fix", false);
    private BooleanSetting newCombat = new BooleanSetting("1.9 Delay", false);
    private BooleanSetting newSwing = new BooleanSetting("1.9 Swing", false);

    // Attacked Entity Types
    private BooleanSetting players = new BooleanSetting("Players", true);
    private BooleanSetting nonPlayers = new BooleanSetting("Non Players", true);
    private BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
    private BooleanSetting dead = new BooleanSetting("Attack Dead", false);

    // Render Settings
    private BooleanSetting targetEsp = new BooleanSetting("Target ESP", true);
    private BooleanSetting targetOnPlayer = new BooleanSetting("Target On Player", true);

    // Other Settings
    private BooleanSetting disableOnWorldChange = new BooleanSetting("Disable On World Change", true);
    // TODO: Add scaffold module
    // private BooleanSetting attackWithScaffold = new BooleanSetting("Attack With Scaffold", false);
    private BooleanSetting attackInInterface = new BooleanSetting("Attack In Interface", true);
    private BooleanSetting onClick = new BooleanSetting("On Click", false);

    public Aura() {
        super("Aura", Category.COMBAT);

        addSettings(mode, rotationMode, blockMode, sortingMode, range, rotationRange, extendedRangeEnabled, extendedRange, minCps, maxCps, maxTargets, predict, random, maxRotation, minRotation, maxYawRot, minYawRot, maxPitchRot, minPitchRot, sinWaveSpeed, derpSpeed, predictedPosition, rayTrace, alwaysSwing, deadZone, throughWalls, silentRotations, keepSprint, onlyInAir, strafe, newCombat, newSwing, players, nonPlayers, invisibles, dead, targetEsp, targetOnPlayer, attackInInterface, onClick);
        Dragon.eventManager.registerListener(this, WorldChangedEvent.class);
        Dragon.eventManager.registerListener(this, PacketReceiveEvent.class);
        Dragon.eventManager.registerListener(this, PreMotionEvent.class);
        Dragon.eventManager.registerListener(this, PostMotionEvent.class);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
        Dragon.eventManager.registerListener(this, Render3DEvent.class);
    }

    @Override
    public void onWorldChangedEvent(WorldChangedEvent event) {
        if (disableOnWorldChange.get()) {
            Dragon.moduleManager.addChatMessage("Disabling KillAura due to world change");
            setEnabled(false);
        }
    }

    @Override
    public void onPacketReceiveEvent(PacketReceiveEvent event) {
        final Packet<?> packet = event.packet;

        if (packet instanceof SPacketWindowItems) {
            blocking = false;
        }
    }
    
    @Override
    public void onPreMotionEvent(PreMotionEvent event) {
        ++this.hitTicks;

        if (blockMode.get().equalsIgnoreCase("Hypixel")) {
            if (target == null) {
                mc.gameSettings.keyBindUseItem.isPressed();
            }
        }
        // TODO: if we add the target strafe module then we need to set the targetstrafe variable to true if the module is enabled

        handle: {
            if (target == null) {
                if (!targetstrafe) mc.player.chasingPosY = Double.NaN;

                unblock();

                break handle;
            } else {
                switch (blockMode.get()) {
                    case "NCP":
                    case "Interact":
                        unblock();
                        break;
                }
            }

            /*
             * Whilst we have silent rotations enabled we only want the rotations to be seen server sided.
             * And whilst we have non-silent rotations we can just update our rotations manually.
             */

            if (this.silentRotations.get() && !rotationMode.get().equals("None")) {
                // if (!(PlayerUtil.isOnServer("Hypixel") && (this.getModule(Speed.class).isEnabled() || this.getModule(LongJump.class).isEnabled()))) {
                //     event.setYaw(serverYaw);
                //     event.setPitch(serverPitch);
                // }

                mc.player.renderYawOffset = serverYaw;
                mc.player.rotationYawHead = serverYaw;
            } else {
                mc.player.rotationYaw = serverYaw;
                mc.player.rotationPitch = serverPitch;
            }

            /*
             * Gets position on player to be used for render options
             */

            RayTraceResult RayTraceResult = PlayerUtil.getMouseOver(serverYaw, serverPitch, (float) range.get());

            if (RayTraceResult == null) {
                return;
            }

            final Vec3d rayCast = Objects.requireNonNull(RayTraceResult).hitVec;
            if (rayCast == null) return;
            lastPositionOnPlayer = positionOnPlayer;
            positionOnPlayer = rayCast;
        }
    }

    @Override
    public void onPostMotionEvent(PostMotionEvent event) {
        if (target != null && PlayerUtil.isHoldingSword()) {
            switch (blockMode.get()) {
                case "Hypixel":
                    if (mc.player.swingProgressInt == 1) {
                        new BlockPos();
                        PacketUtil.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    break;
            }
        }
    }
    
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if (!(!onClick.get() || Mouse.isButtonDown(0))) {
            target = null;
            return;
        }

        if (target == null) {
            /*
             * We want to make sure whilst we do not have a target to attack we
             * do not break the players strafing, so we reset it every tick in here.
             */
            if (!targetstrafe) mc.player.chasingPosY = Double.NaN;

            /*
             * This unblocks the aura when its not in a situation to block, because otherwise you flag movement
             */
            unblock();

            sinWaveTicks = 0;

            return;
        }

        double ping = 250;
        ping /= 50;
        if (predictedPosition.get()) {
            final double deltaX = (target.posX - target.lastTickPosX) * 2;
            final double deltaY = (target.posY - target.lastTickPosY) * 2;
            final double deltaZ = (target.posZ - target.lastTickPosZ) * 2;
            targetPosX = target.posX + deltaX * ping;
            targetPosY = target.posY + deltaY * ping;
            targetPosZ = target.posZ + deltaZ * ping;
        } else {
            targetPosX = target.posX;
            targetPosY = target.posY;
            targetPosZ = target.posZ;
        }

        // TODO: If we add the auto gap module then we need to check if the gap is not -37
        // if (AutoGap.gap != -37) {
        //     this.unblock();
        //     return;
        // }

        
        if (!rotationMode.get().equals("Sin Wave"))
            sinWaveTicks = 0;

        // TODO: if we add the scaffold module then we need to uncomment this
        // if ((this.getModule(Scaffold.class).isEnabled() && !attackWithScaffold.isEnabled())
        //     || ((mc.currentScreen != null && !(mc.currentScreen instanceof ClickGUI)) && !attackInInterfaces.isEnabled())) {
        //     unblock();
        //     target = null;
        //     return;
        // }

        /*
         * For our movement to be correctly fixed we are going to have
         * to use rotations the server actually sees instead of the
         * current ones as our rotations update per frame and this
         * will make it so our movement yaw and server yaw will
         * be different which will cause issues.
         */
        serverYaw = yaw;
        serverPitch = pitch;

        /*
         * If we want to correct our movement whilst rotating silently we can update
         * the chasingPosY variable which will correct our movement to the given yaw for us.
         */
        if (this.strafe.get() && this.silentRotations.get()) mc.player.chasingPosY = serverYaw;
        else if (!targetstrafe) mc.player.chasingPosY = Double.NaN;

        double delayValue = -1;

                /*
         * In the modern versions of Minecraft there is a hit delay which occurs when you hit somebody.
         * On specific items for a certain amount of ticks causes low damage until the time required has passed.
         * We have the delays set in here as an option for people who play on 1.9 and above servers.
         */
        if (this.newCombat.get()) {
            delayValue = 4;

            if (mc.player.getHeldItem(EnumHand.MAIN_HAND) != null) {
                final Item item = mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem();

                if (item instanceof ItemSpade || item == Items.GOLDEN_AXE || item == Items.DIAMOND_AXE || item == Items.WOODEN_HOE || item == Items.GOLDEN_HOE)
                    delayValue = 20;

                if (item == Items.WOODEN_AXE || item == Items.STONE_AXE)
                    delayValue = 25;

                if (item instanceof ItemSword)
                    delayValue = 12;

                if (item instanceof ItemPickaxe)
                    delayValue = 17;

                if (item == Items.IRON_AXE)
                    delayValue = 22;

                if (item == Items.STONE_HOE)
                    delayValue = 10;

                if (item == Items.IRON_HOE)
                    delayValue = 7;
            }

            delayValue *= Math.max(1, mc.timer.field_194147_b);
        }

        boolean attack = false;
        
        /*
         * This is the part we actually calculate the click delay we need in order land another hit.
         * The attack boolean will be true when the time required for another attack passes.
         */
        if (this.timer.hasReached(this.cps)) {
            final int maxValue = (int) ((this.minCps.getMaximum() - this.maxCps.get()) * 20);
            final int minValue = (int) ((this.minCps.getMaximum() - this.minCps.get()) * 20);

            this.cps = (int) (RandomUtil.nextInt(minValue, maxValue) - RandomUtil.RANDOM.nextInt(10) + RandomUtil.RANDOM.nextInt(10));

            this.timer.reset();

            attack = true;
        } else if (blockMode.get().equals("Bypass")) {
            this.unblock();
        }

        /*
         * Updates the Derp Rotation Modes yaw so that it rotates.
         */
        derpYaw += derpSpeed.get() - (((Math.random() - 0.5) * random.get()) / 2);

        if ((!newSwing.get() && attack) || (newSwing.get() && this.hitTicks > delayValue)) {
            final boolean rayCast = PlayerUtil.isMouseOver(serverYaw, serverPitch, target, (float) range.get()) || predictedPosition.get();
            double x = mc.player.posX;
            double z = mc.player.posZ;
            final double y = mc.player.posY;
            final double endPositionX = targetPosX;
            final double endPositionZ = targetPosZ;
            double distanceX = x - endPositionX;
            double distanceZ = z - endPositionZ;
            double distanceY = y - targetPosY;
            double distance = MathHelper.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ) * 6.5;
            if (extendedRangeEnabled.get()) {
                int packets = 0;

                while (distance > (range.get() - 0.5657) * 6.5 && packets < 100) {
                    final CPacketPlayer.Position c04 = new CPacketPlayer.Position(x, mc.player.posY, z, true);

                    PacketUtil.sendPacket(c04);

                    packetList.add(c04);

                    distanceX = x - endPositionX;
                    distanceZ = z - endPositionZ;
                    distanceY = y - targetPosY;
                    distance = MathHelper.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ) * 6.5;

                    final double v = (x * distance + endPositionX) / (distance + 1) - x;
                    final double v1 = (z * distance + endPositionZ) / (distance + 1) - z;
                    Dragon.moduleManager.addChatMessage(String.valueOf(MathHelper.sqrt(v * v + v1 * v1)));

                    x = (x * distance + endPositionX) / (distance + 1);
                    z = (z * distance + endPositionZ) / (distance + 1);

                    packets++;
                }
            }

            /*
             * Whilst the time required to attack an entity again has passed and our raytrace failed
             * when this setting enabled the client will swing even tho it cannot see it causing more
             * realistic swinging during pvp which could help making the aura less detectable.
             */
            if ((mc.player.getDistance(targetPosX, targetPosY, targetPosZ) - 0.5657 > ((this.extendedRangeEnabled.get()) ? this.extendedRange.get() : this.range.get()) && !rayCast)
                    || (this.rayTrace.get() && !rayCast)) {
                if (this.alwaysSwing.get()) {
                    PacketUtil.sendPacket(new CPacketAnimation());
                    return;
                }
            }

            /*
             * We want to make sure the target is actually on our attack distance and not only our rotation distance.
             * Plus if raytrace is enabled we shall make sure there is an intersection.
             */
            if (mc.player.getDistance(targetPosX, targetPosY, targetPosZ) - 0.5657 > ((this.extendedRangeEnabled.get()) ? this.extendedRange.get() : this.range.get())
                    || (this.rayTrace.get() && !rayCast)) return;

            /*
             * If we are not allowed to hit through walls we should not
             * attack the entity by checking if we can see them or not.
             */
            if (!this.throughWalls.get() && !mc.player.canEntityBeSeen(target)) return;

            /*
             * On the legacy versions of Minecraft the player before sending an interaction
             * packet sends a swing packet. Which is not the case on newer versions.
             */
            if (!this.newSwing.get()) mc.player.swingArm(EnumHand.MAIN_HAND);

            /*
             * When keep sprint is disabled to keep everything vanilla about
             * movement we can use the games attack method to keep everything vanilla.
             */

             switch (this.blockMode.get()) {
                case "AAC":
                case "Interact": {
                    this.unblock();
                    break;
                }
            }

            switch (mode.get()) {
                case "Single": {
                    /*
                     * Calls attack event so other modules can use information from the entity
                     * When the C02 packet is sent the attack event does not
                     * get called, so we have to manually call it ourselves in here.
                     */
                    final AttackEvent attackEvent = new AttackEvent(target);
                    Dragon.eventManager.callEvent(attackEvent);

                    if (attackEvent.isCancelled())
                        return;

                    if (this.keepSprint.get() && (!mc.player.onGround || !onlyInAir.get())) {
                        PacketUtil.sendPacket(new CPacketUseEntity(target));
                    } else {
                        mc.playerController.attackEntity(mc.player, target);
                    }

                    if (mc.player.fallDistance > 0) mc.player.onCriticalHit(target);
                    break;
                }

                case "Switch": {
                    final List<EntityLivingBase> entities = getTargets();

                    if (entities.size() >= targetIndex)
                        targetIndex = 0;

                    if (entities.isEmpty()) {
                        targetIndex = 0;
                        return;
                    }

                    final EntityLivingBase entity = entities.get(targetIndex);

                    /*
                     * Calls attack event so other modules can use information from the entity
                     * When the C02 packet is sent the attack event does not
                     * get called, so we have to manually call it ourselves in here.
                     */
                    final AttackEvent attackEvent = new AttackEvent(entity);
                    Dragon.eventManager.callEvent(attackEvent);

                    if (attackEvent.isCancelled())
                        return;

                    if (this.keepSprint.get() && (!mc.player.onGround || !onlyInAir.get())) {
                        PacketUtil.sendPacket(new CPacketUseEntity(entity));
                    } else {
                        mc.playerController.attackEntity(mc.player, entity);
                    }

                    if (mc.player.fallDistance > 0) mc.player.onCriticalHit(target);

                    targetIndex++;
                    break;
                }

                case "Multi": {
                    for (final EntityLivingBase entity : getTargets()) {
                        /*
                         * Calls attack event so other modules can use information from the entity
                         * When the C02 packet is sent the attack event does not
                         * get called, so we have to manually call it ourselves in here.
                         */
                        final AttackEvent attackEvent = new AttackEvent(target);
                        Dragon.eventManager.callEvent(attackEvent);

                        if (attackEvent.isCancelled())
                            return;

                        if (this.keepSprint.get() && (!mc.player.onGround || !onlyInAir.get())) {
                            PacketUtil.sendPacket(new CPacketUseEntity(entity));
                        } else {
                            mc.playerController.attackEntity(mc.player, entity);
                        }

                        if (mc.player.fallDistance > 0) mc.player.onCriticalHit(entity);
                    }
                    break;
                }
            }

            if (extendedRangeEnabled.get()) {
                Collections.reverse(packetList);
                packetList.forEach(PacketUtil::sendPacket);
                packetList.clear();
            }

            /*
             * On the modern versions of Minecraft unlike legacy the player
             * sends an arm swing after they interact with the object.
             */
            if (this.newSwing.get()) mc.player.swingArm(EnumHand.MAIN_HAND);

            // Resetting the hit ticks
            this.hitTicks = 0;
        }
        // TODO: if we add the scaffold module then we need to add this to the if statement
        //  && !Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Scaffold")).isEnabled()
        if (PlayerUtil.isHoldingSword()) {
            switch (this.blockMode.get()) {
                case "AAC": {
                    if (mc.player.ticksExisted % 2 == 0) {
                        mc.playerController.interactWithEntity(mc.player, target, EnumHand.MAIN_HAND);
                    }
                    break;
                }

                case "Bypass":
                case "Vanilla": {
                    break;
                }
            }
        }
    }

    @Override
    protected void onEnable() {
        ticks = 66;

        /*
         * For the first rotation to be properly rounded we can set our last
         * rotations to our current rotations in order to round everything properly.
         */
        lastYaw = mc.player.rotationYaw;
        lastPitch = mc.player.rotationPitch;
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;

        sinWaveTicks = 0;

        /*
         * Sets blocking variable, so we can use this to not send extra blocking packets
         */
        blocking = mc.gameSettings.keyBindUseItem.isKeyDown();
    }

    @Override
    protected void onDisable() {
        // We do not want to strafe whilst not using aura.
        if (!targetstrafe) mc.player.chasingPosY = Double.NaN;

        // Resetting the target index for Switch.
        targetIndex = 0;

        // Reset our timer for our attacks.
        timer.reset();

        // Set our target null as we do not want other stuff thinking we are attacking something.
        target = null;

        // This will only unblock if you're already blocking
        mc.gameSettings.keyBindUseItem.isPressed();
        unblock();
    }

        @Override
    public void onRender3DEvent(final Render3DEvent event) {
        // TODO: if we add the scaffold module then we need to add this to the if statement
        // if ((this.getModule(Scaffold.class).isEnabled() && !attackWithScaffold.isEnabled())
        //         || ((mc.currentScreen != null && !(mc.currentScreen instanceof ClickGUI)) && !attackInInterfaces.isEnabled())) {
        //     unblock();
        //     target = null;
        //     return;
        // }

        // Update our target for the aura as we want the entity we want to attack right now.
        this.updateTarget();

        /*
         * If the aura could not find a target on the specified settings
         * we cannot grab rotations or attack anything so we can return.
         */
        if (target == null) {
            lastYaw = mc.player.rotationYaw;
            lastPitch = mc.player.rotationPitch;
        } else {
            /*
             * Because we have found a target successfully we can grab the
             * required rotations to look and actually attack this target.
             */
            this.updateRotations();

                        switch (mode.get()) {
                case "Single": {
                    if (target != null) {
                        drawCircle(target, 0.67, -1, true);
                    }
                    break;
                }
                case "Switch": {
                    if (target != null) {
                        drawCircle(target, 0.67, -1, true);
                    }
                    break;
                }

                case "Multi": {
                    for (final EntityLivingBase entity : getTargets()) {
                        drawCircle(entity, 0.67, -1, true);
                    }
                    break;
                }
        }
    }

    }
    
    private void unblock() {
        if (blocking) {
            PacketUtil.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            blocking = false;
        }
    }

    private void updateRotations() {
        /*
         * Update our last rotations as the current ones as we are updating
         * the current ones soon. We require the last rotations to smooth
         * out the current rotations properly based on the last rotations.
         */
        lastYaw = yaw;
        lastPitch = pitch;

        /*
         * Finally grab the required rotations to actually aim at the target.
         * We do not need to pass in any parameters as the method already grabs the settings for us.
         */
        final float[] rotations = this.getRotations();

        /*
         * We can now update the rotation fields for the aura so the client
         * can send the server the rotations we actually want to apply.
         */
        yaw = rotations[0];
        pitch = rotations[1];

        if (deadZone.get()) {
            if (rayTrace(lastYaw, lastPitch, rotationRange.get(), target)) {
                yaw = lastYaw;
                pitch = lastPitch;
            }
        }
    }

        private float[] getRotations() {
        final double predictValue = predict.get();

        final double x = (targetPosX - (target.lastTickPosX - targetPosX) * predictValue) + 0.01 - mc.player.posX;
        final double z = (targetPosZ - (target.lastTickPosZ - targetPosZ) * predictValue) - mc.player.posZ;

        double minus = (mc.player.posY - targetPosY);

        if (minus < -1.4) minus = -1.4;
        if (minus > 0.1) minus = 0.1;

        final double y = (targetPosY - (target.lastTickPosY - targetPosY) * predictValue) + 0.4 + target.getEyeHeight() / 1.3 - (mc.player.posY + mc.player.getEyeHeight()) + minus;

        final double xzSqrt = MathHelper.sqrt(x * x + z * z);

        float yaw = MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(z, x)) - 90.0F);
        float pitch = MathHelper.wrapDegrees((float) Math.toDegrees(-Math.atan2(y, xzSqrt)));

        final double randomAmount = random.get();

        if (randomAmount != 0) {
            randomYaw += ((Math.random() - 0.5) * randomAmount) / 2;
            randomYaw += ((Math.random() - 0.5) * randomAmount) / 2;
            randomPitch += ((Math.random() - 0.5) * randomAmount) / 2;

            if (mc.player.ticksExisted % 5 == 0) {
                randomYaw = (float) (((Math.random() - 0.5) * randomAmount) / 2);
                randomPitch = (float) (((Math.random() - 0.5) * randomAmount) / 2);
            }

            yaw += randomYaw;
            pitch += randomPitch;
        }

        final int fps = (int) (Minecraft.getDebugFPS() / 20.0F);

        switch (this.rotationMode.get()) {
            case "Custom": {
                if (this.maxRotation.get() != 180.0F && this.minRotation.get() != 180.0F) {
                    final float distance = (float) RandomUtil.nextFloat(this.minRotation.get(), this.maxRotation.get());

                    final float deltaYaw = (((yaw - lastYaw) + 540) % 360) - 180;
                    final float deltaPitch = pitch - lastPitch;

                    final float distanceYaw = MathHelper.clamp(deltaYaw, -distance, distance) / fps * 4;
                    final float distancePitch = MathHelper.clamp(deltaPitch, -distance, distance) / fps * 4;

                    yaw = MathHelper.wrapDegrees(lastYaw) + distanceYaw;
                    pitch = MathHelper.wrapDegrees(lastPitch) + distancePitch;
                }
                break;
            }

            case "Custom Simple": {
                final float yawDistance = (float) RandomUtil.nextFloat(this.minRotation.get(), this.maxRotation.get());
                final float pitchDistance = (float) RandomUtil.nextFloat(this.minRotation.get(), this.maxRotation.get());


                final float deltaYaw = (((yaw - lastYaw) + 540) % 360) - 180;
                final float deltaPitch = pitch - lastPitch;

                final float distanceYaw = MathHelper.clamp(deltaYaw, -yawDistance, yawDistance) / fps * 4;
                final float distancePitch = MathHelper.clamp(deltaPitch, -pitchDistance, pitchDistance) / fps * 4;

                yaw = lastYaw + distanceYaw;
                pitch = lastPitch + distancePitch;
                break;
            }

            case "Custom Advanced": {
                final float advancedYawDistance = (float) RandomUtil.nextFloat(this.minYawRot.get(), this.maxYawRot.get());
                final float advancedPitchDistance = (float) RandomUtil.nextFloat(this.minPitchRot.get(), this.maxPitchRot.get());

                final float advancedDeltaYaw = (((yaw - lastYaw) + 540) % 360) - 180;
                final float advancedDeltaPitch = pitch - lastPitch;

                final float advancedDistanceYaw = MathHelper.clamp(advancedDeltaYaw, -advancedYawDistance, advancedYawDistance) / fps * 4;
                final float advancedDistancePitch = MathHelper.clamp(advancedDeltaPitch, -advancedPitchDistance, advancedPitchDistance) / fps * 4;

                yaw = lastYaw + advancedDistanceYaw;
                pitch = lastPitch + advancedDistancePitch;
                break;
            }

            case "Smooth": {
                final float yawDelta = (float) (((((yaw - lastYaw) + 540) % 360) - 180) / (fps / 3 * (1 + Math.random())));
                final float pitchDelta = (float) ((pitch - lastPitch) / (fps / 3 * (1 + Math.random())));

                yaw = lastYaw + yawDelta;
                pitch = lastPitch + pitchDelta;

                break;
            }

            case "Down": {
                pitch = RandomUtil.nextFloat(89, 90);
                break;
            }

            case "Up": {
                pitch = RandomUtil.nextFloat(-80, -81);
                break;
            }

            case "Derp": {
                pitch = RandomUtil.nextFloat(89, 90);
                yaw = derpYaw;
                break;
            }

            case "Sin Wave": {
                final float halal = (float) (Math.abs(Math.sin((sinWaveTicks + Math.random() * 0.001) / 10)) * sinWaveSpeed.get());

                final float sinWaveYaw = MathHelper.clamp((((yaw - lastYaw) + 540) % 360) - 180, -halal, halal) / fps;
                final float sinWavePitch = MathHelper.clamp(pitch - lastPitch, -halal, halal) / fps / fps;

                yaw = lastYaw + sinWaveYaw;
                pitch = lastPitch + sinWavePitch;

                sinWaveTicks++;
                break;
            }
        }

        final float[] rotations = new float[]{yaw, pitch};
        final float[] lastRotations = new float[]{yaw, pitch};

        final float[] fixedRotations = RotationUtil.getFixedRotation(rotations, lastRotations);

        yaw = fixedRotations[0];
        pitch = fixedRotations[1];

        if (this.rotationMode.get().equals("None")) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
        }

        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);

        return new float[]{yaw, pitch};
    }

    private List<EntityLivingBase> getTargets() {
        final List<EntityLivingBase> entities = mc.world.loadedEntityList
                // Stream our entity list.
                .stream()

                // Only get living entities so we don't have to check for items on ground etc.
                .filter(entity -> entity instanceof EntityLivingBase)

                // Map our entities to entity living base as we have filtered out none living entities.
                .map(entity -> ((EntityLivingBase) entity))

                // Only get the entities we can attack.
                .filter(entity -> {
                    if (entity instanceof EntityPlayer && !players.get()) return false;

                    if (!(entity instanceof EntityPlayer) && !nonPlayers.get()) return false;

                    if (entity.isInvisible() && !invisibles.get()) return false;

                    // if (PlayerUtil.isOnSameTeam(entity) && teams.isEnabled()) return false;

                    if (entity.isDead && !dead.get()) return false;

                    if (entity.deathTime != 0 && !dead.get()) return false;

                    if (entity.ticksExisted < 2) return false;

                    // if (AntiBot.bots.contains(entity)) return false;

                    if (entity instanceof EntityPlayer) {
                        final EntityPlayer player = ((EntityPlayer) entity);

                        // TODO: Add a friends system
                        // for (final String name : instance.getFriends()) {
                        //     if (name.equalsIgnoreCase(player.getGameProfile().getName()))
                        //         return false;
                        // }

                        // dude there's nothing here
                        String[] hotPeople = new String[]{"WilliamVanBeast"};
                        for (String name : hotPeople) {
                            if (player.getName().equals(name)) return false;
                        }

                        // if (PlayerUtil.isOnServer("blocksmc") && (player.getName().contains("SHOP") || player.getName().contains("UPGRADES")))
                        //     return false;
                    }

                    return mc.player != entity;
                })

                // Do a proper distance calculation to get entities we can reach.
                .filter(entity -> {
                    // DO NOT TOUCH THIS VALUE ITS CALCULATED WITH MATH
                    final double girth = 0.5657;

                    // See if the other entity is in our range.
                    return mc.player.getDistanceToEntity(entity) - girth < rotationRange.get();
                })

                // Sort out potential targets with the algorithm provided as a setting.
                .sorted(Comparator.comparingDouble(entity -> {
                    switch (sortingMode.get()) {
                        case "Distance":
                            return mc.player.getDistanceSqToEntity(entity);
                        case "Health":
                            return entity.getHealth();
                        case "Hurttime":
                            return entity.hurtTime;

                        default:
                            return -1;
                    }
                }))

                // Sort out all the specified targets.
                // TODO: add targets list
                // .sorted(Comparator.comparing(entity -> entity instanceof EntityPlayer && !Rise.INSTANCE.getTargets().contains(((EntityPlayer) entity).getName())))

                // Get the possible targets and put them in a list.
                .collect(Collectors.toList());

        // Removes entities when there are too many targets
        final int maxTargets = (int) Math.round(this.maxTargets.get());

        if (mode.get().equals("Multi") && entities.size() > maxTargets) {
            entities.subList(maxTargets, entities.size()).clear();
        }

        // Returns the list of entities
        return entities;
    }

    private void updateTarget() {
        final List<EntityLivingBase> entities = getTargets();

        // Grab our best option from the list.
        target = entities.size() > 0 ? entities.get(0) : null;
    }

    private boolean rayTrace(final float yaw, final float pitch, final double reach, final Entity target) {
        final Vec3d Vec3d = mc.player.getPositionEyes(mc.timer.field_194148_c);
        final Vec3d Vec3d1 = mc.player.getVectorForRotation(MathHelper.clamp(pitch, -90.F, 90.F), yaw % 360);
        final Vec3d Vec3d2 = Vec3d.addVector(Vec3d1.xCoord * reach, Vec3d1.yCoord * reach, Vec3d1.zCoord * reach);

        final RayTraceResult objectPosition = target.getEntityBoundingBox().calculateIntercept(Vec3d, Vec3d2);

        return (objectPosition != null && objectPosition.hitVec != null);
    }

  
    private void drawCircle(final Entity entity, final double rad, final int color, final boolean shade) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(false);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        if (shade) GL11.glShadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableCull();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.field_194148_c - (mc.getRenderManager()).renderPosX;
        final double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.field_194148_c - (mc.getRenderManager()).renderPosY) + Math.sin(System.currentTimeMillis() / 2E+2) + 0.8;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.field_194148_c - (mc.getRenderManager()).renderPosZ;

        for (float i = 0; i < Math.PI * 2; i += Math.PI * 2 / 64.F) {
            final double vecX = x + rad * Math.cos(i);
            final double vecZ = z + rad * Math.sin(i);

            if (shade) {
                GL11.glColor4f(255f / 255.F,
                        255f / 255.F,
                        255f / 255.F,
                        0
                );
                GL11.glVertex3d(vecX, y - Math.cos(System.currentTimeMillis() / 2E+2) / 2.0F, vecZ);
                GL11.glColor4f(255f / 255.F,
                        255f / 255.F,
                        255f / 255.F,
                        0.85F
                );
            }
            GL11.glVertex3d(vecX, y, vecZ);
        }

        GL11.glEnd();
        if (shade) GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableCull();
        GL11.glDisable(2848);
        GL11.glDisable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glColor3f(255, 255, 255);
    }

   
}
