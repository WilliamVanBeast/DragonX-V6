package dragonclient.event;

import dragonclient.event.Events.*;

public interface Listener {
    default void onUpdateEvent(UpdateEvent event) {
        throw new UnsupportedOperationException("unimplemented onUpdateEvent was called");
    }

    default void onPreMotionEvent(PreMotionEvent event) {
        throw new UnsupportedOperationException("unimplemented onPreMotionEvent was called");
    }

    default void onPostMotionEvent(PostMotionEvent event) {
        throw new UnsupportedOperationException("unimplemented onPostMotionEvent was called");
    }

    default void onMoveEvent(MoveEvent event) {
        throw new UnsupportedOperationException("unimplemented MoveEvent was called");
    }

    default void onRenderNametagEvent(RenderNametagEvent event) {
        throw new UnsupportedOperationException("unimplemented RenderNametagEvent was called");
    }

    default void onAttackEvent(AttackEvent event) {
        throw new UnsupportedOperationException("unimplemented onAttackEvent was called");
    }

    default void onTickEvent(TickEvent event) {
        throw new UnsupportedOperationException("unimplemented onTickEvent was called");
    }

    default void onRender3DEvent(Render3DEvent event) {
        throw new UnsupportedOperationException("unimplemented onRender3DEvent was called");
    }

    default void onRender2DEvent(Render2DEvent event) {
        throw new UnsupportedOperationException("unimplemented onRender2DEvent was called");
    }

    default void onPacketSendEvent(PacketSendEvent event) {
        throw new UnsupportedOperationException("unimplemented onPacketSendEvent was called");
    }

    default void onPacketReceiveEvent(PacketReceiveEvent event) {
        throw new UnsupportedOperationException("unimplemented onPacketReceiveEvent was called");
    }

    default void onBlockBreakEvent(BlockBreakEvent event) {
        throw new UnsupportedOperationException("unimplemented onBlockBreakEvent was called");
    }

    default void onJumpEvent(JumpEvent event) {
        throw new UnsupportedOperationException("unimplemented onJumpEvent was called");
    }

    default void onWorldChangedEvent(WorldChangedEvent event) {
        throw new UnsupportedOperationException("unimplemented onWorldChangedEvent was called");
    }

    default void onChatEvent(ChatEvent event) {
        throw new UnsupportedOperationException("unimplemented onChatEvent was called");
    }


    default void onActionEvent(ActionEvent event) {
        throw new UnsupportedOperationException("unimplemented onActionEvent was called");
    }

}