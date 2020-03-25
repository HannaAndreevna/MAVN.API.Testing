package com.lykke.api.testing.config;

import static com.lykke.api.testing.config.ConfigConsts.CENTRAL_CONFIG_FILE_NAME;
import static com.lykke.api.testing.config.ConfigConsts.ENVIRONMENT_FILE_PREFIX;
import static com.lykke.api.testing.config.ConfigConsts.YAML_EXTENSION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.lykke.api.testing.config.environment.EnvironmentSettings;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@UtilityClass
public class SettingsReader<E> {

    private static Settings settings;
    private static String folderPath;

    static {
        if (null == settings) {
            settings = readSettingsFromCentralYaml("");
        }
    }

    public static Settings readSettings(String configFolderPath) {
        if (configFolderPath.isEmpty()) {
            folderPath = configFolderPath;
        }
        if (null == settings) {
            settings = readSettingsFromCentralYaml(folderPath);
        }
        return settings;
    }

    private static Settings readSettingsFromCentralYaml(String configFolderPath) {
        final Settings allSettings = readSettingsFromEnvironmentYaml(
                new Settings(),
                configFolderPath + CENTRAL_CONFIG_FILE_NAME + YAML_EXTENSION);
        val environmentFileNameSuffix = allSettings.getEnvironment().getCode();
        EnvironmentSettings environmentSettings =
                readSettingsFromEnvironmentYaml(
                        new EnvironmentSettings(),
                        configFolderPath + ENVIRONMENT_FILE_PREFIX + environmentFileNameSuffix + YAML_EXTENSION);
        ;
        allSettings.setEnvironmentSettings(environmentSettings);
        return allSettings;
    }

    @SneakyThrows
    private static <E> E readSettingsFromEnvironmentYaml(E instance, String fileName) {
        ////val yaml = new Yaml(new Constructor(instance.getClass()));
        val mapper = new ObjectMapper(new YAMLFactory());
        val file = new File(instance.getClass().getClassLoader().getResource(fileName).getFile());
        ////val yamlContent = readFile(file.getAbsolutePath(), Charset.defaultCharset());
        ////return yaml.load(yamlContent);
        return (E) mapper.readValue(file, instance.getClass());
    }

    @SneakyThrows
    private static String readFile(String path, Charset encoding) {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
