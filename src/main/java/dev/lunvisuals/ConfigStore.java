package dev.lunvisuals;

import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public final class ConfigStore {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("lunvisuals.properties");
    private ConfigStore() {}
    public static void load(ModuleRegistry registry) {
        if (!Files.exists(FILE)) return;
        Properties p = new Properties();
        try (Reader r = Files.newBufferedReader(FILE)) {
            p.load(r);
            for (ModuleRegistry.Module m : registry.all()) m.enabled = Boolean.parseBoolean(p.getProperty(m.name, "false"));
        } catch (IOException e) { System.err.println("[LunVisuals] Could not load config: " + e.getMessage()); }
    }
    public static void save(ModuleRegistry registry) {
        Properties p = new Properties();
        for (ModuleRegistry.Module m : registry.all()) p.setProperty(m.name, Boolean.toString(m.enabled));
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer w = Files.newBufferedWriter(FILE)) { p.store(w, "LunVisuals module states"); }
        } catch (IOException e) { System.err.println("[LunVisuals] Could not save config: " + e.getMessage()); }
    }
}
