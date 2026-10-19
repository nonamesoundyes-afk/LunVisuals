package dev.lunvisuals;

import java.util.*;

public final class ModuleRegistry {
    public enum Category { VISUAL, HANDS, INTERFACE, UTILITY }
    public static final class Module {
        public final String name, description;
        public final Category category;
        public boolean enabled;
        Module(String name, String description, Category category) {
            this.name = name; this.description = description; this.category = category;
        }
    }
    private final List<Module> modules = new ArrayList<>();
    public ModuleRegistry() {
        add("Fullbright", "Brighter world rendering", Category.VISUAL);
        add("Zoom", "Camera zoom", Category.VISUAL);
        add("Crosshair", "Crosshair customization", Category.VISUAL);
        add("Block Overlay", "Block selection outline", Category.VISUAL);
        add("Custom World", "Local time, weather and sky options", Category.VISUAL);
        add("Trails", "Visual movement trails", Category.VISUAL);
        add("Hit Particles", "Custom hit particles", Category.VISUAL);
        add("View Model", "Adjust first-person item position", Category.HANDS);
        add("Swing Animation", "First-person swing animation options", Category.HANDS);
        add("Animations", "Animated interface transitions", Category.HANDS);
        add("HUD", "Coordinates and FPS overlay", Category.INTERFACE);
        add("Custom Scoreboard", "Scoreboard styling", Category.INTERFACE);
        add("Inventory HUD", "Inventory overlay", Category.INTERFACE);
        add("Theme", "Accent color and opacity", Category.INTERFACE);
        add("Name Protect", "Hide your name in supported UI elements", Category.UTILITY);
        add("Streamer Mode", "Privacy-oriented display options", Category.UTILITY);
        add("Shulker View", "Preview shulker contents", Category.UTILITY);
        add("Auto Respawn", "Respawn convenience option", Category.UTILITY);
    }
    private void add(String name, String desc, Category cat) { modules.add(new Module(name, desc, cat)); }
    public List<Module> all() { return Collections.unmodifiableList(modules); }
    public List<Module> in(Category cat) { return modules.stream().filter(m -> m.category == cat).toList(); }
    public Optional<Module> byName(String name) { return modules.stream().filter(m -> m.name.equals(name)).findFirst(); }
}
