package de.derfrzocker.ore.control.impl.v_14_R1;

import com.mojang.datafixers.Dynamic;
import de.derfrzocker.ore.control.api.Biome;
import de.derfrzocker.ore.control.api.NMSReplacer;
import net.minecraft.server.v1_14_R1.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("Duplicates")
public class NMSReplacer_v1_14_R1 implements NMSReplacer {

    @Override
    public void replaceNMS() {
        for (Field field : Biomes.class.getFields()) {
            try {
                replaceBase((BiomeBase) field.get(null));
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error while hook in NMS for Biome field: " + field.getName(), e);
            }
        }
    }

    private void replaceBase(final BiomeBase base) throws NoSuchFieldException, IllegalAccessException {
        final Biome biome;

        try {
            biome = Biome.valueOf(IRegistry.BIOME.getKey(base).getKey().toUpperCase());
        } catch (IllegalArgumentException e) {
            return;
        }

        final Map<WorldGenStage.Decoration, List<WorldGenFeatureConfigured<?>>> map = get(base);

        final List<WorldGenFeatureConfigured<?>> list = map.get(WorldGenStage.Decoration.UNDERGROUND_ORES);

        for (WorldGenFeatureConfigured<?> composite : list)
            replace(composite, biome);
    }

    @SuppressWarnings("unchecked")
    private Map<WorldGenStage.Decoration, List<WorldGenFeatureConfigured<?>>> get(final BiomeBase base)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassCastException {

        final Field field = getField(base.getClass(), "r");
        field.setAccessible(true);

        return (Map<WorldGenStage.Decoration, List<WorldGenFeatureConfigured<?>>>) field.get(base);
    }

    @SuppressWarnings("rawtypes")
    private Field getField(final Class clazz, final String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            final Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

    private void replace(final WorldGenFeatureConfigured<?> composite, final Biome biome) throws NoSuchFieldException, IllegalAccessException {
        if (replaceBadlandsGold(composite, biome))
            return;

        if (replaceEmerald(composite, biome))
            return;

        if (replaceLapis(composite, biome))
            return;

        replaceNormal(composite, biome);
    }

    private boolean replaceBadlandsGold(final WorldGenFeatureConfigured<?> composite, final Biome biome) throws NoSuchFieldException, IllegalAccessException {
        if (!(composite.b instanceof WorldGenFeatureCompositeConfiguration))
            return false;

        final WorldGenFeatureCompositeConfiguration worldGenFeatureDecoratorConfiguration = (WorldGenFeatureCompositeConfiguration) composite.b;

        if (!(worldGenFeatureDecoratorConfiguration.b.b instanceof WorldGenFeatureChanceDecoratorCountConfiguration))
            return false;

        final WorldGenFeatureChanceDecoratorCountConfiguration configuration = (WorldGenFeatureChanceDecoratorCountConfiguration) worldGenFeatureDecoratorConfiguration.b.b;

        if (configuration.a != 20)
            return false;

        if (configuration.b != 32)
            return false;

        if (configuration.c != 32)
            return false;

        if (configuration.d != 80)
            return false;

        {
            final Field field = getField(worldGenFeatureDecoratorConfiguration.b.getClass(), "a");
            field.setAccessible(true);
            field.set(worldGenFeatureDecoratorConfiguration.b, new WorldGenDecoratorNetherHeightBadlandsGoldOverrider_v1_14_R1(getDynamicFunction(worldGenFeatureDecoratorConfiguration.b.a), biome));
        }

        return true;
    }

    private boolean replaceEmerald(final WorldGenFeatureConfigured<?> composite, final Biome biome) throws NoSuchFieldException, IllegalAccessException {
        if (!(composite.b instanceof WorldGenFeatureCompositeConfiguration))
            return false;

        final WorldGenFeatureCompositeConfiguration worldGenFeatureDecoratorConfiguration = (WorldGenFeatureCompositeConfiguration) composite.b;

        if (!(worldGenFeatureDecoratorConfiguration.b.b instanceof WorldGenFeatureDecoratorEmptyConfiguration))
            return false;

        {
            final Field field = getField(worldGenFeatureDecoratorConfiguration.b.getClass(), "a");
            field.setAccessible(true);
            field.set(worldGenFeatureDecoratorConfiguration.b, new WorldGenDecoratorEmeraldOverrider_v1_14_R1(getDynamicFunction1(worldGenFeatureDecoratorConfiguration.b.a),biome));
        }

        return true;
    }

    private boolean replaceLapis(final WorldGenFeatureConfigured<?> composite, final Biome biome) throws NoSuchFieldException, IllegalAccessException {
        if (!(composite.b instanceof WorldGenFeatureCompositeConfiguration))
            return false;

        final WorldGenFeatureCompositeConfiguration worldGenFeatureDecoratorConfiguration = (WorldGenFeatureCompositeConfiguration) composite.b;

        if (!(worldGenFeatureDecoratorConfiguration.b.b instanceof WorldGenDecoratorHeightAverageConfiguration))
            return false;

        {
            final Field field = getField(worldGenFeatureDecoratorConfiguration.b.getClass(), "a");
            field.setAccessible(true);
            field.set(worldGenFeatureDecoratorConfiguration.b, new WorldGenDecoratorHeightAverageOverrider_v1_14_R1(getDynamicFunction2(worldGenFeatureDecoratorConfiguration.b.a), biome));
        }

        return true;
    }

    private void replaceNormal(final WorldGenFeatureConfigured<?> composite, final Biome biome) throws NoSuchFieldException, IllegalAccessException {
        if (!(composite.b instanceof WorldGenFeatureCompositeConfiguration))
            return;

        final WorldGenFeatureCompositeConfiguration worldGenFeatureDecoratorConfiguration = (WorldGenFeatureCompositeConfiguration) composite.b;

        if(!(worldGenFeatureDecoratorConfiguration.b.a instanceof WorldGenDecoratorNetherHeight))
            return;

        {
            final Field field = getField(worldGenFeatureDecoratorConfiguration.b.getClass(), "a");
            field.setAccessible(true);
            field.set(worldGenFeatureDecoratorConfiguration.b, new WorldGenDecoratorNetherHeightNormalOverrider_v1_14_R1(getDynamicFunction(worldGenFeatureDecoratorConfiguration.b.a), biome));
        }
    }

    @SuppressWarnings("unchecked")
    private Function<Dynamic<?>, ? extends WorldGenFeatureChanceDecoratorCountConfiguration> getDynamicFunction(WorldGenDecorator<?> worldGenDecorator) throws IllegalAccessException, NoSuchFieldException {
        final Field field = getField(worldGenDecorator.getClass(), "M");
        field.setAccessible(true);
        return (Function<Dynamic<?>, ? extends WorldGenFeatureChanceDecoratorCountConfiguration>) field.get(worldGenDecorator);
    }

    @SuppressWarnings("unchecked")
    private Function<Dynamic<?>, ? extends  WorldGenFeatureDecoratorEmptyConfiguration> getDynamicFunction1(WorldGenDecorator<?> worldGenDecorator) throws IllegalAccessException, NoSuchFieldException {
        final Field field = getField(worldGenDecorator.getClass(), "M");
        field.setAccessible(true);
        return (Function<Dynamic<?>, ? extends WorldGenFeatureDecoratorEmptyConfiguration>) field.get(worldGenDecorator);
    }

    @SuppressWarnings("unchecked")
    private Function<Dynamic<?>, ? extends  WorldGenDecoratorHeightAverageConfiguration> getDynamicFunction2(WorldGenDecorator<?> worldGenDecorator) throws IllegalAccessException, NoSuchFieldException {
        final Field field = getField(worldGenDecorator.getClass(), "M");
        field.setAccessible(true);
        return (Function<Dynamic<?>, ? extends WorldGenDecoratorHeightAverageConfiguration>) field.get(worldGenDecorator);
    }

}