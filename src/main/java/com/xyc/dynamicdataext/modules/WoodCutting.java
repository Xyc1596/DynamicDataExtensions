package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.DynamicDataRegistry;
import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.SingleItemRecipeGraph;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleOption;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.ConfigUtils;
import com.xyc.dynamicdataext.utils.LocationUtils;
import com.xyc.dynamicdataext.utils.RegistryUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class WoodCutting extends Module {
    public static final WoodCutting INSTANCE = new WoodCutting(
        DynamicDataMain.MOD_ID, "wood_cutting"
    );
    public static final TranslatableLang TITLE = INSTANCE.configBuilder
        .getTitleLangBuilder()
        .translation("zh_cn", "切木")
        .translation("en_us", "Wood Cutting")
        .build();

    protected ModuleOption<Boolean> woodOption;
    protected ModuleOption<Boolean> fenceOption;
    protected ModuleOption<Boolean> fenceGateOption;
    protected ModuleOption<Boolean> trapdoorOption;
    protected ModuleOption<Boolean> doorOption;

    protected WoodCutting(String namespace, String moduleId) {
        super(namespace, moduleId);
    }

    @Override
    public @NotNull Set<DynamicRecipeEntry> gatherRecipesToAdd() {
        boolean reversible = DynamicDataRegistry.isModuleEnabled(
            DynamicDataMain.MOD_ID, ReversibleCutting.INSTANCE.getModuleId()
        );
        return new LinkedHashSet<>(buildPlankRecipeGraph().createAllRecipes(this, reversible));
    }

    protected @NotNull SingleItemRecipeGraph buildPlankRecipeGraph() {
        SingleItemRecipeGraph graph = new SingleItemRecipeGraph();
        boolean trapdoorBalance = DynamicDataRegistry.isModuleEnabled(
            DynamicDataMain.MOD_ID, TrapdoorBalance.INSTANCE.getModuleId()
        );
        boolean woodEnabled = woodOption.getValue();
        boolean fenceEnabled = fenceOption.getValue();
        boolean fenceGateEnabled = fenceGateOption.getValue();
        boolean trapdoorEnabled = trapdoorOption.getValue();
        boolean doorEnabled = doorOption.getValue();

        Map<String, Item> materialToLogMap = new HashMap<>();
        Optional<HolderSet.Named<Item>> logs_ = RegistryUtils.getItemTagContents(ItemTags.LOGS);
        if (logs_.isPresent()) {
            for (Holder<Item> holder : logs_.get()) {
                Item log = holder.value();
                Optional<ResourceKey<Item>> name_ = holder.unwrapKey();
                if (name_.isPresent()) {
                    ResourceLocation location = name_.get().location();
                    String name = location.getPath();
                    boolean stripped = name.startsWith("stripped");
                    int materialStart = stripped ? 9 : 0;
                    String material;
                    if (name.endsWith("log")) {
                        material = name.substring(materialStart, name.length() - 4);
                        if (!stripped)
                            materialToLogMap.put(material, log);
                    } else if (name.endsWith("stem")) {
                        // noinspection DuplicateExpressions
                        material = name.substring(materialStart, name.length() - 5);
                        if (!stripped)
                            materialToLogMap.put(material, log);
                    } else {
                        if (!woodEnabled) continue;
                        // noinspection DuplicateExpressions
                        material = name.endsWith("wood")
                            ? name.substring(materialStart, name.length() - 5)
                            : name.substring(materialStart, name.length() - 7);
                    }

                    graph.addConnection(materialToLogMap.get(material), log);
                    if (doorEnabled) {
                        RegistryUtils.getItem(
                            LocationUtils.fromNamespaceAndPath(namespace, material + "_door")
                        ).ifPresent(item -> graph.addConnection(log, item, 2));
                    }
                }
            }
        }

        Optional<HolderSet.Named<Item>> planks_ = RegistryUtils.getItemTagContents(ItemTags.PLANKS);
        if (planks_.isPresent()) {
            for (Holder<Item> holder : planks_.get()) {
                Item plank = holder.value();
                Optional<ResourceLocation> plankLocation_ = RegistryUtils.getItemLocation(plank);
                if (plankLocation_.isEmpty())
                    continue;
                ResourceLocation plankLocation = plankLocation_.get();
                String namespace = plankLocation.getNamespace();
                String plankName = plankLocation.getPath();
                String material = StringUtils.removeEnd(plankName, "_planks");

                if (materialToLogMap.containsKey(material))
                    graph.addConnection(materialToLogMap.get(material), plank, 4);

                Consumer<Item> add = item -> graph.addConnection(plank, item);
                Consumer<Item> add2 = item -> graph.addConnection(plank, item, 2);

                RegistryUtils.getItem(LocationUtils.fromNamespaceAndPath(namespace, material + "_slab"))
                             .ifPresent(add2);
                RegistryUtils.getItem(LocationUtils.fromNamespaceAndPath(namespace, material + "_stairs"))
                             .ifPresent(add);
                if (fenceEnabled)
                    RegistryUtils.getItem(LocationUtils.fromNamespaceAndPath(namespace, material + "_fence"))
                                 .ifPresent(add);
                if (fenceGateEnabled)
                    RegistryUtils.getItem(LocationUtils.fromNamespaceAndPath(namespace, material + "_fence_gate"))
                                 .ifPresent(add);
                if (trapdoorEnabled && trapdoorBalance)
                    RegistryUtils.getItem(LocationUtils.fromNamespaceAndPath(namespace, material + "_trapdoor"))
                                 .ifPresent(add);
            }

            graph.addConnection(Items.BAMBOO_PLANKS, Items.BAMBOO_MOSAIC);
            graph.addConnection(Items.BAMBOO_MOSAIC, Items.BAMBOO_MOSAIC_SLAB, 2);
            graph.addConnection(Items.BAMBOO_MOSAIC, Items.BAMBOO_MOSAIC_STAIRS);
        }
        return graph;
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleOptionBuilder<Boolean> enabledBuilder =
            ConfigUtils.createEnabledOptionBuilderWithTitle(this.configBuilder);

        ModuleOptionBuilder<Boolean> woodBuilder = this.configBuilder.createBooleanOptionBuilder("wood");
        this.woodOption = woodBuilder.setTitle(woodBuilder
            .getTitleLangBuilder()
            .translation("zh_cn", "木头 / 菌核")
            .translation("en_us", "Woods / hyphae")
            .build()
        ).setTooltip(woodBuilder
            .getTooltipLangBuilder()
            .translation("zh_cn", "1 原木 / 菌柄 → 1 木头 / 菌核")
            .translation("en_us", "1 log / stem → 1 wood / hyphae")
            .build()
        ).setDefaultValue(true).build();

        ModuleOptionBuilder<Boolean> fenceBuilder = this.configBuilder.createBooleanOptionBuilder("fence");
        this.fenceOption = fenceBuilder.setTitle(fenceBuilder
            .getTitleLangBuilder()
            .translation("zh_cn", "栅栏")
            .translation("en_us", "Fences")
            .build()
        ).setTooltip(fenceBuilder
            .getTooltipLangBuilder()
            .translation("zh_cn", "1 木板 → 1 栅栏 / 1 原木 → 4 栅栏")
            .translation("en_us", "1 plank → 1 fence / 1 log → 4 fences")
            .build()
        ).setDefaultValue(true).build();

        ModuleOptionBuilder<Boolean> fenceGateBuilder = this.configBuilder.createBooleanOptionBuilder("fence_gate");
        this.fenceGateOption = fenceGateBuilder.setTitle(fenceGateBuilder
            .getTitleLangBuilder()
            .translation("zh_cn", "栅栏门")
            .translation("en_us", "Fence Gates")
            .build()
        ).setTooltip(fenceGateBuilder
            .getTooltipLangBuilder()
            .translation("zh_cn", "1 木板 → 1 栅栏门 / 1 原木 → 4 栅栏门")
            .translation("en_us", "1 plank → 1 fence gate / 1 log → 4 fence gates")
            .build()
        ).setDefaultValue(true).build();

        ModuleOptionBuilder<Boolean> trapdoorBuilder = this.configBuilder.createBooleanOptionBuilder("trapdoor");
        this.trapdoorOption = trapdoorBuilder.setTitle(trapdoorBuilder
            .getTitleLangBuilder()
            .translation("zh_cn", "活板门")
            .translation("en_us", "Trapdoors")
            .build()
        ).setTooltip(trapdoorBuilder
            .getTooltipLangBuilder()
            .translation("zh_cn", "1 木板 → 1 活板门 / 1 原木 → 4 活板门\n%s")
            .translation("en_us", "1 plank → 1 trapdoor / 1 log → 4 trapdoors\n%s")
            .child(trapdoorBuilder
                .createTooltipChildLangBuilder()
                .translation("zh_cn", "仅当【%s】模块开启时生效")
                .translation("en_us", "Only effective when [%s] module is enabled")
                .child(TrapdoorBalance.TITLE.getPlaceholder())
                .format(ChatFormatting.ITALIC)
                .build()
            ).build()
        ).setDefaultValue(true).build();

        ModuleOptionBuilder<Boolean> doorBuilder = this.configBuilder.createBooleanOptionBuilder("door");
        this.doorOption = doorBuilder.setTitle(doorBuilder
            .getTitleLangBuilder()
            .translation("zh_cn", "门")
            .translation("en_us", "Doors")
            .build()
        ).setTooltip(doorBuilder
            .getTooltipLangBuilder()
            .translation("zh_cn", "1 原木 → 2 门")
            .translation("en_us", "1 log → 2 doors")
            .build()
        ).setDefaultValue(true).build();

        return this.configBuilder
            .setTitle(TITLE)
            .defineEnabled(enabledBuilder
                .setTooltip(enabledBuilder
                    .getTooltipLangBuilder()
                    .translation("zh_cn", "添加原木/木板切石机配方\n%s")
                    .translation("en_us", "Add stonecutting recipes for logs and planks\n%s")
                    .child(enabledBuilder
                        .createTooltipChildLangBuilder(this.moduleId)
                        .translation("zh_cn", "兼容【%s】模块")
                        .translation("en_us", "Compatible with [%s] module")
                        .child(ReversibleCutting.TITLE.getPlaceholder())
                        .format(ChatFormatting.ITALIC)
                        .build()
                    ).build()
                ).build()
            ).defineOption(this.woodOption)
            .defineOption(this.fenceOption)
            .defineOption(this.fenceGateOption)
            .defineOption(this.trapdoorOption)
            .defineOption(this.doorOption)
            .build();
    }
}