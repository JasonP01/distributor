package fr.xpdustry.distributor.audience;

import arc.audio.Sound;
import fr.xpdustry.distributor.meta.MetaContainer;
import fr.xpdustry.distributor.meta.MetaKey;
import fr.xpdustry.distributor.text.Component;
import fr.xpdustry.distributor.text.renderer.ComponentRenderer;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;
import mindustry.core.Version;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.net.NetConnection;
import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("JavaReflectionMemberAccess")
final class PlayerAudience implements Audience {

  private static final @Nullable Method CALL_PLAY_SOUND;
  private static final @Nullable Method CALL_PLAY_SOUND_AT;

  static {
    if (Version.isAtLeast("135")) {
      try {
        CALL_PLAY_SOUND = Call.class.getMethod(
          "sound",
          NetConnection.class, Sound.class, float.class, float.class, float.class
        );
        CALL_PLAY_SOUND_AT = Call.class.getMethod(
          "soundAt",
          NetConnection.class, Sound.class, float.class, float.class, float.class, float.class, float.class
        );
      } catch (final NoSuchMethodException e) {
        throw new RuntimeException("Mindustry v135+ should support play sounds.", e);
      }
    } else {
      CALL_PLAY_SOUND = null;
      CALL_PLAY_SOUND_AT = null;
    }
  }

  private final Player player;
  private final MetaContainer metas;

  PlayerAudience(final Player player) {
    this.player = player;
    this.metas = MetaContainer.builder()
      .withConstant(MetaKey.UUID, this.player.uuid())
      .withConstant(
        MetaKey.LOCALE, Locale.forLanguageTag(this.player.locale().replace('-', '_'))
      )
      .withSupplier(MetaKey.NAME, this.player::name)
      .withSupplier(MetaKey.TEAM, this.player::team)
      .build();
  }

  @Override
  public void sendMessage(final Component component) {
    Call.sendMessage(player.con(), ComponentRenderer.client().render(component), null, null);
  }

  @Override
  public void sendWarning(final Component component) {
    Call.announce(player.con(), ComponentRenderer.client().render(component));
  }

  @Override
  public void sendAnnouncement(final Component component) {
    Call.infoMessage(player.con(), ComponentRenderer.client().render(component));
  }

  @Override
  public void sendNotification(final Component component, final char icon) {
    Call.warningToast(player.con(), icon, ComponentRenderer.client().render(component));
  }

  @Override
  public void sendNotification(final Component component) {
    Call.warningToast(player.con(), 0, ComponentRenderer.client().render(component));
  }

  @Override
  public void playSound(final Sound sound, final float volume, final float pitch, final float pan, final float x, final float y) {
    if (CALL_PLAY_SOUND_AT != null) {
      try {
        CALL_PLAY_SOUND_AT.invoke(null, player.con(), sound, x, y, volume, pitch, pan);
      } catch (final ReflectiveOperationException e) {
        throw new IllegalStateException("Failed to play sound.", e);
      }
    }
  }

  @Override
  public void playSound(final Sound sound, final float volume, final float pitch, final float pan) {
    if (CALL_PLAY_SOUND != null) {
      try {
        CALL_PLAY_SOUND.invoke(null, player.con(), sound, volume, pitch, pan);
      } catch (final ReflectiveOperationException e) {
        throw new IllegalStateException("Failed to play sound.", e);
      }
    }
  }

  @Override
  public void setHud(final Component component) {
    Call.setHudText(ComponentRenderer.client().render(component));
  }

  @Override
  public void clearHud() {
    Call.hideHudText(player.con());
  }

  @Override
  public <T> Optional<T> getMeta(final MetaKey<T> key) {
    return metas.getMeta(key);
  }
}