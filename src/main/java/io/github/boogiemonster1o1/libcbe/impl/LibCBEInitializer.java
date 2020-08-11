package io.github.boogiemonster1o1.libcbe.impl;

import org.apache.logging.log4j.LogManager;

import net.fabricmc.api.ModInitializer;

public class LibCBEInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        // no idea why i've added an initializer
        // currently there's just a few mixins and api classes
        // might remove this later
        LogManager.getLogger().info("Initializing Conditional Block Entities");
    }
}
