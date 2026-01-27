package com.xyc.practicalextensions.client;

import com.xyc.practicalextensions.base.Module;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public record ModuleLang(
    String optionKey,
    Optional<String> option,
    String tooltipKey,
    Optional<String> tooltip
) {
    public static Map<String, ModuleLang> getModuleLang(Module module) {
        Map<String, String> option = module.getOptionTranslations();
        Map<String, String> tooltip = module.getTooltipTranslations();
        Map<String, ModuleLang> output = new HashMap<>();
        Stream.concat(module.getOptionTranslations().keySet().stream(),
                  module.getTooltipTranslations().keySet().stream())
              .distinct()
              .forEach(locale -> output.put(
                  locale,
                  new ModuleLang(
                      module.getOptionTranslationKey(),
                      Optional.of(option.get(locale)),
                      module.getTooltipTranslationKey(),
                      Optional.of(tooltip.get(locale))
                  )
              ));
        return output;
    }

    public static Map<Module, Map<String, ModuleLang>> getAllModuleLang(Set<Module> MODULES) {
        return MODULES.stream()
                      .map(module -> Map.entry(module, getModuleLang(module)))
                      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
