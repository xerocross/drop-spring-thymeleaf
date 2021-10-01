package com.adamfgcross.drop.bootstrap;

import com.adamfgcross.drop.entity.Drop;

import java.util.List;

public class BootstrapDrops {
    List<Drop> drops;

    public BootstrapDrops(List<Drop> drops) {
        this.drops = drops;
    }

    public BootstrapDrops() {
    }

    public List<Drop> getDrops() {
        return drops;
    }

    public void setDrops(List<Drop> drops) {
        this.drops = drops;
    }
}
