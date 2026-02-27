# 动态数据扩展

**简体中文** | [English](README.md)

[//]: # (**Modrinth**：[Dynamic Data Extensions]&#40;&#41;)

<div style="text-align: center;">
<img src="src/main/resources/icon_256x.png" alt="icon_256x.png"/>
</div>

本模组添加了一系列模块化、动态加载的实用数据组件，以及一系列可用于创建动态数据的 API（用于制作附属模组）；

本模组旨在使用一种更加灵活、易于使用和扩展的方案，替代仅包含配方、标签等修改的轻度魔改数据包。

推荐与 JEI 和 Cloth Config API 一同使用。

* **适用版本：** Minecraft 1.21.1 NeoForge

* **运行环境：** 客户端可选，服务端需装

## 特性

### 动态数据生成

运行时生成数据，可为每种材料生成单独的配方（可使用本模组动态添加的标签进行匹配），无需为每种材料单独编写配方，且自动适配其他模组；

还可根据其他模块的启用状态改变生成的内容。

<details>
<summary>已支持数据类型：</summary>

* 原版配方（基于`RecipeBuilder`）
    * [有序合成](src/main/java/com/xyc/dynamicdataext/mixins/recipes/ShapedRecipeBuilderMixin.java)
    * [无序合成](src/main/java/com/xyc/dynamicdataext/mixins/recipes/ShapelessRecipeBuilderMixin.java)
    * [烧炼](src/main/java/com/xyc/dynamicdataext/mixins/recipes/SimpleCookingRecipeBuilderMixin.java)（熔炉 / 高炉 / 烟熏炉 / 营火）
    * [切石机](src/main/java/com/xyc/dynamicdataext/mixins/recipes/SingleItemRecipeBuilderMixin.java)
    * [锻造升级](src/main/java/com/xyc/dynamicdataext/mixins/recipes/SmithingTransformRecipeBuilderMixin.java)
    * [盔甲纹饰](src/main/java/com/xyc/dynamicdataext/mixins/recipes/SmithingTrimRecipeBuilderMixin.java)
    * [定制配方 / 特殊配方](src/main/java/com/xyc/dynamicdataext/mixins/recipes/SpecialRecipeBuilderMixin.java)
* 标签

</details>

> 例：根据 `c:storage_blocks/raw_(.*)` 标签匹配所有粗矿物块，生成将它们烧炼成对应矿物块的配方

### 模块化加载

将一种或一系列功能相似的内容组成模块，每个模块可以单独配置是否启用；部分模块还提供了更多配置项。

### 游戏内配置界面

需要 [Cloth Config API](https://github.com/shedaniel/ClothConfig/) 模组。

### 自动重新加载

每次更改配置后立即自动重新加载数据（与`/reload`相同）使变更生效（可在配置中禁用）。

### 兼容性

动态数据基于原版数据类，除通过 Mixin 注入数据外不修改游戏运行逻辑，已测试在 200+ 模组环境中能够正常工作；

### 附属支持

附属模组可利用本模组的数据注入系统，只需仿照本模组模式编写模块、Mixin（如果需要支持其他模组中的自定义数据，如[机械动力](https://github.com/Creators-of-Create/Create)的加工配方）和模组初始化方法，即可实现运行时动态生成数据，并自动拥有本模组同款配置系统。

## 计划中的内容

* 战利品列表、进度；
* 更多配置项类型；
* 未安装 Cloth Config API 的提示页面；
* 附属模组及教程；
* 向 1.20.1 Forge 移植。

> * 本模组只支持原版配方类型，其他模组配方类型的支持将由附属模组完成；
>
> * 暂无向除 1.20.1 Forge 之外的版本移植的计划。
