package com.adamfgcross.drop.config;

import com.adamfgcross.drop.bootstrap.BootstrapDrops;
import com.adamfgcross.drop.bootstrap.DataBootstrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

@Configuration
public class AppConfig {

    @Bean
    public BootstrapDrops bootstrapDrops() {
        Yaml yaml = new Yaml(new Constructor(BootstrapDrops.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("BootstrapDrops.yaml");
        BootstrapDrops bootstrapDrops = yaml.load(inputStream);
        return bootstrapDrops;
    }

}
