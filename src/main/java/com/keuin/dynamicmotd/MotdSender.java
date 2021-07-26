package com.keuin.dynamicmotd;

import com.keuin.dynamicmotd.event.OnPlayerJoinedEvent;
import com.keuin.dynamicmotd.util.Exceptions;
import liqp.ProtectionSettings;
import liqp.Template;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MotdSender implements OnPlayerJoinedEvent.OnPlayerJoinedEventCallback {

    private static final Logger logger = Logger.getLogger(MotdSender.class.getName());
    private final CachedTemplate template = new CachedTemplate(Paths.get("motd.liquid"));
    private final RenderDataMap renderDataMap;

    static {
        logger.setLevel(Level.FINEST);
    }

    public MotdSender(MinecraftServer server) {
        this.renderDataMap = new RenderDataMap(server);
    }

    @Override
    public void onPlayerJoined(ServerPlayerEntity player) {
        // TODO: cache template in memory
        final String fileName = "motd.liquid";
        try {
            var template = this.template.get();
            var motd = template.render(this.renderDataMap)
                    .replace("\r\n", "\n")
                    .replace('\r', '\n');
            player.sendMessage(new LiteralText(motd), false);
        } catch (FileNotFoundException e) {
            logger.severe(String.format("Template file `%s` does not exist!", fileName));
        } catch (IOException e) {
            logger.severe("Cannot read template: " + e + e.getMessage() + Exceptions.getStackTrace(e));
        } catch (RuntimeException e) {
            logger.severe("Uncaught exception: " + e + e.getMessage() + Exceptions.getStackTrace(e));
        }
    }

    private static class CachedTemplate {

        private Template cachedTemplate = null;
        private final Path path;
        private FileTime lastModifiedTime;

        public CachedTemplate(Path templateFile) {
            this.path = templateFile;
        }

        public Template get() throws IOException {
            var fileTime = Files.readAttributes(this.path, BasicFileAttributes.class).lastModifiedTime();
            if (cachedTemplate == null) {
                updateTemplate();
            } else if (!fileTime.equals(this.lastModifiedTime)) {
                this.lastModifiedTime = fileTime;
                updateTemplate();
            }
            return this.cachedTemplate;
        }

        private void updateTemplate() throws IOException {
            this.cachedTemplate = Template
                    .parse(this.path.toFile())
                    .withProtectionSettings(
                            new ProtectionSettings.Builder()
                                    .withMaxSizeRenderedString(10000)
                                    .withMaxRenderTimeMillis(1000L)
                                    .withMaxTemplateSizeBytes(10000)
                                    .build());
        }
    }
}
