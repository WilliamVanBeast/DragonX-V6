package dragonclient.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import dragonclient.event.Events.*;
import dragonclient.module.Module;
public final class EventManager {
    Map<Listener, HashSet<Class<? extends Event>>> listeners = new HashMap<>();

    public void registerListener(Listener listener, Class<? extends Event> event) {
        if (listeners.containsKey(listener)) {
            listeners.get(listener).add(event);
        } else {
            HashSet<Class<? extends Event>> set = new HashSet<>();
            set.add(event);
            listeners.put(listener, set);
        }
    }

    public void unregisterListener(Listener listener) {
        listeners.remove(listener);
    }

    public void callEvent(Event event) {
        for (Map.Entry<Listener, HashSet<Class<? extends Event>>> entry : listeners.entrySet()) {
            if ((Object) entry.getKey() instanceof Module) {
                if (!((Module) ((Object) entry.getKey())).isEnabled()) {
                    continue;
                }
            }

            if (entry.getValue().contains(event.getClass())) {
                if (event instanceof UpdateEvent) {
                    entry.getKey().onUpdateEvent((UpdateEvent) event);
                } else if (event instanceof PreMotionEvent) {
                    entry.getKey().onPreMotionEvent((PreMotionEvent) event);
                } else if (event instanceof PostMotionEvent) {
                    entry.getKey().onPostMotionEvent((PostMotionEvent) event);
                } else if (event instanceof AttackEvent) {
                    entry.getKey().onAttackEvent((AttackEvent) event);
                } else if (event instanceof TickEvent) {
                    entry.getKey().onTickEvent((TickEvent) event);
                } else if (event instanceof Render3DEvent) {
                    entry.getKey().onRender3DEvent((Render3DEvent) event);
                } else if (event instanceof PacketSendEvent) {
                    entry.getKey().onPacketSendEvent((PacketSendEvent) event);
                } else if (event instanceof PacketReceiveEvent) {
                    entry.getKey().onPacketReceiveEvent((PacketReceiveEvent) event);
                } else if (event instanceof Render2DEvent) {
                    entry.getKey().onRender2DEvent((Render2DEvent) event);
                } else if (event instanceof BlockBreakEvent) {
                    entry.getKey().onBlockBreakEvent((BlockBreakEvent) event);
                } else if (event instanceof JumpEvent) {
                    entry.getKey().onJumpEvent((JumpEvent) event);
                } else if (event instanceof WorldChangedEvent) {
                    entry.getKey().onWorldChangedEvent((WorldChangedEvent) event);
                } else if (event instanceof ChatEvent) {
                    entry.getKey().onChatEvent((ChatEvent) event);
                } else if (event instanceof ActionEvent) {
                    entry.getKey().onActionEvent((ActionEvent) event);
                } else if (event instanceof MoveEvent) {
                    entry.getKey().onMoveEvent((MoveEvent) event);
                }else if (event instanceof RenderNametagEvent) {
                    entry.getKey().onRenderNametagEvent((RenderNametagEvent) event);
                }
            }
        }
    }
}
